package com.shurenyun.proxyservice.controller;

import org.springframework.web.bind.annotation.RestController;

import com.shurenyun.proxyservice.controller.vo.AddStackRequest;
import com.shurenyun.proxyservice.entity.EQImage;
import com.shurenyun.proxyservice.entity.ServiceCompose;
import com.shurenyun.proxyservice.service.DabCompose;
import com.shurenyun.proxyservice.service.DockerComposeTemplate;
import com.shurenyun.proxyservice.service.DynamicResource;
import com.shurenyun.proxyservice.service.DMApiRequestForward;

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
	DockerComposeTemplate dockerComposeTemplate;
	
 	@Autowired
	DynamicResource dynamicResource;

	@Autowired
	DabCompose dabCompose;
	
	@Autowired
	DMApiRequestForward DMApiRequestForward;

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
		Map<String,ServiceCompose> services = dockerComposeTemplate.doGet(svn_url);

		//need to use lock, when retrieve free ports for services.
		Lock lock = new ReentrantLock();
		lock.lock();
		
		//get port. 
		List<Long> notOccupiedPorts = dynamicResource.getPorts(); 
		
		//create dab compose.
		String dab = dabCompose.doCreate(services,images, notOccupiedPorts);
			
		//invoke stack API.
		String result = "";
		try{
			result = DMApiRequestForward.createStack(stack_name,dab);
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
			result = DMApiRequestForward.searchStack(stack_name);
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
				result = DMApiRequestForward.delStack(stack_name);
			}catch(Exception e){
				result = e.getMessage();
			}
			return result;
	}

}
