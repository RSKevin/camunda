package com.camunda.couchbase.service;

import java.util.List;
import java.util.Map;

public interface CountryTargetService {
	public Map<String, Object> getByKey(String key);
    public List<Map<String, Object>> getAll();
	public List<Map<String, Object>> getByPortfolio(String portfolioShortName);
	public List<Map<String, Object>> getByPmGroup(String pmGroup);
	public List<Map<String, Object>> getByPortfolioAndPmGroup(String portfolioShortName, String pmGroup);
}
