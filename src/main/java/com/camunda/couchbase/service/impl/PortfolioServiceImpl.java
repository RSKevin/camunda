package com.camunda.couchbase.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camunda.couchbase.service.CouchbaseQueryService;
import com.camunda.couchbase.service.PortfolioService;

@Service
public class PortfolioServiceImpl implements PortfolioService {

	@Autowired
	CouchbaseQueryService couchbaseQueryService;

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public Map<String, Object> getByKey(String key) {
		return couchbaseQueryService.getByKey(key);
	}

	@Override
	public List<Map<String, Object>> getAll() {
		log.info("Framing fields and condition for query service");
		String fields = "fundShortName, fundName, netAssets, availableCash, availableCashBps, dateTimeChanged";
		String condition = "type = 'portfolioSummary'";
		return couchbaseQueryService.selectQuery(fields, condition);
	}

}