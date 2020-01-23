package com.cnhind.cp.api.tests.entities;

import com.cnhind.cp.api.tests.common.ConfigurationSettings;

public class SubscriptionOption {
	private String name;
	private String service_level;
	private String activation_date;
	private String expiration_date;
	
	public SubscriptionOption() {
		
	}
	
	public SubscriptionOption(String optionName, String levelOfService) {
		name = optionName;
		service_level = levelOfService;
		
		activation_date = ConfigurationSettings.getSubscritpionActivationDate();
		expiration_date = ConfigurationSettings.getSubscriptionExpirationDate();
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

	public void setService_level(String service_level) {
		this.service_level = service_level;
	}

	public String getActivation_date() {
		return activation_date;
	}

	public void setActivation_date(String activation_date) {
		this.activation_date = activation_date;
	}

	public String getExpiration_date() {
		return expiration_date;
	}

	public void setExpiration_date(String expiration_date) {
		this.expiration_date = expiration_date;
	}
}
