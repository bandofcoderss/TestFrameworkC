package com.cnhind.cp.api.tests.common;

public class Constants {
	public static final String JSON_CONTENT_TYPE = "application/json";
	
	public static final String TRANSACTION_ID = "transactionId";
	
	public static final String SUBSCRIPTION_API = "/connectivity/b2c/api/v1/subscriptions"; 
	public static final String SUBSCRIPTION_STATUS_API = "/connectivity/b2c/api/v1/subscriptions/{transactionId}"; 

	public static final String SUBSCRIPTION_ACTIVATE_STATUS = "A";
	public static final String SUBSCRIPTION_DEACTIVATE_STATUS = "D";
	
	public static final String SUBSCRIPTION_SERVICE_LEVEL_BASIC = "BASIC";
}