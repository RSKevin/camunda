package com.camunda.couchbase.service;

import java.util.List;
import java.util.Map;

public interface PortfolioService {
	public Map<String, Object> getByKey(String key);
    public List<Map<String, Object>> getAll();
}
