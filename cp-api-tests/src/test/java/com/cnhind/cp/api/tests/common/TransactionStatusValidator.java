package com.cnhind.cp.api.tests.common;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import com.cnhind.framework.test.api.Logger;

public class TransactionStatusValidator {
	public boolean Validate(String statusCheckURL, String transactionId, 
			String expectedStatusSuccessMessage, String expectedStatusFailureMessage, boolean expectSuccess,   
			int maxTryCount, int delayBetweenTriesMS) throws InterruptedException {
		
		// TODO: Add logging
		
		boolean validationStatus = false;
		
		for (int counter = 0; counter < maxTryCount; counter++) {
			Thread.sleep(delayBetweenTriesMS);
			
			Logger.logInfo(String.format("Checking transaction status: %d / %d after delay of %dms", 
					counter + 1, maxTryCount, delayBetweenTriesMS));
			
			String status = 
			given()
				.headers("sessionId", ConfigurationSettings.getSessionId())
				.headers("requestId", ConfigurationSettings.getRequestId())
				.accept(Constants.JSON_CONTENT_TYPE)
				.pathParam(Constants.TRANSACTION_ID, transactionId)
			.when()
				.get(statusCheckURL)
			.then()
				.statusCode(200)
				.extract().path("status");
		
			if (containsString(expectedStatusSuccessMessage).matches(status)) {
				validationStatus = true;
				break;
			}
			
			// TODO: Add expected error message related logic
		}
		
		return validationStatus;
	}
}
