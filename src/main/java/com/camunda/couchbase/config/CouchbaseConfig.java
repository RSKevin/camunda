package com.camunda.couchbase.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class CouchbaseConfig {
	private String bucket; 
	private String couchbaseUsername;
	private String couchbasePassword;
	private String environment;
	private List<String> servers;
}
