package com.camunda.couchbase.delegate.response;

import static org.camunda.spin.Spin.JSON;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountryTargetDelegate implements JavaDelegate {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("In execute method of CountryTargetDelegate");
		execution.setVariable("countryTargetData", JSON(execution.getVariable("countryTargetResponse")).prop("data").toString());
	}
}
