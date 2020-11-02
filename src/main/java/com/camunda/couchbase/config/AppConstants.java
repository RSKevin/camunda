package com.camunda.couchbase.config;

public class AppConstants {

	public static final Integer INTERNAL_SERVER_ERROR_CODE = 500;
	public static final String INTERNAL_SERVER_ERROR = "Internal server error!";

	public static final Integer BAD_REQUEST_ERROR_CODE = 400;
	public static final String BAD_REQUEST_MESSAGE = "Bad request. Required fields are not present in request!";

	public static final Integer SUCCESS_CODE = 200;
	public static final String SUCCESS_MESSAGE = "Request processed successfully!";
	
	public static final Integer NO_CONTENT_CODE = 204;
	public static final String NO_CONTENT_MESSAGE = "No content to display!";

	public static final Integer INVALID_INPUT_ERROR_CODE = 406;
	public static final String INVALID_INPUT_ERROR = "Invalid Input!";

}