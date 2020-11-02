package com.camunda.couchbase.service.impl;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camunda.couchbase.config.CouchbaseConfig;
import com.camunda.couchbase.service.CouchbaseQueryService;
import com.camunda.couchbase.service.CountryTargetService;
import com.couchbase.client.java.query.Statement;

@Service
public class CountryTargetServiceImpl implements CountryTargetService {

	@Autowired
	CouchbaseQueryService couchbaseQueryService;

	@Autowired
	CouchbaseConfig couchbaseConfig;

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public Map<String, Object> getByKey(String key) {
		return couchbaseQueryService.getByKey(key);
	}

	@Override
	public List<Map<String, Object>> getAll() {
		return couchbaseQueryService.getByType("countryTargetTest");
	}

	@Override
	public List<Map<String, Object>> getByPortfolio(String portfolioShortName) {
		log.info("Framing Query for getByPortfolio");
		Statement statement = select(x("meta().id, equitypm.*")).from(i(couchbaseConfig.getBucket())).as("equitypm")
				.where(x("equitypm.type").eq(s("countryTargetTest")).and("equitypm.portfolioShortName")
						.eq(s(portfolioShortName)));
		return couchbaseQueryService.selectQuery(statement);
	}

	@Override
	public List<Map<String, Object>> getByPmGroup(String pmGroup) {
		log.info("Framing Query for getByUserGroup");
		Statement statement = select(x("meta().id, equitypm.*")).from(i(couchbaseConfig.getBucket())).as("equitypm")
				.where(x("equitypm.type").eq(s("countryTargetTest")).and("equitypm.pmGroup").eq(s(pmGroup)));
		return couchbaseQueryService.selectQuery(statement);
	}

	@Override
	public List<Map<String, Object>> getByPortfolioAndPmGroup(String portfolioShortName, String pmGroup) {
		log.info("Framing Query for getByPortfolioAndUserGroup");
		Statement statement = select(x("meta().id, equitypm.*")).from(i(couchbaseConfig.getBucket())).as("equitypm")
				.where(x("equitypm.type").eq(s("countryTargetTest")).and("equitypm.portfolioShortName")
						.eq(s(portfolioShortName)).and("equitypm.pmGroup").eq(s(pmGroup)));
		return couchbaseQueryService.selectQuery(statement);
	}

}