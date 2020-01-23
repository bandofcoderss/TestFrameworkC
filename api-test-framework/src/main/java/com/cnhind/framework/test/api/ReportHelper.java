/**
* @author  Dhwanil Shah
* @version 1.0
* @since   2018-05-18 
*/

package com.cnhind.framework.test.api;

import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;  

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Test;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

/**
 * 
 * Provides routines for creating ExtentReport, creating ExtentTest, recording
 * results and logs from each test
 *
 */
public class ReportHelper {
	private static ExtentReports extent;
	private static ExtentHtmlReporter htmlReporter;
	protected static ExtentTest frameworkLog;
	
	/**
	 * Static constructor for ReportHelper - initializes ExtentReports to create a new report under
	 * "{user.dir}/reports/". The name of the file would be based on current date time.
	 */
	static {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss");  
		LocalDateTime now = LocalDateTime.now();  
		String newFileName = System.getProperty("user.dir") + "/reports/" + dtf.format(now) + ".html";
		
		htmlReporter = new ExtentHtmlReporter(newFileName);
		extent = new ExtentReports();
		
		extent.attachReporter(htmlReporter);
		
		frameworkLog = extent.createTest("CNHi API Framework logs");
	}
	
	private static ExtentTest currentTest;
	
	
	/**
	 * Starts a new ExtentTest 
	 * @param testName Name of the new test to be started
	 */
	public static void startTest(String testName) {
		currentTest = extent.createTest(testName);
		currentTest.log(Status.INFO, "Test started");
	}
	
	/**
	 * Ends the current ExtentTest with the final status as passed
	 * @param message 
	 */
	public static void endTestAsPassed(String message) {
		currentTest.pass(message);
	}

	/**
	 * Ends the current ExtentTest with the final status as failed
	 * @param message
	 */
	public static void endTestAsFailed(String message) {
		currentTest.fail(message);
	}

	/**
	 * Ends the current ExtentTest with the final status as skipped
	 * @param message
	 */
	public static void endTestAsSkipped(String message) {
		currentTest.skip(message);
	}
	
	/**
	 * Sets the name current ExtentTest
	 * @param testName
	 */
	public static void setTestName(String testName) {
		Test testModel = currentTest.getModel();
		testModel.setName(testName);
	}
	
	/** 
	 * Sets the description for current ExtentTest
	 * @param testDescription
	 */
	public static void setTestDescription(String testDescription) {
		Test testModel = currentTest.getModel();
		testModel.setDescription(testDescription);
	}
	
	/**
	 * Returns the current ExtentTest instance
	 * @return
	 */
	public static ExtentTest getCurrentTest() {
		return currentTest;
	}
	
	/**
	 * Flush the in memory ExtentReport contents to file system
	 */
	protected static void flush() {
		extent.flush();
	}
}
