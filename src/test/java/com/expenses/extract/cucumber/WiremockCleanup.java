package com.expenses.extract.cucumber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;

import com.expenses.extract.TestConfiguration;
import com.github.tomakehurst.wiremock.client.WireMock;

import cucumber.api.java.Before;

@ContextConfiguration(classes = TestConfiguration.class, loader = SpringBootContextLoader.class)
public class WiremockCleanup {
	
	@Autowired
	private WireMock mock;
	
	@Before
	public void cleanWiremock() {
		mock.resetMappings();
	}
}
