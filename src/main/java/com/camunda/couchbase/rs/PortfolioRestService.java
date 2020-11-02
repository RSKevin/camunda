package com.camunda.couchbase.rs;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camunda.couchbase.model.AppResponse;

@RequestMapping("/api/portfolio")
public interface PortfolioRestService {

	@PostMapping
	public AppResponse getByKey(@RequestBody Map<String, Object> payload);
	
	@PostMapping("/summary")
	public AppResponse getAll();

}
