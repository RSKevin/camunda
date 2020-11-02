package com.camunda.couchbase.delegate.request;

import static org.camunda.spin.Spin.JSON;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreProcessDelegate implements JavaDelegate {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("In execute method of PreProcessDelegate");
		Map<String, Object> payload = new HashMap<>();
		payload.put("portfolioData", JSON(execution.getVariable("portfolioData")).mapTo(Map.class));
		payload.put("processInstanceId", execution.getProcessInstanceId());
		payload.put("userName", (String) execution.getVariable("userName"));
		payload.put("type", "rebalanceTest");
		payload.put("id", execution.getVariable("documentKey"));
		String preProcessRequest = JSON(payload).toString();
		log.info("PreProcessing Request {}", preProcessRequest);
		execution.setVariable("preProcessRequest", preProcessRequest);
		
	}
}