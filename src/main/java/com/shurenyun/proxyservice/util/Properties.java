package com.shurenyun.proxyservice.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "shurenyun")
public class Properties {
	private String api;
	private String swarmmgt;
	
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public String getSwarmmgt() {
		return swarmmgt;
	}
	public void setSwarmmgt(String swarmmgt) {
		this.swarmmgt = swarmmgt;
	}
	
	
	
}
