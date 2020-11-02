package com.camunda.couchbase.delegate.response;

import static org.camunda.spin.Spin.JSON;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
//import org.camunda.spin.json.SpinJsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortfolioDelegate implements JavaDelegate {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("In execute method of PortfolioDelegate");
		/*SpinJsonNode jsonNode = JSON(execution.getVariable("portfolioResponse"));
		jsonNode = jsonNode.prop("data");
		log.info("Data {}", jsonNode);
		execution.setVariable("portfolioData", jsonNode.toString());*/
		execution.setVariable("portfolioData", JSON(execution.getVariable("portfolioResponse")).prop("data").toString());
	}
}
