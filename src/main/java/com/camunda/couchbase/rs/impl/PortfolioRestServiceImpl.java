package com.camunda.couchbase.rs.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.camunda.couchbase.config.AppConstants;
import com.camunda.couchbase.model.AppResponse;
import com.camunda.couchbase.rs.PortfolioRestService;
import com.camunda.couchbase.service.PortfolioService;

@RestController
public class PortfolioRestServiceImpl implements PortfolioRestService {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	PortfolioService portfolioService;

	@Override
	public AppResponse getByKey(Map<String, Object> payload) {
		log.info("Input pyload {}", payload);
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(portfolioService.getByKey(payload.get("key").toString()));
		return appResponse;
	}
	
	@Override
	public AppResponse getAll() {
		log.info("Before calling query service");
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(portfolioService.getAll());
		return appResponse;
	}

}
