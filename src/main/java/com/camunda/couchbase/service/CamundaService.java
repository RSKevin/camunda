package com.camunda.couchbase.service;

import java.util.Map;

import com.camunda.couchbase.model.request.RebalanceRequest;

public interface CamundaService {

	public Object startProcessByKey(String key);

	public Object taskByProcessInstanceId(String processInstanceId);

	public Map<String, Object> doRebalanceWi(RebalanceRequest rebalanceRequest);
	
	public Map<String, Object> doRebalance(RebalanceRequest rebalanceRequest);

}