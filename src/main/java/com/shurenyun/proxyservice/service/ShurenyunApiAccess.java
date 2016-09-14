package com.shurenyun.proxyservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shurenyun.proxyservice.service.entity.SryAuthRequest;
import com.shurenyun.proxyservice.service.entity.SryAuthResponse;
import com.shurenyun.proxyservice.util.ServiceProperties;

@Service
public class ShurenyunApiAccess {
	
	// Define the logger object for this class
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	  	
	@Autowired
	private ServiceProperties configuration;
	
	/**
	 * request to get access token.
	 * @return
	 */
	public String doAuthentication(){
		
		String token = new String();
	
		RestTemplate authRestTemplate = new RestTemplate();
		authRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		authRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        
		String uri = new String(this.configuration.getApi()+"/auth");
		SryAuthRequest sryAuthRequest = new SryAuthRequest();
		sryAuthRequest.setName(this.configuration.getName());
		sryAuthRequest.setPassword(this.configuration.getPassword());
		
		ResponseEntity<SryAuthResponse> responseEntity = authRestTemplate.postForEntity(uri, sryAuthRequest, SryAuthResponse.class);
		SryAuthResponse sryAuthResponse = responseEntity.getBody();
		
		token = sryAuthResponse.getData().getToken();
		log.debug(token);
		
		return token;
	}	
}
