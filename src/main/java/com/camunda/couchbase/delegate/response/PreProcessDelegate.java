package com.camunda.couchbase.delegate.response;

import static org.camunda.spin.Spin.JSON;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
//import org.camunda.spin.json.SpinJsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreProcessDelegate implements JavaDelegate {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("In execute method of PreProcessDelegate");
		execution.setVariable("preProcessData", JSON(execution.getVariable("preProcessResponse")).prop("data").toString());
	}
}
