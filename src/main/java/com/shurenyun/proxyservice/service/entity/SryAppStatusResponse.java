package com.shurenyun.proxyservice.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SryAppStatusResponse {
	
	int code;
	SryAppStatusData data;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public SryAppStatusData getData() {
		return data;
	}
	public void setData(SryAppStatusData data) {
		this.data = data;
	}
	
		
}
