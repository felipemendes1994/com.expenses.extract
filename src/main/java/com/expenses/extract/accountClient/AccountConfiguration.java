package com.expenses.extract.accountClient;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class AccountConfiguration {

    @Bean("accountRestTemplate")
    public RestTemplate restTemplateWithErrorHandler(RestTemplateBuilder builder) {
        RestTemplate build = builder.build();
        build.setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                return false;
            };
        });
        build.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8081"));
        return build;
    }
}
