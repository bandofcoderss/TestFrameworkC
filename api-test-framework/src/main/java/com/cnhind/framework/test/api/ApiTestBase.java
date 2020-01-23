/**
* @author  Dhwanil Shah
* @version 1.0
* @since   2018-05-18 
*/

package com.cnhind.framework.test.api;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.Status;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

/**
 * 
 * Base class for all API Tests. Provides easy setup of proxy, base uri, 
 * logging of requests and responses etc. It also sets up the ExtentReport logging 
 * and provides routines for logging to the same from actual tests 
 */
public class ApiTestBase {
	private static final String FRAMEWORK_SETTINGS_FILE = "api.framework.settings";
	private static Boolean isFrameworkConfigured = false;
	
	/**
	 * Sets up ExtetnReports and applies API Framework configuration after reading the same from
	 * api.framework.settings file. Actual test projects should place the file in their 
	 * "src/test/resources" directories.
	 * 
	 * TestNG honors the {@literal @BeforeSuite} attribute and calls the method before running any {@literal @Test} methods
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	@BeforeSuite
	public void setup() throws UnsupportedEncodingException, FileNotFoundException {
		if (isFrameworkConfigured == false) {
			frameworkLog(Status.INFO, "Start framework initialization");
	
			PropertiesReader frameworkConfigurationReader = new PropertiesReader(FRAMEWORK_SETTINGS_FILE);
			if (frameworkConfigurationReader.getWasLoadSuccessful() == true) {
				ConfigureProxy(frameworkConfigurationReader);
				
				ConfigureBaseUri(frameworkConfigurationReader);
				
				ConfigureRequestResposeLogging(frameworkConfigurationReader);
			} else {
				frameworkLog(Status.INFO, "api.framework.settings file was not found, assuming no configuration is needed");
			}

			isFrameworkConfigured = true;
			
			frameworkLog(Status.INFO, "End framework initialization");
		}
	}
	
	/**
	 * Starts a new ExtentTest through the ReportHelper class. By default the test is named
	 * based on the name of the {@literal @Test} method. Users can change that name to match their
	 * actual test cases by calling {@code reportHelper.setTestName} method
	 * 
	 * TestNG honors the {@literal @BeforeMethod} attribute and calls this method before running 
	 * {@literal @Test} annotated method. 
	 * @param testMethod TestNG injects the {@literal @Test} method that is going to be executed next 
	 */
	@BeforeMethod()
	public void startHtmlReportForTest(Method testMethod) {
		ReportHelper.startTest(testMethod.getName());
	}
	
	/**
	 * Ends the ExtentTest that was started earlier. Records the final status of the test execution.
	 * 
	 * TestNG honors the {@literal @AfterMethod} attribute and calls this method after running 
	 * {@literal @Test} annotated method.
	 * @param result TestNG injects the actual result of the test that was executed
	 */
	@AfterMethod()
	public void endHtmlReportForTest(ITestResult result) {
		if (result.getStatus() == ITestResult.SUCCESS) {
			ReportHelper.endTestAsPassed("Test passed");
		} else if (result.getStatus() == ITestResult.SKIP) {
			ReportHelper.endTestAsSkipped("Test skipped due to: " + result.getThrowable());
		} else if (result.getStatus() == ITestResult.FAILURE) {
			ReportHelper.endTestAsFailed("Test failed due to: " + result.getThrowable());
		}
	}

	/**
	 * Flushes the ExtentReports data from memory to file system
	 * 
	 * TestNG honors the {@literal @AfterSuite} attribute and calls this method after running 
	 * all {@literal @Test} annotated methods.
	 */
	@AfterSuite()
	public void afterEndOfAllTests() {
		ReportHelper.flush();
	}
	
	/**
	 * Logs CNHi API Framework specific messages
	 * @param status Log status (DEBUG, INFO, WARN etc.)
	 * @param details Message to log
	 */
	private void frameworkLog(Status status, String details) {
		ReportHelper.frameworkLog.log(status, details);
	}
	
