package com.shurenyun.proxyservice.entity;

import java.util.List;

public class ServiceCompose {
	
	private String name;
	private String image;
	private List<String> links;
	private List<String> ports;
	private List<String> volumes;
	private List<String> env;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public List<String> getLinks() {
		return links;
	}
	public void setLinks(List<String> links) {
		this.links = links;
	}
	public List<String> getPorts() {
		return ports;
	}
	public void setPorts(List<String> ports) {
		this.ports = ports;
	}
	public List<String> getVolumes() {
		return volumes;
	}
	public void setVolumes(List<String> volumes) {
		this.volumes = volumes;
	}
	public List<String> getEnv() {
		return env;
	}
	public void setEnv(List<String> env) {
		this.env = env;
	}
}
