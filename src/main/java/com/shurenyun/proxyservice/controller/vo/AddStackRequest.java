package com.shurenyun.proxyservice.controller.vo;

import java.util.List;

import com.shurenyun.proxyservice.entity.EQImage;

public class AddStackRequest {

	String svn_url;
	String stack_name;
	List<EQImage> images;
	
	public String getSvn_url() {
		return svn_url;
	}
	public void setSvn_url(String svn_url) {
		this.svn_url = svn_url;
	}
	public String getStack_name() {
		return stack_name;
	}
	public void setStack_name(String stack_name) {
		this.stack_name = stack_name;
	}
	public List<EQImage> getImages() {
		return images;
	}
	public void setImages(List<EQImage> images) {
		this.images = images;
	}
	
	
	
}
