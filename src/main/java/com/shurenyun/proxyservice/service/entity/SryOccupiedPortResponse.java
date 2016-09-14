package com.shurenyun.proxyservice.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SryOccupiedPortResponse {
	
	int code;
	SryOccupiedPortData data;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public SryOccupiedPortData getData() {
		return data;
	}
	public void setData(SryOccupiedPortData data) {
		this.data = data;
	}
	
	
	

}
