package com.shurenyun.proxyservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shurenyun.proxyservice.service.entity.SryOccupiedPortResponse;
import com.shurenyun.proxyservice.util.ServiceProperties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Service
public class ShurenyunApiRequestForward2 {
	
	// Define the logger object for this class
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	  	
	@Autowired
	private ServiceProperties configuration;
	
	/**
	 * create stack.
	 * @param token
	 * @param cluster_id
	 * @param stack_name
	 * @param dockercompose
	 * @param shurenyuncompose
	 */
	public String createStack(String stack_name,String dab){

		RestTemplate createStackRestTemplate = new RestTemplate();
		createStackRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		createStackRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        
		String uri = new String(this.configuration.getApi()+"/stacks");
		
		HttpHeaders requestHeaders = new HttpHeaders();
		
//		request example...	
//		 {
//		     "Namespace":"test-2",
//		     "Stack": {
//		        "Services": {
//		          "redis": {
//		            "Image": "redis"
//		          }
//		         },
//		        "Version": "0.1"
//		      }
//		    }

		String jsonrequest = "{"+
				     "\"Namespace\":\""+stack_name+"\","+
				     "\"Stack\":{"+
				     "\"Services\": {"+
				     dab+
				     "},"+
				     "\"Version\": \"0.1\""+
				     "}}";
	
		log.debug(jsonrequest);
		
		HttpEntity<String> request = new HttpEntity<String>(jsonrequest,requestHeaders);
				
		ResponseEntity<String> responseEntity = createStackRestTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		
		log.debug(responseEntity.getBody().toString());
		return responseEntity.getBody().toString();
	}
	
	/**
	 * search stack.
	 * @param token
	 * @param cluster_id
	 * @param stack_id
	 */
	public String searchStack(String stack_name) {
		
		RestTemplate searchStackRestTemplate = new RestTemplate();
		searchStackRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		searchStackRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        
		String uri = new String(this.configuration.getApi()+"/api/v1/stacks/"+stack_name);
		
		HttpEntity<String> request = new HttpEntity<String>("");
		ResponseEntity<String> responseEntity = searchStackRestTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		
		log.debug(responseEntity.getBody().toString());
		
		return responseEntity.getBody().toString();
	
	}
	
	/**
	 * delete stack.
	 * @param token
	 * @param cluster_id
	 * @param stack_id
	 */
	public String delStack(String stack_name) throws Exception {
		
		RestTemplate delStackRestTemplate = new RestTemplate();
		delStackRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		delStackRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        
		String uri = new String(this.configuration.getApi()+"/api/v1/stacks/"+stack_name);
		
		HttpEntity<String> request = new HttpEntity<String>("");
		ResponseEntity<String> responseEntity = delStackRestTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		
		return responseEntity.getBody().toString();
	
	}
	
	/**
	 * get occuped ports.
	 * @param token
	 * @param cluster_id
	 * @return
	 */
	public List<Long> getOccupedPorts() {
		
		RestTemplate sryOccupiedPortRestTemplate = new RestTemplate();
		sryOccupiedPortRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		sryOccupiedPortRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        
		String uri = new String(this.configuration.getSwarmmgt()+"/services");
		
		HttpEntity<String> request = new HttpEntity<String>("");
		ResponseEntity<String> responseEntity = sryOccupiedPortRestTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		log.debug(responseEntity.getBody().toString());
		List<Long> list = new ArrayList<Long>();
//		JSONArray services = responseEntity.getBody();
//		for(int i=0;i< services.size();i++) {
//			JSONObject innerObj = (JSONObject) services.get(i); 
//			JSONObject endpoint = (JSONObject)innerObj.get("Endpoint");
//			JSONObject ports = (JSONObject)endpoint.get("Ports");
//			String publishedPort = (String)ports.get("PublishedPort");
//			list.add(Long.parseLong(publishedPort));
//		}
		return list;
	} 
}
