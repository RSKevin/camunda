package com.camunda.couchbase.rs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.camunda.couchbase.config.AppConstants;
import com.camunda.couchbase.model.AppResponse;
import com.camunda.couchbase.model.request.RebalanceRequest;
import com.camunda.couchbase.rs.CamundaRestService;
import com.camunda.couchbase.service.CamundaService;

@RestController
public class CamundaRestServiceImpl implements CamundaRestService {

	@Autowired
	private CamundaService camundaService;

	@Override
	public AppResponse startProcessByKey(String key) {
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(camundaService.startProcessByKey(key));
		return appResponse;
	}

	@Override
	public AppResponse getTask(String processInstanceId) {
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(camundaService.taskByProcessInstanceId(processInstanceId));
		return appResponse;
	}

	@Override
	public AppResponse doRebalanceWi(RebalanceRequest rebalanceRequest) {
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(camundaService.doRebalanceWi(rebalanceRequest));
		return appResponse;
	}
	
	@Override
	public AppResponse doRebalance(RebalanceRequest rebalanceRequest) {
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.SUCCESS_CODE);
		appResponse.setErrorFlag(false);
		appResponse.setData(camundaService.doRebalance(rebalanceRequest));
		return appResponse;
	}

}
