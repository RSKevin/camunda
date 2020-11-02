package com.camunda.couchbase.service.impl;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;
import static org.camunda.spin.Spin.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.spin.json.SpinJsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camunda.couchbase.model.request.RebalanceRequest;
import com.camunda.couchbase.service.CamundaService;
import com.camunda.couchbase.service.CouchbaseQueryService;
import com.couchbase.client.java.query.Statement;

@Service
public class CamundaServiceImpl implements CamundaService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	CouchbaseQueryService couchbaseQueryService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	TaskService taskService;
	
	@Override
	public Object startProcessByKey(String key) {
		log.info("In startProcessByKey method Impl for {}", key);
		//ProcessInstance instance = runtimeService.startProcessInstanceByKey(key);
		String processInstanceId = runtimeService.startProcessInstanceByKey(key).getProcessInstanceId();
	    log.info("Process started with id : {}", processInstanceId);	    
		return processInstanceId;
	}

	@Override
	public Object taskByProcessInstanceId(String processInstanceId) {
		//Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
	    //taskService.complete(task.getId());
	    log.info("In taskByProcessInstanceId method Impl for {}", processInstanceId);
		return taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult().getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> doRebalanceWi(RebalanceRequest rebalanceRequest) {
		String key = "rebalanceTest::" + rebalanceRequest.getPortfolioShortName() + "::" + rebalanceRequest.getUserName();
		Map<String, Object> rebalanceData = couchbaseQueryService.getByKey(key);
		
		if (!rebalanceRequest.isRefresh() && rebalanceData != null) {
			log.info("Rebalancing done already!");
			return rebalanceData;
		} else {
			Map<String, Object> instanceVariables = new HashMap<>();
			instanceVariables.put("portfolioShortName", rebalanceRequest.getPortfolioShortName());
			instanceVariables.put("userName", rebalanceRequest.getUserName());
			instanceVariables.put("documentKey",key);
			log.info("Starting the instance");
			ProcessInstance instance = runtimeService.startProcessInstanceByKey("rebalance-wi", instanceVariables);
			for (int i = 0; i < 3; i++) {
				completeTask(instance.getProcessInstanceId());
			}
			return (Map<String, Object>) getValue(instance.getId(), "preProcessData");
		}
	}

	/*@Override
	public Map<String, Object> doRebalance(RebalanceRequest rebalanceRequest) {
		log.info("In doRebalance method Impl");
		String key = "rebalanceTest::" + rebalanceRequest.getPortfolioShortName() + "::" + rebalanceRequest.getUserName();
		Map<String, Object> rebalanceData = couchbaseQueryService.getByKey(key);
		
		if (!rebalanceRequest.isRefresh() && rebalanceData != null) {
			log.info("Rebalancing done already!");
			return rebalanceData;
		} else {
			Map<String, Object> response = new HashMap<>();
			Map<String, Object> instanceVariables = new HashMap<>();
			instanceVariables.put("portfolioShortName", rebalanceRequest.getPortfolioShortName());
			instanceVariables.put("userName", rebalanceRequest.getUserName());
			instanceVariables.put("pmGroup", rebalanceRequest.getPmGroup());
			instanceVariables.put("documentKey",key);
			log.info("Starting the instance");
			ProcessInstance instance = runtimeService.startProcessInstanceByKey("rebalance-v1", instanceVariables);
			completeTask(instance.getProcessInstanceId());
			log.info("Inserting Portfolio Data");
			Map<String, Object> insertedData = insertPortfolioData(instance.getId(), instanceVariables);
			response.putAll(insertedData);
			for (int i = 0; i < 2; i++) {
				completeTask(instance.getProcessInstanceId());
			}
			log.info("Adding Country Target Data");
			Map<String, Object> countryTargetData = addCountryTargetData(instance.getId(), instanceVariables);
			log.info("Setting Response {}", countryTargetData);
			response.putAll(countryTargetData);
			return response;
		}
	}*/

    @Override
    public Map<String, Object> doRebalance(RebalanceRequest rebalanceRequest) {
        log.info("In doRebalance method Impl");
        String key = "rebalanceTest::" + rebalanceRequest.getPortfolioShortName() + "::" + rebalanceRequest.getUserName();
        Map<String, Object> rebalanceData = couchbaseQueryService.getByKey(key);

        if (!rebalanceRequest.isRefresh() && rebalanceData != null) {
            log.info("Rebalancing done already!");
            return rebalanceData;
        } else {
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> instanceVariables = new HashMap<>();
            instanceVariables.put("portfolioShortName", rebalanceRequest.getPortfolioShortName());
            instanceVariables.put("userName", rebalanceRequest.getUserName());
            instanceVariables.put("pmGroup", rebalanceRequest.getPmGroup());
            instanceVariables.put("documentKey",key);
            log.info("Starting the instance");
            ProcessInstance instance = runtimeService.startProcessInstanceByKey("rebalance", instanceVariables);
            completeTask(instance.getProcessInstanceId());
            log.info("Inserting Portfolio Data");
            Map<String, Object> insertedData = insertPortfolioData(instance.getId(), instanceVariables);
            response.putAll(insertedData);
            for (int i = 0; i < 2; i++) {
                completeTask(instance.getProcessInstanceId());
            }
            log.info("Adding Country Target Data");
            Map<String, Object> countryTargetData = addCountryTargetData(instance.getId(), instanceVariables);
            log.info("Setting Response {}", countryTargetData);
            response.putAll(countryTargetData);
            return response;
        }
    }
	
	private void completeTask(String processInstanceId) {
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		log.info("Executing task {} with id {}", task.getName(), task.getId());
		taskService.complete(task.getId());
	}
	
	private Object getValue(String instanceId, String variable) {

		List<HistoricVariableInstance> historicVariables = processEngine.getHistoryService()
				.createHistoricVariableInstanceQuery().processInstanceId(instanceId).variableName(variable)
				.list();
		
		if (historicVariables != null) {
			String jsonString = historicVariables.get(0).getValue().toString();
			SpinJsonNode resultJson;
			boolean list = false;
			if (jsonString.startsWith("[")) {
				resultJson = JSON("{}").prop("data", JSON(jsonString));
				list = true;
			} else {
				resultJson = JSON(jsonString);
			}
			log.info("resultJson : {}", resultJson);
			return list ? JSON(resultJson).mapTo(Map.class).get("data") : JSON(resultJson).mapTo(Map.class);
		}
        return null;
	}

	private Object getValue(String instanceId, String rootVariable, String level1Variable) {
		List<HistoricVariableInstance> historicVariables = processEngine.getHistoryService()
				.createHistoricVariableInstanceQuery().processInstanceId(instanceId).variableName(rootVariable)
				.list();

		if (historicVariables != null) {
			String jsonString = JSON(historicVariables.get(0).getValue()).prop(level1Variable).toString();
			//String jsonString = JSON(historicVariables.get(0).getValue()).toString();
			SpinJsonNode resultJson;
			boolean list = false;
			if (jsonString.startsWith("[")) {
				resultJson = JSON("{}").prop("data", JSON(jsonString));
				list = true;
			} else {
				resultJson = JSON(jsonString);
			}
			log.info("resultJson : {}", resultJson);
			return list ? JSON(resultJson).mapTo(Map.class).get("data") : JSON(resultJson).mapTo(Map.class);
		}
		return null;
	}
	
	private Map<String, Object> insertPortfolioData(String instanceId, Map<String, Object> instanceVariables) {
		Map<String, Object> payload = new HashMap<>();
		/*payload.put("portfolioData", getValue(instanceId, "portfolioData"));
		payload.put("processInstanceId", instanceId);
		payload.put("userName", instanceVariables.get("userName"));
		payload.put("type", "rebalanceTest");
		payload.put("id", instanceVariables.get("documentKey"));*/
        payload.put("portfolioData", getValue(instanceId, "portfolioResponse", "data"));
        payload.put("processInstanceId", instanceId);
        payload.put("userName", instanceVariables.get("userName"));
        payload.put("type", "rebalanceTest");
        payload.put("id", instanceVariables.get("documentKey"));
		return couchbaseQueryService.upsert(payload);
	}
	
	private Map<String, Object> addCountryTargetData(String instanceId, Map<String, Object> instanceVariables) {
		//String fields = "countryTargetData = " + JSON(getValue(instanceId, "countryTargetData")).toString();
		String fields = "countryTargetData = " + JSON(getValue(instanceId, "countryTargetResponse", "data")).toString();
		String key = (String) instanceVariables.get("documentKey");
		String retuningFields = "countryTargetData";
		log.info("Calling updateByKey for key {} to set {}", key, fields);
		return couchbaseQueryService.updateByKey(fields, key, retuningFields);
	}

}