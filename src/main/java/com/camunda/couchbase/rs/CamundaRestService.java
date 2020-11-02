package com.camunda.couchbase.rs;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camunda.couchbase.model.AppResponse;
import com.camunda.couchbase.model.request.RebalanceRequest;

@RequestMapping("/api")
public interface CamundaRestService {
	
	@PostMapping("/process/{key}/start")
	public AppResponse startProcessByKey(@PathVariable String key);
	
	@PostMapping("/task/{processInstanceId}")
	public AppResponse getTask(@PathVariable String processInstanceId);
	
	@PostMapping("/rebalance-wi")
	public AppResponse doRebalanceWi(@RequestBody RebalanceRequest rebalanceRequest);
	
	@PostMapping("/rebalance")
	public AppResponse doRebalance(@RequestBody RebalanceRequest rebalanceRequest);

}