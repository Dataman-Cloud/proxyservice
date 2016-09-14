package com.shurenyun.proxyservice.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SrySearchStackData {
	
	Object stack;
	String namespace;
	
	public Object getStack() {
		return stack;
	}
	public void setStack(Object stack) {
		this.stack = stack;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	
	
}
