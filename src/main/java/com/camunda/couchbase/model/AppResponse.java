package com.camunda.couchbase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppResponse {
	private int responseCode;
	private boolean errorFlag;
	private Object data;
}
