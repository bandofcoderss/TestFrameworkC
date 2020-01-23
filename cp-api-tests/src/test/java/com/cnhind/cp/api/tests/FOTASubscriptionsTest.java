package com.cnhind.cp.api.tests;

import org.hamcrest.core.IsEqual;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.cnhind.cp.api.tests.common.Constants;
import com.cnhind.cp.api.tests.common.SubscriptionHelper;
import com.cnhind.cp.api.tests.entities.Subscription;
import com.cnhind.cp.api.tests.entities.SubscriptionOption;
import com.cnhind.framework.test.api.ApiTestBase;
import com.cnhind.framework.test.api.ExcelReader;
import com.cnhind.framework.test.api.ReportHelper;

import static com.cnhind.cp.api.tests.common.Constants.*;
import static io.restassured.RestAssured.given;
import static com.cnhind.framework.matchers.StringContainsIgnoringCase.containsStringIgnoringCase;
import static org.hamcrest.Matchers.*;

import java.util.Map;

public class FOTASubscriptionsTest extends ApiTestBase {
	private static final String FOTA_APP_ID = "ras.ras,framework.slurp-server";
	
	public static final String RST_CONTENT_NAME = "RST";
	
	public static final String FOTA_OPTION_NAME = "FOTA";
	
	@Test
	public void subscribeFOTA() throws InterruptedException {
		ReportHelper.setTestName("CP_2018_FOTA_POST_01_Positive_Subscribe");
		
		// build the JSON body for FOTA subscription
		Subscription fotaSubscription = new Subscription(RST_CONTENT_NAME, SUBSCRIPTION_SERVICE_LEVEL_BASIC);
		SubscriptionOption fotaOption = new SubscriptionOption(FOTA_OPTION_NAME, SUBSCRIPTION_SERVICE_LEVEL_BASIC);
		fotaSubscription.content.options.add(fotaOption);
		
		// build request headers for FOTA subscription
		Map<String, String> headers = SubscriptionHelper.getSubscriptionRequestHeaders(FOTA_APP_ID, SUBSCRIPTION_ACTIVATE_STATUS);
		
		boolean status = SubscriptionHelper.testSubscriptionActivation(fotaSubscription, headers); 
		Assert.assertTrue(status, "FOTA subscription failed!");
		
		// TODO: Add validation for Redbend vehicle creation
	}
	
	@DataProvider(name = "ExpectErrorDescription")
	public String[][] expectErrorDescriptionTestsDataSource() throws Exception {
		return ExcelReader.getExcelData("FOTAMiscTests.xlsx", "ExpectErrorDescription");
	}
	
	@Test(dataProvider = "ExpectErrorDescription")
	public void expectErrorDescriptionTests(String testId, String testDescription, String tdac, String vin, 
			String expectedStatusCode, String expectedErrorMessage) throws InterruptedException {
		ReportHelper.setTestName(testId);
		ReportHelper.setTestDescription(testDescription);
		
		// build the JSON body for FOTA subscription
		Subscription fotaSubscription = new Subscription(RST_CONTENT_NAME, SUBSCRIPTION_SERVICE_LEVEL_BASIC);
		SubscriptionOption fotaOption = new SubscriptionOption(FOTA_OPTION_NAME, SUBSCRIPTION_SERVICE_LEVEL_BASIC);
		fotaSubscription.content.options.add(fotaOption);
		
		// build request headers for FOTA subscription
		Map<String, String> headers = SubscriptionHelper.getSubscriptionRequestHeaders(FOTA_APP_ID, SUBSCRIPTION_ACTIVATE_STATUS, 
				tdac, vin);
		
		int expectedStatusCodeInt = Integer.parseInt(expectedStatusCode);
		
        given()
    		.contentType(Constants.JSON_CONTENT_TYPE)
    		.headers(headers)
    		.body(fotaSubscription)
		.when()
			.post(SUBSCRIPTION_API)
		.then()
			.statusCode(expectedStatusCodeInt)
			.body(TRANSACTION_ID, is(nullValue()))		// validate that transaction-id is not received
			.body("errors", is(notNullValue()))			// validate that a list of "errors" is received
			.body("errors.size()", is(1))				// validate that the list of errors has only one item
			.body("errors[0].description", containsStringIgnoringCase(expectedErrorMessage));	// validate that error message is as expected
	} 
	
	@DataProvider(name = "ExpectErrorMessage")
	public String[][] expectErrorMessageTestsDataSource() throws Exception {
		return ExcelReader.getExcelData("FOTAMiscTests.xlsx", "ExpectErrorMessage");
	}
	
	@Test(dataProvider = "ExpectErrorMessage")
	public void expectErrorMessageTests(String testId, String testDescription, String tdac, String vin, 
			String expectedStatusCode, String expectedErrorMessage) throws InterruptedException {
		ReportHelper.setTestName(testId);
		ReportHelper.setTestDescription(testDescription);
		
		// build the JSON body for FOTA subscription
		Subscription fotaSubscription = new Subscription(RST_CONTENT_NAME, SUBSCRIPTION_SERVICE_LEVEL_BASIC);
		SubscriptionOption fotaOption = new SubscriptionOption(FOTA_OPTION_NAME, SUBSCRIPTION_SERVICE_LEVEL_BASIC);
		fotaSubscription.content.options.add(fotaOption);
		
		// build request headers for FOTA subscription
		Map<String, String> headers = SubscriptionHelper.getSubscriptionRequestHeaders(FOTA_APP_ID, SUBSCRIPTION_ACTIVATE_STATUS, 
				tdac, vin);
		
		int expectedStatusCodeInt = Integer.parseInt(expectedStatusCode);
		
        given()
    		.contentType(Constants.JSON_CONTENT_TYPE)
    		.headers(headers)
    		.body(fotaSubscription)
		.when()
			.post(SUBSCRIPTION_API)
		.then()
			.statusCode(expectedStatusCodeInt)
			.body(TRANSACTION_ID, is(nullValue()))			// validate that transaction-id is not received
			.body("errorMessage", is(notNullValue()))		// validate that errorMessage is received
			.body("errorMessage", containsStringIgnoringCase(expectedErrorMessage));	// validate that error message is as expected
	} 
}
