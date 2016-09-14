package com.shurenyun.proxyservice.service.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SryOccupiedPortData {
	
	List<SryInnerPort> innerPorts;
	List<SryOuterPort> outerPorts;
	
	public List<SryInnerPort> getInnerPorts() {
		return innerPorts;
	}
	public void setInnerPorts(List<SryInnerPort> innerPorts) {
		this.innerPorts = innerPorts;
	}
	public List<SryOuterPort> getOuterPorts() {
		return outerPorts;
	}
	public void setOuterPorts(List<SryOuterPort> outerPorts) {
		this.outerPorts = outerPorts;
	}
	

}
