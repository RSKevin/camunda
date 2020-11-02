package com.camunda.couchbase.delegate.request;

import static org.camunda.spin.Spin.JSON;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortfolioDelegate implements JavaDelegate {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("In execute method of PortfolioDelegate");
		Map<String, Object> payload = new HashMap<>();
		payload.put("key", "registeredPortfolioTest::" + execution.getVariable("portfolioShortName"));
		//String portfolioRequest = JSON(payload).toString();
		log.info("PortfolioRequest {}", payload.get("key"));
		execution.setVariable("portfolioRequest", JSON(payload).toString());
	}
}
