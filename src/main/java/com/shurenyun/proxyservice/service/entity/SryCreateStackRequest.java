package com.shurenyun.proxyservice.service.entity;

public class SryCreateStackRequest {
	
	String name;
	String compose;
	String marathonConfig;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompose() {
		return compose;
	}
	public void setCompose(String compose) {
		this.compose = compose;
	}
	public String getMarathonConfig() {
		return marathonConfig;
	}
	public void setMarathonConfig(String marathonConfig) {
		this.marathonConfig = marathonConfig;
	}
	
}
