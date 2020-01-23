package com.cnhind.cp.api.tests;

import org.testng.Assert;
import org.testng.annotations.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.cnhind.framework.test.api.ApiTestBase;
import com.cnhind.framework.test.api.ReportHelper;
import com.cnhind.cp.api.tests.common.ConfigurationSettings;
import com.cnhind.cp.api.tests.common.Constants;
import com.cnhind.cp.api.tests.common.SubscriptionHelper;
import com.cnhind.cp.api.tests.entities.*;

import static com.cnhind.cp.api.tests.common.Constants.*;

public class TelematicsSubscriptionsTest extends ApiTestBase {
	private static final String TELEMATICS_APP_ID = "telematics.telematics";
	
	private static final String TELEMATICS_CONTENT_NAME = "TELEMATICS";
		
	@Test
	public void subscribeTelematics() throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
		ReportHelper.setTestName("CP_2018_Subscribe_01_Positive_Telematics");
		
		Subscription telematicsSubscription = new Subscription(TELEMATICS_CONTENT_NAME, SUBSCRIPTION_SERVICE_LEVEL_BASIC);
		Map<String, String> headers = SubscriptionHelper.getSubscriptionRequestHeaders(TELEMATICS_APP_ID, SUBSCRIPTION_ACTIVATE_STATUS); 
		
		// Request Telematics Basic subscription. Verify status code of 200 and get transctionId
		Boolean status = SubscriptionHelper.testSubscriptionActivation(telematicsSubscription, headers);
		Assert.assertTrue(status, "Telematics subscription failed!");
	}
	
	@Test
	public void subscribeFailsDueToInActiveAsset() {
		Subscription telematicsSubscription = new Subscription(TELEMATICS_CONTENT_NAME, SUBSCRIPTION_SERVICE_LEVEL_BASIC);
		
		String expectedErrorMessage = String.format("Asset with input TDAC- %s is not active.", ConfigurationSettings.getSingleTDAC());
		
        given()
        	.contentType(Constants.JSON_CONTENT_TYPE)
        	.headers(SubscriptionHelper.getSubscriptionRequestHeaders(TELEMATICS_APP_ID, SUBSCRIPTION_ACTIVATE_STATUS))
        	.body(telematicsSubscription)
    	.when()
    		.post(SUBSCRIPTION_API)
    	.then()
    		.statusCode(500)
    		.body("errorMessage", is(notNullValue()))
    		.body("errorMessage", equalToIgnoringCase(expectedErrorMessage));
	}
	
	@Test
	public void subscribeFailsDueToAssetNotExisting() {
		Subscription telematicsSubscription = new Subscription(TELEMATICS_CONTENT_NAME, SUBSCRIPTION_SERVICE_LEVEL_BASIC);
		
		String expectedErrorMessage = String.format("Asset with input TDAC- %s does not exist.", ConfigurationSettings.getSingleTDAC());
		
        given()
        	.contentType(Constants.JSON_CONTENT_TYPE)
        	.headers(SubscriptionHelper.getSubscriptionRequestHeaders(TELEMATICS_APP_ID, SUBSCRIPTION_ACTIVATE_STATUS))
        	.body(telematicsSubscription)
    	.when()
    		.post(SUBSCRIPTION_API)
    	.then()
    		.statusCode(500)
    		.body("errorMessage", is(notNullValue()))
    		.body("errorMessage", equalToIgnoringCase(expectedErrorMessage));
	}
}
