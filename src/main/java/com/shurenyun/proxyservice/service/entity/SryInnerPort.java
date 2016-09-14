package com.shurenyun.proxyservice.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shurenyun.proxyservice.domain.EQApp;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SryInnerPort {
	
	int port;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.port == ((SryInnerPort)obj).getPort());
	}
	

}
