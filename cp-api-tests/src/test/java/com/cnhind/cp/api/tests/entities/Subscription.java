package com.cnhind.cp.api.tests.entities;

public class Subscription {
	private String schema_name;
	private String schema_version;
	public SubscriptionContent content;

	public Subscription() {
	}
	
	public Subscription(String contentName, String serviceLevel) {
		schema_name = "SUBSCRIPTION";
		schema_version = "1.0";

		content = new SubscriptionContent(contentName, serviceLevel);
	}

	public String getSchemaName() {
		return schema_name;
	}

	public void setSchemaName(String schema_name) {
		this.schema_name = schema_name;
	}

	public String getSchemaVersion() {
		return schema_version;
	}

	public void setSchemaVersion(String schema_version) {
		this.schema_version = schema_version;
	}
}

