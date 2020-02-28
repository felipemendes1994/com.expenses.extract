package com.expenses.extract.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expenses.extract.accountClient.AccountClient;
import com.expenses.extract.accountClient.dao.AccountRequest;
import com.expenses.extract.exceptions.BadRequestException;
//import com.expenses.account.entity.Account;
//import com.expenses.account.entity.AccountType;
//import com.expenses.account.service.AccountService;
//import com.expenses.exceptions.BadRequestException;
//import com.expenses.transaction.entity.AccountTransaction;
//import com.expenses.transaction.service.TransactionService;
import com.webcohesion.ofx4j.domain.data.MessageSetType;
import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.domain.data.ResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import com.webcohesion.ofx4j.domain.data.banking.BankingResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.common.Transaction;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;
import com.webcohesion.ofx4j.io.OFXParseException;

@Service
public class ExtractService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractService.class);

	@Autowired
	private AggregateUnmarshaller<ResponseEnvelope> aggregate;
	
	@Autowired
	private AccountClient accountClient;

//	@Autowired
//	private TransactionService transactionService;

	public void readExtract(String filePath) throws BadRequestException {
		try {
			LOGGER.info(String.format("Trying to load file from path ", filePath));
			ResponseEnvelope responseEnvelope = (ResponseEnvelope) aggregate.unmarshal(getFileInput(filePath));
			
			if (responseEnvelope.getMessageSet(MessageSetType.banking) != null) {
				startExtractAnalyzation(responseEnvelope);
			}
		} catch (IOException e) {
			LOGGER.error(String.format("Failed loading file %s", filePath));
			throw new BadRequestException(String.format("Failed reading file %s", filePath));
		} catch (OFXParseException e) {
			LOGGER.error(String.format("Failed parsing file %s", filePath));
			throw new BadRequestException(String.format("Failed reading file %s", filePath));
		}
	}

	private FileInputStream getFileInput(String filePath) throws BadRequestException {
		try {
			if(filePath.contains(System.getProperty("user.home"))) {
				return new FileInputStream(new File(filePath));
			}
			return new FileInputStream(new File(System.getProperty("user.home") + filePath));
		}catch(FileNotFoundException e) {
			throw new BadRequestException(String.format("File not found in directory %s", filePath));
		}
	}
	
	private void startExtractAnalyzation(ResponseEnvelope responseEnvelope) {
		analyzeAccount(responseEnvelope);
		analyzeTransactions(responseEnvelope);
	}
	

	private void analyzeAccount(ResponseEnvelope responseEnvelope) {
		LOGGER.info(String.format("Analyzing bank account record from extract file"));
		ResponseMessageSet message = responseEnvelope.getMessageSet(MessageSetType.banking);

		List<BankStatementResponseTransaction> banks = ((BankingResponseMessageSet) message).getStatementResponses();
		
		String bankNumber = responseEnvelope.getSignonResponse().getFinancialInstitution().getId();
		String bankName = responseEnvelope.getSignonResponse().getFinancialInstitution().getOrganization();
		
		for (BankStatementResponseTransaction bank : banks) {
			String account = bank.getMessage().getAccount().getAccountNumber();
			String agency = bank.getMessage().getAccount().getBranchId();
			
			if(!accountClient.findAccount(account, agency).isPresent()) {
				accountClient.createAccount(new AccountRequest(agency, account, bankNumber, bankName));
			}
		}
	}

	private void analyzeTransactions(ResponseEnvelope responseEnvelope) {
//		List<BankStatementResponseTransaction> banks = ((BankingResponseMessageSet) message).getStatementResponses();
//		
//		for (BankStatementResponseTransaction bank : banks) {
//			List<Transaction> transactions = bank.getMessage().getTransactionList().getTransactions();
//			
//			for(Transaction t : transactions) {
//				t.getAmount();
//				t.getDatePosted();
//				t.getMemo();
//				t.getTransactionType();
//			}
//		}
//	private void analyzeTransactions(List<Transaction> transactions, Account account) {
//		for (Transaction transaction : transactions) {
//			LocalDate datePosted = LocalDate.ofInstant(transaction.getDatePosted().toInstant(), ZoneOffset.UTC);
//			
//			AccountTransaction accountTransaction = new AccountTransaction();
//			accountTransaction.setAccountSource(account);
//			accountTransaction.setTransactionDate(datePosted);
//			accountTransaction.setValue(BigDecimal.valueOf(transaction.getAmount()).toString());
//			accountTransaction.setDescription(transaction.getMemo());
//			
//			transactionService.saveTransaction(accountTransaction);
//		}
//		LOGGER.info(String.format("analyzed %s transactions successfully!", transactions.size()));
	}
}
