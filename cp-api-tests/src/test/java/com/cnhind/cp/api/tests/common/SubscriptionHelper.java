package com.cnhind.cp.api.tests.common;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

import com.cnhind.cp.api.tests.entities.Subscription;
import com.cnhind.framework.test.api.Logger;

import static com.cnhind.cp.api.tests.common.Constants.*;

public class SubscriptionHelper {
	public static boolean testSubscriptionActivation(Subscription subscriptionDetails, 
			Map<String, String> subscriptionRequestHeaders) throws InterruptedException {
		
		// Request subscription. Verify status code of 200 and get transctionId
		String transactionId = 
        given()
        	.contentType(JSON_CONTENT_TYPE)
        	.headers(subscriptionRequestHeaders)
        	.body(subscriptionDetails)
    	.when()
    		.post(SUBSCRIPTION_API)
    	.then()
    		.statusCode(200)
    		.body(TRANSACTION_ID, is(notNullValue()))
    		.extract().path(TRANSACTION_ID);
		
		Logger.logInfo(String.format("Received transactionId: %s", transactionId));
		
		Logger.logInfo("Validating transaction status");
		
		// Get status of subscription request till "Success – Subscription Activated" is observed
		TransactionStatusValidator validator = new TransactionStatusValidator();
		boolean status = validator.Validate(SUBSCRIPTION_STATUS_API, transactionId, 
				"Success - Subscription Activated", "", true, 10, 1000);
		return status;
	}
	
	public static Map<String, String> getSubscriptionRequestHeaders(String appId, String status) {
		return getSubscriptionRequestHeaders(appId, status, 
				ConfigurationSettings.getSingleTDAC(), ConfigurationSettings.getSingleVIN());
	}
	
	public static Map<String, String> getSubscriptionRequestHeaders(String appId, String status, String tdac, String vin) {
		Map<String, String> returnValue = new HashMap<String, String>();

		returnValue.put("sessionID", ConfigurationSettings.getSessionId());
		returnValue.put("requestID", ConfigurationSettings.getRequestId());
		returnValue.put("appID", appId);
		returnValue.put("status", status);
		returnValue.put("tdac", tdac);
		returnValue.put("vin", vin);
		
		return returnValue;
	}
}
