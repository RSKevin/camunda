package com.camunda.couchbase.rs.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.camunda.couchbase.config.AppConstants;
import com.camunda.couchbase.model.AppResponse;
import com.camunda.couchbase.rs.DocumentRestService;
import com.camunda.couchbase.service.CouchbaseQueryService;

@RestController
public class DocumentRestServiceImpl implements DocumentRestService {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	CouchbaseQueryService couchbaseQueryService;

	@Override
	public AppResponse getByDocKey(String key) {
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(couchbaseQueryService.getByKey(key));
		return appResponse;
	}
	
	@Override
	public AppResponse getByKey(Map<String, Object> payload) {
		log.info("Input pyload {}", payload);
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(couchbaseQueryService.getByKey(payload.get("key").toString()));
		return appResponse;
	}

	@Override
	public AppResponse getByType(String type) {
		log.info("Before calling query service");
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(couchbaseQueryService.getByType(type));
		return appResponse;
	}

	@Override
	public AppResponse upsert(Map<String, Object> payload) {
		log.info("Input pyload {}", payload);
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(couchbaseQueryService.upsert(payload));
		return appResponse;
	}

	@Override
	public AppResponse insert(Map<String, Object> payload) {
		log.info("Input pyload {}", payload);
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(couchbaseQueryService.insert(payload));
		return appResponse;
	}
	
	@Override
	public AppResponse bulkInsert(List<Map<String, Object>> payload) {
		log.info("Input pyload {}", payload);
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(couchbaseQueryService.bulkInsert(payload));
		return appResponse;
	}

}
