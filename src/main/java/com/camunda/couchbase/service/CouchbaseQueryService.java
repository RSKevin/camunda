package com.camunda.couchbase.service;

import java.util.List;
import java.util.Map;

import com.couchbase.client.java.query.Statement;

public interface CouchbaseQueryService {
    public Map<String, Object> getByKey(String key);
    public List<Map<String, Object>> getByType(String type);
    public List<Map<String, Object>> selectQuery(Statement statement);
    public List<Map<String, Object>> selectQuery(String fields);
	public List<Map<String, Object>> selectQuery(String fields, String condition);
    public Map<String, Object> upsert(Map<String, Object> payload);
	public Map<String, Object> insert(Map<String, Object> payload);
	public Map<String, Object> bulkInsert(List<Map<String, Object>> payload);
	public Map<String, Object> update(Map<String, Object> payload);

    public Map<String, Object> updateByKey(String fields, String key, String retuningFields);
}
