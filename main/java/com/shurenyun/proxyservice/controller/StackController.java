package com.shurenyun.proxyservice.controller;

import org.springframework.web.bind.annotation.RestController;

import com.shurenyun.proxyservice.controller.vo.AddStackRequest;
import com.shurenyun.proxyservice.domain.ServiceCompose;
import com.shurenyun.proxyservice.domain.EQImage;
import com.shurenyun.proxyservice.service.CreateDabCompose;
import com.shurenyun.proxyservice.service.RetrieveDockercomposeTemplate;
import com.shurenyun.proxyservice.service.RetrieveNotOccupiedPort;
import com.shurenyun.proxyservice.service.ShurenyunApiRequestForward2;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class StackController {
	
	// Define the logger object for this class
 	private final Logger log = LoggerFactory.getLogger(this.getClass());
 	
 	@Autowired
	RetrieveDockercomposeTemplate retrieveDockercomposeTemplate;
	
 	@Autowired
	RetrieveNotOccupiedPort retrieveNotOccupiedPort;

	@Autowired
	CreateDabCompose createDabCompose;
	
	@Autowired
	ShurenyunApiRequestForward2 shurenyunApiRequestForward2;

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/stack",
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.TEXT_PLAIN_VALUE)
	public String create(@Valid @RequestBody AddStackRequest addStackRequest) {
		String svn_url = addStackRequest.getSvn_url();
		String stack_name = addStackRequest.getStack_name();
		List<EQImage> images = addStackRequest.getImages();
		
		String inputmessage = " svn_url:"+svn_url+
							  " stack_name:"+stack_name;
		log.debug("POST /stack "+inputmessage);
		
		//retrieve docker compose template.
		Map<String,ServiceCompose> docker_compose_template_yaml = retrieveDockercomposeTemplate.doGet(svn_url);

		//need to use lock, when retrieve free ports for services.
		Lock lock = new ReentrantLock();
		lock.lock();
		
		//get port. 
		List<Long> not_occupied_ports = retrieveNotOccupiedPort.getPorts(); 
		
		//create dab compose.
		createDabCompose.doCreate(docker_compose_template_yaml,images, not_occupied_ports);
		String dab = createDabCompose.getDab();
			
		//invoke stack API.
		String result = "";
		try{
			result = shurenyunApiRequestForward2.createStack(stack_name,dab);
		}catch(Exception e) {
			result = e.getMessage();
		}
		lock.unlock();
		return result;
		
	}
	
	@RequestMapping(
			method = RequestMethod.GET,
			value = "/stack/{stack_name}",
			produces = MediaType.TEXT_PLAIN_VALUE)
	public String get(@PathVariable("stack_name") String stack_name ) {
		
		log.debug("GET /stack/"+stack_name);
		
		String result = "";
		try{
			//invoke inspect stack API.
			shurenyunApiRequestForward2.searchStack(stack_name);
		}catch(Exception e){
			result = e.getMessage();
		}
		return result; 
	}
	
	@RequestMapping(
			method = RequestMethod.DELETE,
			value = "/stack/{stack_name}",
			produces = MediaType.TEXT_PLAIN_VALUE)
	public String delete(@PathVariable("stack_name") String stack_name ) {
			log.debug("DELETE /stack/"+stack_name);
			String result = "";
			try{
				//invoke delete stack API.
				result = shurenyunApiRequestForward2.delStack(stack_name);
			}catch(Exception e){
				result = e.getMessage();
			}
			return result;
	}

}
