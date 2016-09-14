package com.shurenyun.proxyservice;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ComponentScan
@EnableAutoConfiguration
public class Application {

	// Define the logger object for this class
  	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	} 
}
