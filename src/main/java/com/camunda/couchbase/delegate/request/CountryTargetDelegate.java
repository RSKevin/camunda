package com.camunda.couchbase.delegate.request;

import static org.camunda.spin.Spin.JSON;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountryTargetDelegate implements JavaDelegate {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("In execute method of CountryTargetDelegate");
		Map<String, Object> payload = new HashMap<>();
		payload.put("pmGroup", (String) execution.getVariable("pmGroup"));
		payload.put("portfolioShortName", (String) execution.getVariable("portfolioShortName"));
		execution.setVariable("countryTargetRequest", JSON(payload).toString());
	}
}
