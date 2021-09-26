package com.samknows.metric.be.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "camel")
public class CamelConfig {
	
	private String metricLoadEndpoint;
	private String metricStorageEndpoint;

	public String getMetricLoadEndpoint() {
		return metricLoadEndpoint;
	}

	public void setMetricLoadEndpoint(String metricLoadEndpoint) {
		this.metricLoadEndpoint = metricLoadEndpoint;
	}

	public String getMetricStorageEndpoint() {
		return metricStorageEndpoint;
	}

	public void setMetricStorageEndpoint(String metricStorageEndpoint) {
		this.metricStorageEndpoint = metricStorageEndpoint;
	}

}
