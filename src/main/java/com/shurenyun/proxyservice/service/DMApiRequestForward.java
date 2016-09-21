package com.shurenyun.proxyservice.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

import com.shurenyun.proxyservice.entity.ServiceResponse;
import com.shurenyun.proxyservice.entity.StackResponse;
import com.shurenyun.proxyservice.util.Properties;

import java.util.ArrayList;
import java.util.List;



@Service
public class DMApiRequestForward {
	
	// Define the logger object for this class
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	  	
	@Autowired
	private Properties configuration;
	
	/**
	 * create stack.
	 * @param stack_name
	 * @param dab
	*/
	public String createStack(String stack_name,String dab){

		RestTemplate createStackRestTemplate = new RestTemplate();
		createStackRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		createStackRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        
		String uri = new String(this.configuration.getApi()+"/api/v1/stacks");
		
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
		
		String jsonInString = "";
		
		RestTemplate searchStackRestTemplate = new RestTemplate();
		searchStackRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		searchStackRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        
		String uri = new String(this.configuration.getApi()+"/api/v1/stacks/"+stack_name+"/services");
		HttpEntity<String> request = new HttpEntity<String>("");
		ResponseEntity<String> responseEntity = searchStackRestTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		
		JSONParser parser = new JSONParser();
		StackResponse stackResponse = new StackResponse();
		
		try {
			String managerUrl = "";
			String nodeuri = new String(this.configuration.getApi()+"/api/v1/nodes");
			ResponseEntity<String> nodesResponse = searchStackRestTemplate.exchange(nodeuri, HttpMethod.GET, new HttpEntity<String>(""), String.class);
			JSONObject json = (JSONObject) JSONValue.parse(nodesResponse.getBody().toString());
			JSONArray nodes = (JSONArray) JSONValue.parse(json.get("data").toString());
			for (int y = 0; y < nodes.size(); y++) {
				JSONObject node = (JSONObject) JSONValue.parse(nodes.get(y).toString());
				JSONObject isManager = (JSONObject) JSONValue.parse(node.get("ManagerStatus").toString());
				
				if (Boolean.parseBoolean(isManager.get("Leader").toString()) && 
						isManager.get("Reachability").toString().equals("reachable")) {
					JSONObject labels = (JSONObject) ((JSONObject)JSONValue.parse(node.get("Spec").toString())).get("Labels");
					String[] url = labels.get("dm.reserved.node.endpoint").toString().split(":");
					if (url.length == 3) {
						managerUrl = url[1];
					} else {
						managerUrl = url[0];
					}
					break;
				}
			}
			
			JSONObject stack = (JSONObject) parser.parse(responseEntity.getBody().toString());
			JSONArray services = (JSONArray) parser.parse(stack.get("data").toString());
			List<ServiceResponse> list  = new ArrayList<ServiceResponse>();
			for (int i = 0;i < services.size(); i++) {
				

				ServiceResponse sr = new ServiceResponse();
				JSONObject service = (JSONObject) parser.parse(services.get(i).toString());
				int numTasksTotal = Integer.parseInt(service.get("NumTasksTotal").toString());
				int numTasksRunning = Integer.parseInt(service.get("NumTasksRunning").toString());
				if (numTasksTotal == 0 && numTasksRunning == 0) {
					sr.setStatus("not stated");
				} else if (numTasksRunning == numTasksTotal) {
					sr.setStatus("running");
				} else {
					sr.setStatus("");
				}
				
				sr.setId(service.get("ID").toString());
				sr.setName(service.get("Name").toString());
				
				String serviceuri = new String(this.configuration.getApi()+"/api/v1/stacks/"+stack_name+"/services/" + sr.getName());
				ResponseEntity<String> response = searchStackRestTemplate.exchange(serviceuri, HttpMethod.GET, new HttpEntity<String>(""), String.class);
				JSONObject sinfo = (JSONObject)JSONValue.parse(response.getBody().toString());
				JSONObject data = (JSONObject)JSONValue.parse(sinfo.get("data").toString());
				JSONObject endpoint = (JSONObject) JSONValue.parse(data.get("Endpoint").toString());
				JSONArray ports = (JSONArray) JSONValue.parse(endpoint.get("Ports").toString());
				for (int x = 0;x < ports.size(); x++) {
					JSONObject port = (JSONObject) JSONValue.parse(ports.get(x).toString());
					sr.setUrl("http://" +managerUrl+":" + port.get("PublishedPort").toString());
				}
				
				
				list.add(sr);
			}
			stackResponse.setApp_list(list);
			ObjectMapper mapper = new ObjectMapper();
			jsonInString = mapper.writeValueAsString(stackResponse);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();

		}
		
		//log.debug(responseEntity.getBody().toString());
		//return responseEntity.getBody().toString();
		
		//log.debug(jsonInString);
	
		log.debug(jsonInString);
		return jsonInString;
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
		
		JSONParser parser = new JSONParser();
		JSONArray services = new JSONArray();
		try {
			services = (JSONArray)parser.parse(responseEntity.getBody());
			for(int i=0;i< services.size();i++) {
				JSONObject innerObj = (JSONObject) parser.parse(services.get(i).toString()); 
				JSONObject endpoint = (JSONObject)parser.parse(innerObj.get("Endpoint").toString());
				if (endpoint.get("Ports") == null) continue;
				JSONArray ports = (JSONArray)parser.parse(endpoint.get("Ports").toString());
				for (int x = 0; x < ports.size(); x++) {
					JSONObject port = (JSONObject)parser.parse(ports.get(x).toString());
					list.add(Long.parseLong(port.get("PublishedPort").toString()));
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return list;
	} 
}
