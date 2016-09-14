package com.shurenyun.proxyservice.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "shurenyun")
public class ServiceProperties {
	private String name;
	private String password;
	private String api;
	private String swarmmgt;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
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
