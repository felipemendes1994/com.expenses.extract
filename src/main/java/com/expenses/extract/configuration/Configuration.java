package com.expenses.extract.configuration;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;

@SpringBootConfiguration
public class Configuration {
	
	@Bean
	public AggregateUnmarshaller<ResponseEnvelope> aggregateUnmarshaller() {
		return new AggregateUnmarshaller<ResponseEnvelope>(ResponseEnvelope.class); 
	}
}
