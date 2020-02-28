package com.expenses.extract.steps;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.expenses.extract.utility.HttpCommonResponse;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern;

import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.Quando;

public class ImportacaoExtratoBancario {
	
	@Autowired
	private WireMock mock;
	
	@Autowired
	@Qualifier("Test")
	private RestTemplate restTemplate;
	
	@Autowired
	private HttpHeaders defaultHeaders;
	
	@Autowired
	private HttpCommonResponse result;
	
	@Dado("^que o account esteja respondendo na API de GET \"([^\"]*)\" o status (\\d+)$")
	public void que_o_account_esteja_respondendo_na_API_de_GET_o_status(String uri, int status) throws Throwable {
        mock.register(
    		get(urlPathMatching(uri))
            .willReturn(
        		aResponse()
                .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .withStatus(status)
            ));
	}
	
	@Dado("^que o account esteja respondendo na API de POST \"([^\"]*)\" o status (\\d+) e request payload$")
	public void que_o_account_esteja_respondendo_na_API_de_POST_o_status_e_request_payload(String uri, int status, String request) throws Throwable {
		mock.register(
				post(urlPathMatching(uri))
				.withRequestBody(new EqualToJsonPattern(request, true, true))
				.willReturn(aResponse()
					.withStatus(status)
				));
	}
	
	@Quando("^a API de POST para importacao de extrato \"([^\"]*)\" for chamada com o body \"([^\"]*)\"$")
	public void a_API_de_POST_para_importacao_de_extrato_for_chamada_com_o_body(String uri, String body) throws Throwable {
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(body), String.class);
		result.setBody(response.getBody());
		result.setStatus(response.getStatusCode());
	}
}
