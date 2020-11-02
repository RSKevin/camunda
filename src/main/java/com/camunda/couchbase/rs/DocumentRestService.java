package com.camunda.couchbase.rs;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camunda.couchbase.model.AppResponse;

@RequestMapping("/api/document")
public interface DocumentRestService {
	
	@GetMapping("/key/{key}")
	public AppResponse getByDocKey(@PathVariable String key);
	
	@PostMapping("/key")
	public AppResponse getByKey(@RequestBody Map<String, Object> payload);

	@GetMapping("/type/{type}")
	public AppResponse getByType(@PathVariable String type);
	
	@PostMapping("/upsert")
	public AppResponse upsert(@RequestBody Map<String, Object> payload);

	@PostMapping("/insert")
	public AppResponse insert(@RequestBody Map<String, Object> payload);
	
	@PostMapping("/insert/bulk")
	public AppResponse bulkInsert(@RequestBody List<Map<String, Object>> payload);
}
