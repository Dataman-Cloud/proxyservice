package com.shurenyun.proxyservice.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SryApplication{
	
	@JsonProperty("Instances")
	int Instances;
	
	@JsonProperty("Name")
	String Name;
	
	public int getInstances() {
		return Instances;
	}
	public void setInstances(int instances) {
		Instances = instances;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
}
