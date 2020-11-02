package com.camunda.couchbase.rs.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.camunda.couchbase.config.AppConstants;
import com.camunda.couchbase.model.AppResponse;
import com.camunda.couchbase.model.request.CountryTarget;
import com.camunda.couchbase.rs.CountryTargetRestService;
import com.camunda.couchbase.service.CountryTargetService;

@RestController
public class CountryTargetRestServiceImpl implements CountryTargetRestService {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	CountryTargetService countryTargetService;

	@Override
	public AppResponse getByKey(CountryTarget countryTarget) {
		log.info("Get country target for key : {}", countryTarget.getKey());
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(countryTargetService.getByKey(countryTarget.getKey()));
		return appResponse;
	}

	@Override
	public AppResponse getAll() {
		log.info("Get all country target");
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(countryTargetService.getAll());
		return appResponse;
	}

	@Override
	public AppResponse getByPortfolio(CountryTarget countryTarget) {
		log.info("Get country target for portfolio : {}", countryTarget.getPortfolioShortName());
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(countryTargetService.getByPortfolio(countryTarget.getPortfolioShortName()));
		return appResponse;
	}

	@Override
	public AppResponse getByPmGroup(CountryTarget countryTarget) {
		log.info("Get country target for user group : {}", countryTarget.getPmGroup());
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(countryTargetService.getByPmGroup(countryTarget.getPmGroup()));
		return appResponse;
	}

	@Override
	public AppResponse getByPortfolioAndPmGroup(CountryTarget countryTarget) {
		log.info("Get country target for portfolio : {}, user group : {}", countryTarget.getPortfolioShortName(), countryTarget.getPmGroup());
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(countryTargetService.getByPortfolioAndPmGroup(countryTarget.getPortfolioShortName(), countryTarget.getPmGroup()));
		return appResponse;
	}

}
