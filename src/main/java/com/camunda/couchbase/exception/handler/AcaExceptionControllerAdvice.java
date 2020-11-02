package com.camunda.couchbase.exception.handler;

import com.camunda.couchbase.config.AppConstants;
import com.camunda.couchbase.model.AppResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AcaExceptionControllerAdvice {

	private final Logger log = LoggerFactory.getLogger(AcaExceptionControllerAdvice.class);

	@ExceptionHandler(Exception.class)
	public AppResponse appException(Exception ex) {
		log.info("Exception {}", ex.getMessage());
		AppResponse appResponse = new AppResponse();
		appResponse.setResponseCode(AppConstants.INTERNAL_SERVER_ERROR_CODE);
		appResponse.setData(AppConstants.INTERNAL_SERVER_ERROR);
		appResponse.setErrorFlag(true);
		return appResponse;
	}

}
