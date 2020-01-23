package com.cnhind.cp.api.tests.common;

import com.cnhind.framework.test.api.PropertiesReader;

public class ConfigurationSettings {
	private static PropertiesReader reader = new PropertiesReader("cp.tests.settings");
	
	public static boolean getUseProxy() {
		return Boolean.parseBoolean(reader.getPropertyValue("UseProxy"));
	}
	
	public static String getProxyIP() {
		return reader.getPropertyValue("ProxyIP");
	}
	
	public static int getProxyPort() {
		return Integer.parseInt(reader.getPropertyValue("ProxyPort"));
	}
	
	public static String getBaseURI() {
		return reader.getPropertyValue("BaseURI");
	}
	
	public static String getSubscritpionActivationDate() {
		return reader.getPropertyValue("SubscriptionActivationDate");
	}
	
	public static String getSubscriptionExpirationDate() {
		return reader.getPropertyValue("SubscriptionExpirationDate");
	}
	
	public static String getSessionId() {
		return reader.getPropertyValue("SessionId");
	}
	
	public static String getRequestId() {
		return reader.getPropertyValue("RequestId");
	}
	
	public static String getSingleTDAC() {
		return reader.getPropertyValue("SingleTDAC");
	}
	
	public static String getSingleVIN() {
		return reader.getPropertyValue("SingleVIN");
	}
}
