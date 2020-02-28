package com.expenses.extract.cucumber;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale.Category;

import javax.persistence.EntityManager;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import com.expenses.extract.TestConfiguration;

import cucumber.api.java.Before;

@ContextConfiguration(classes = {TestConfiguration.class}, loader = SpringBootContextLoader.class)
public class DatabaseCleanUp {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseCleanUp.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private EntityManager em;
	
    @Before
    public void tearDownDatabase() {

//        checkIsNotProductionDatabase();

//        deleteDatabaseInfo(AccountTransaction.class, Category.class, Account.class);
    }

    private void deleteDatabaseInfo(Class<?>... entityClasses) {

        List<String> deleteCommands = new ArrayList<>();
        List<String> ordenedTableList = getTableNameInOrder(entityClasses);

        verifyForNotDeclaredClasses(ordenedTableList);

        for (String tableName : ordenedTableList) {
            deleteCommands.add(String.format("DELETE FROM %s", tableName));
            LOGGER.debug("DELETE FROM {}", tableName);
        }

        jdbcTemplate.batchUpdate(deleteCommands.toArray(new String[deleteCommands.size()]));
        LOGGER.info("Database tables cleaned");
    }

    private void verifyForNotDeclaredClasses(List<String> ordenedTableList) {
        if (em.getMetamodel().getEntities().size() != ordenedTableList.size()) {
            throw new IllegalStateException(
                    String.format("Test classes mismatch declared classes quantity [Test classes %s, Declared Classes %s",
                            em.getMetamodel().getEntities().size(), ordenedTableList.size()));
        }
    }

    private List<String> getTableNameInOrder(Class<?>... entityClasses) {
        List<String> tableNames = new LinkedList<>();
        for (Class<?> clazz : entityClasses) {
            tableNames.add(clazz.getAnnotation(Table.class).name());
        }

        return tableNames;
    }

    private void checkIsNotProductionDatabase() {
        List<String> result = jdbcTemplate.query("select inet_server_addr()", (rs, i) -> rs.getString(1));

        if (result.isEmpty()) {
            throw new IllegalStateException("Cant check if is production database");
        }

        if (result.get(0).equals("10.128.33.137") || result.get(0).equals("10.128.33.138")) {
            throw new IllegalStateException("Trying to execute test delete in Production Database");
        }
    }
}