	/**
	 * Configures RestAssured proxy based on settings from api.framework.settings file
	 * @param frameworkConfigurationReader Reader for api.framework.settings file
	 */
	private void ConfigureProxy(PropertiesReader frameworkConfigurationReader) {
		String useProxySetting = frameworkConfigurationReader.getPropertyValue("UseProxy", "false");
		boolean useProxy = Boolean.parseBoolean(useProxySetting);
		
		if (useProxy == true) {
			String proxyIP = frameworkConfigurationReader.getPropertyValue("ProxyIP");
			String proxyPort = frameworkConfigurationReader.getPropertyValue("ProxyPort");
			RestAssured.proxy(proxyIP, Integer.valueOf(proxyPort));
			
			frameworkLog(Status.INFO, String.format("Using proxy: %s:%s", proxyIP, proxyPort));
		} else {
			frameworkLog(Status.INFO, "Direct connection specified, no proxy used in framework");
		}
	}
	
	/**
	 * Configures RestAssured BaseURI as per settings from api.framework.settings file
	 * @param frameworkConfigurationReader Reader for api.framework.settings file
	 */
	private void ConfigureBaseUri(PropertiesReader frameworkConfigurationReader) {
		String baseURI = frameworkConfigurationReader.getPropertyValue("BaseURI");
		if (baseURI != null && baseURI.isEmpty() == false) {
			RestAssured.baseURI = baseURI;
			
			frameworkLog(Status.INFO, String.format("Using BaseURI: %s", baseURI));
		} else {
			frameworkLog(Status.INFO, "No BaseURI specified, tests expected to use fully qualified URIs");
		}
	}
	
	/**
	 * Configures RestAssured logging of requests and responses based based on setting from api.framework.settings file
	 * @param frameworkConfigurationReader Reader for api.framework.settings file
	 */
	private void ConfigureRequestResposeLogging(PropertiesReader frameworkConfigurationReader) throws UnsupportedEncodingException, FileNotFoundException {
		String requestResponseLoggingFlag = frameworkConfigurationReader.getPropertyValue("RequestResponseLogging");
		if (requestResponseLoggingFlag == null || requestResponseLoggingFlag.isEmpty() == true) {
			requestResponseLoggingFlag = "NONE";
		} 

		// Create the custom print stream that can use custom logic to redirect RestAssured
		// logging for requests and responses
		CustomPrintStream extentReportLoggingStream = new CustomPrintStream();

		if (requestResponseLoggingFlag.equalsIgnoreCase("ALL")) {
			// configure RestAssured to log all requests and responses. Any logging done by RestAssured 
			// will be directed to ExtentReports through the custom stream
			RestAssured.replaceFiltersWith(
					ResponseLoggingFilter.logResponseTo(extentReportLoggingStream), 
					RequestLoggingFilter.logRequestTo(extentReportLoggingStream));
			
			frameworkLog(Status.INFO, "Logging all requests and responses");
		
		} else if (requestResponseLoggingFlag.equalsIgnoreCase("IF-VALIDATION-FAILS")) {
			// Configure RestAssured to log requests / responses if there are any validation failures.
			// Any logging done by RestAssured will be directed to ExtentReports through the 
			// custom stream
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

			// redirect request response logging that occurs when validation fails to ExtentReports
			RestAssured.config = RestAssured.config.logConfig(
					RestAssured.config.getLogConfig().defaultStream(extentReportLoggingStream));
			
			frameworkLog(Status.INFO, "Logging requests and responses that fail validation");
			
		} else { 
			// Consider as "NONE". As no filters have been added no request / response logging 
			// will occur. 

			// redirect request response logging done through ".log()" to ExtentReports
			RestAssured.config = RestAssured.config.logConfig(
					RestAssured.config.getLogConfig().defaultStream(extentReportLoggingStream));
			
			frameworkLog(Status.INFO, "No logging specified, only requests and responses that call '.log()' methods would be logged");
		}
	}
}
