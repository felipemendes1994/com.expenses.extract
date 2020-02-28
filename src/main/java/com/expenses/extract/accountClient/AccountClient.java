package com.expenses.extract.accountClient;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.expenses.extract.accountClient.dao.AccountRequest;
import com.expenses.extract.accountClient.dao.AccountResponse;

@Component
public class AccountClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountClient.class);

	@Autowired
	@Qualifier("accountRestTemplate")
	private RestTemplate restTemplate;
	
	public Optional<AccountResponse> findAccount(String account, String agency) {
		String uri = String.format("/accounts/%s/agencies/%s", account, agency);
		
		try {
			LOGGER.info(String.format("Requesting Account %s Agency %s record existance", account, agency));
			ResponseEntity<AccountResponse> response = restTemplate.getForEntity(uri, AccountResponse.class);
			return Optional.of(response.getBody());
		}catch(Exception e) {
			LOGGER.warn(String.format("Account %s Agency %s record not found", account, agency));
			return Optional.empty();
		}
	}
	
	public void createAccount(AccountRequest request) {
		try {
			LOGGER.info(String.format("Requesting Account %s Agency %s record creation", 
					request.getAccountNumber(), request.getAgencyNumber()));
			ResponseEntity<Void> response = restTemplate.exchange("/accounts", HttpMethod.POST, new HttpEntity<>(request), Void.class);
			
			if(response.getStatusCode().is2xxSuccessful()) {
				LOGGER.info(String.format("Account %s Agency %s record created successfully",
						request.getAccountNumber(), request.getAgencyNumber()));
			}
		}catch(Exception e) {
			LOGGER.error(String.format("Account %s Agency %s record creation failed"), 
					request.getAccountNumber(), request.getAgencyNumber());
			e.printStackTrace();
		}
	}
}
