package com.camunda.couchbase.rs;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camunda.couchbase.model.AppResponse;
import com.camunda.couchbase.model.request.CountryTarget;

@RequestMapping("/api/country/target")
public interface CountryTargetRestService {

	@PostMapping
	public AppResponse getByKey(@RequestBody CountryTarget countryTarget);
	
	@PostMapping("/all")
	public AppResponse getAll();
	
	@PostMapping("/portfolio")
	public AppResponse getByPortfolio(@RequestBody CountryTarget countryTarget);
	
	@PostMapping("/pmgroup")
	public AppResponse getByPmGroup(@RequestBody CountryTarget countryTarget);
	
	@PostMapping("/portfolio/pmgroup")
	public AppResponse getByPortfolioAndPmGroup(@RequestBody CountryTarget countryTarget);

}
