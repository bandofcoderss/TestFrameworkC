package com.cnhind.cp.api.tests.entities;

import java.util.ArrayList;

import com.cnhind.cp.api.tests.common.ConfigurationSettings;

public class SubscriptionContent {
	private String name;
	private String service_level;
	private String activation_date;
	private String expiration_date;
	
	public ArrayList<SubscriptionOption> options;

	public SubscriptionContent() {
	}
	
	public SubscriptionContent(String contentName, String serviceLevel) {
		name = contentName;
		service_level = serviceLevel;
		
		activation_date = ConfigurationSettings.getSubscritpionActivationDate();
		expiration_date = ConfigurationSettings.getSubscriptionExpirationDate();
		
		options = new ArrayList<SubscriptionOption>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getService_level() {
		return service_level;
	}

	public void setServiceLevel(String service_level) {
		this.service_level = service_level;
	}

	public String getActivationDate() {
		return activation_date;
	}

	public void setActivationDate(String activation_date) {
		this.activation_date = activation_date;
	}

	public String getExpirationDate() {
		return expiration_date;
	}

	public void setExpirationDate(String expiration_date) {
		this.expiration_date = expiration_date;
	}
}
