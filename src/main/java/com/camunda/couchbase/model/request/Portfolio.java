package com.camunda.couchbase.model.request;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {
	private String key;
	private String portfolioShortName;
	private String userName;
}