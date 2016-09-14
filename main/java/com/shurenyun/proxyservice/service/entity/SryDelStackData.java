package com.shurenyun.proxyservice.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SryDelStackData{
	
	int stackId;
	//error.
	String message;

	public int getStackId() {
		return stackId;
	}

	public void setStackId(int stackId) {
		this.stackId = stackId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
