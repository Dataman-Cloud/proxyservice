package com.shurenyun.proxyservice.controller.vo;

public class AddStackResponse {
	
	String status;
	String stack_id;
	String cluster_id;
	String error_message;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStack_id() {
		return stack_id;
	}
	public void setStack_id(String stack_id) {
		this.stack_id = stack_id;
	}
	public String getCluster_id() {
		return cluster_id;
	}
	public void setCluster_id(String cluster_id) {
		this.cluster_id = cluster_id;
	}
	public String getError_message() {
		return error_message;
	}
	public void setError_message(String error_message) {
		this.error_message = error_message;
	}
	
	

}
