package com.shurenyun.proxyservice.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SryDeployedApplication {
	
	@JsonProperty("Id")
	private int Id;
	
	@JsonProperty("Cid")
	private int Cid;
	
	@JsonProperty("Uid")
	private int Uid;
	
	@JsonProperty("StackId")
	private int StackId;
	
	@JsonProperty("Instances")
	private int Instances;
	
	@JsonProperty("Name")
	private String Name;
	
	@JsonProperty("Dependencies")
	private String Dependencies;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getCid() {
		return Cid;
	}
	public void setCid(int cid) {
		Cid = cid;
	}
	public int getUid() {
		return Uid;
	}
	public void setUid(int uid) {
		Uid = uid;
	}
	public int getStackId() {
		return StackId;
	}
	public void setStackId(int stackId) {
		StackId = stackId;
	}
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
	public String getDependencies() {
		return Dependencies;
	}
	public void setDependencies(String dependencies) {
		Dependencies = dependencies;
	}
	
	

}
