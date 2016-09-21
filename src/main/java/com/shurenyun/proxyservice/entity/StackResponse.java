package com.shurenyun.proxyservice.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StackResponse {
	String stack_status;
	String error_message;
	List<ServiceResponse> app_list;
	public String getStack_status() {
		return stack_status;
	}
	public void setStack_status(String stack_status) {
		this.stack_status = stack_status;
	}
	public String getError_message() {
		return error_message;
	}
	public void setError_message(String error_message) {
		this.error_message = error_message;
	}
	public List<ServiceResponse> getApp_list() {
		return app_list;
	}
	public void setApp_list(List<ServiceResponse> app_list) {
		this.app_list = app_list;
	}
}
