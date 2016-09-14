package com.shurenyun.proxyservice.domain;

import java.util.Map;

public class DockerCompose {
	
	private Map<String,ServiceCompose> serviceCompose;

	public Map<String, ServiceCompose> getServiceCompose() {
		return serviceCompose;
	}

	public void setServiceCompose(Map<String, ServiceCompose> serviceCompose) {
		this.serviceCompose = serviceCompose;
	}
	
	

}
