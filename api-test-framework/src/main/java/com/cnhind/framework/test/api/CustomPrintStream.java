/**
* @author  Dhwanil Shah
* @version 1.0
* @since   2018-05-18 
*/

package com.cnhind.framework.test.api;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * 
 * Custom PrintStream that logs messages to ExtentReports - used for redirecting RestAssured's
 * Request and Response logging.
 *
 */
class CustomPrintStream extends PrintStream {
	/**
	 * Constructor for CustomPrintStream
	 * @throws UnsupportedEncodingException
	 */
	public CustomPrintStream() throws UnsupportedEncodingException {
		super(new ByteArrayOutputStream(), true, "UTF-8");
	}
	
	/**
	 * Work around to perform auto flush after each message is logged. 
	 */
	public void print(String x) {
		super.print(x);
		
		flush();
	}	

	/**
	 * Override the {@code flush()} method to perform the actual logging via the callback routine passed
	 * in constructor. Reset the log to capture the next log message. Performs logging only if there is
	 * any data to be logged. 
	 */
	public void flush() {
		super.flush();
		
		ByteArrayOutputStream buffer = ((ByteArrayOutputStream)out); 
		
		String logData = new String(buffer.toByteArray(), StandardCharsets.UTF_8);
		logData = logData.trim();
		
		// log only if there is any data to log
		if (logData.isEmpty() == false) {		
			Logger.logRequestResponse(logData);
		}
		
		buffer.reset();
	}	
}
