package com.shurenyun.proxyservice.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SryCreateStackResponse {
	
	int code;
	SryCreateStackData data;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public SryCreateStackData getData() {
		return data;
	}
	public void setData(SryCreateStackData data) {
		this.data = data;
	}
	
	
	

}
