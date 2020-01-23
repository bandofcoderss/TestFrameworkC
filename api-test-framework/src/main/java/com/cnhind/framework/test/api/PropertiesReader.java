/**
* @author  Dhwanil Shah
* @version 1.0
* @since   2018-05-18 
*/

package com.cnhind.framework.test.api;

import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * Provides easy access to contents of *.properties files placed under "src/test/resources" folder
 *
 */
public class PropertiesReader {
	private String propertiesFileName;
	
	private boolean wasLoadSuccessful;
	private String loadErrorMessage;
	
	private Properties props;
	
	/**
	 * Constructor for PropertiesReader
	 * @param propsFileName Name of the *.properties file that needs to be loaded
	 */
	public PropertiesReader(String propsFileName) {
		propertiesFileName = propsFileName;

		props = new Properties();
		loadErrorMessage = loadPropertiesFile(propertiesFileName, props);
		
		wasLoadSuccessful = loadErrorMessage == null ? true : false; 
	}
	
	/**
	 * Loads the *.properties file
	 * @param propertyFile Name of the *.properties file that needs to be loaded
	 * @param targetProperties Target {@code Properties} object that would be populated with the contents of *.properties file
	 * @return Returns null if there are no exceptions while loading the *.properties file, else returns the exception details
	 */
	private String loadPropertiesFile(String propertyFile, Properties targetProperties) {
		String returnValue = null;

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try (InputStream resourceStream = loader.getResourceAsStream(propertyFile)) {
			targetProperties.load(resourceStream);
		} catch (Exception e) {
			returnValue = e.getMessage();
		}
			
		return returnValue;
	}

	/**
	 * Returns the {@code Properties} object that was populated with *.properties file contents
	 * @return
	 */
	public Properties getProperties() {
		return props;
	}
	
	/**
	 * Returns a flag specifying if loading a *.properties file was successful or not
	 * @return
	 */
	public boolean getWasLoadSuccessful() {
		return wasLoadSuccessful;
	}
	
	/**
	 * Returns value of property if it exists in *.properties file that was loaded else returns null
	 * @param propertyName
	 * @return
	 */
	public String getPropertyValue(String propertyName) {
		if (wasLoadSuccessful == false) return null;
		
		return getPropertyValue(propertyName, null);
	}
	
	/**
	 * Returns value of property if it exists in *.properties file that was loaded else returns the defaultValue
	 * @param propertyName
	 * @param defaultValue
	 * @return
	 */
	public String getPropertyValue(String propertyName, String defaultValue) {
		if (wasLoadSuccessful == false) return defaultValue;
		
		String returnValue = props.getProperty(propertyName, defaultValue);
		if (returnValue != null) {
			returnValue = returnValue.trim();
		}
		
		return returnValue;
	}
}
