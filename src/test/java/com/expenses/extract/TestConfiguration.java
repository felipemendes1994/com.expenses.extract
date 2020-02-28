package com.expenses.extract;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.expenses.extract.utility.HttpCommonResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"com.expenses"})
public class TestConfiguration {
	
	@Value("${application.uri}")
	private String applicationHost;
	
    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return requestHeaders;
    }

    @Bean("Test")
    public RestTemplate restTemplateWithErrorHandler(RestTemplateBuilder builder) {
        RestTemplate build = builder.build();
        build.setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                return false;
            };
        });
        build.setUriTemplateHandler(new DefaultUriBuilderFactory(applicationHost));
        return build;
    }
    
    @Bean
    public HttpCommonResponse commonResponse() {
    	return new HttpCommonResponse();
    }
    
    @Bean
    public WireMockServer wiremockServer() {
    	WireMockServer wiremockServer = new WireMockServer(8081);
    	wiremockServer.start();
    	return wiremockServer;
    }
    
    @Bean
    public WireMock wiremock() {
    	return new WireMock(8081);
    }
}
