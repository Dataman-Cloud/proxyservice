package com.shurenyun.proxyservice.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetrieveNotOccupiedPort {
	
	// Define the logger object for this class
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ShurenyunApiRequestForward2 shurenyunApiRequestForward2;
	
	//get port typically from 32768 to 61000. Invoke shurenyun's api to decide if occupied.
	public List<Long> getPorts() {
		List<Long> not_occupied_ports = new ArrayList<Long>();
		List<Long> sryOccupiedPortList = shurenyunApiRequestForward2.getOccupedPorts();
		for(int i=32768;i<61000;i++) {
			if(!sryOccupiedPortList.contains(i)) {
				not_occupied_ports.add(Long.valueOf(i));
			}
		}
		return not_occupied_ports;
	}
	
}
