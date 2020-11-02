package com.camunda.couchbase.model.request;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RebalanceRequest {
	private String portfolioShortName;
	private String userName;
	private String pmGroup;
	private boolean refresh;
}