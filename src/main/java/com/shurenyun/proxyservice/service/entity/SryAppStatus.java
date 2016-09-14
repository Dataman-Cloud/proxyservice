package com.shurenyun.proxyservice.service.entity;

public enum SryAppStatus {
	DEPLOY(1,"deploying"),
	RUNING(2,"running"),
	STOP(3,"stopped"),
	DELETE(4,"deleted"),
	SCALE(5,"scale out"),
	START(6,"start"),
	UNDO(7,"undo");
	
	private String name;
	private long id;
	
	SryAppStatus(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public static String fromStatusId(long id) {
		for(SryAppStatus sryAppStatus: SryAppStatus.values()) {
			if(sryAppStatus.id == id) {
				return sryAppStatus.name;
			}
		}
		throw new IllegalArgumentException();
	}
	
}
