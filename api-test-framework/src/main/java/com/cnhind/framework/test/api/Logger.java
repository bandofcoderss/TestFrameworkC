/**
* @author  Dhwanil Shah
* @version 1.0
* @since   2018-05-18 
*/

package com.cnhind.framework.test.api;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

/**
 * 
 * Provides routines to log to ExtentReports
 *
 */
public class Logger {
	/**
	 * Logs a DEBUG message for current test
	 * @param details DEBUG message to log
	 */
	public static void logDebug(String details) {
		log(Status.DEBUG, details);
	}
	
	/**
	 * Logs a INFO message for current test
	 * @param details INFO message to log
	 */
	public static void logInfo(String details) {
		log(Status.INFO, details);
	}
	
	/**
	 * Logs a DEBUG message for current test in a CODE block
	 * @param details
	 */
	protected static void logRequestResponse(String details) {
		System.out.println(details);
		
		Markup m = MarkupHelper.createCodeBlock(details);
		ReportHelper.getCurrentTest().debug(m);
	}

	/**
	 * Logs a message for current test as per user specified log status
	 * @param status Log status (DEBUG, INFO, WARN etc.)
	 * @param details Message to log
	 */
	public static void log(Status status, String details) {
		System.out.println(details);
		
		ReportHelper.getCurrentTest().log(status, details);
	}
}
