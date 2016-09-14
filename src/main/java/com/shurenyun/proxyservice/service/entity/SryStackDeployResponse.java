package com.shurenyun.proxyservice.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SryStackDeployResponse {
	int code;
	SryStackDeployData data;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public SryStackDeployData getData() {
		return data;
	}
	public void setData(SryStackDeployData data) {
		this.data = data;
	}
	
	
	
}
