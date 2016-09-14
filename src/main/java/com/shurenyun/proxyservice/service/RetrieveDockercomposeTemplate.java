package com.shurenyun.proxyservice.service;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shurenyun.proxyservice.domain.ServiceCompose;
import com.shurenyun.proxyservice.util.YamlFileParser;

@Service
public class RetrieveDockercomposeTemplate {
	
	// Define the logger object for this class
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private YamlFileParser yamlfileParser;
	
	
	/**
	 * get docker-compose-template.yml.
	 * @param svn_url
	 * @return template_file
	 */
	public Map<String,ServiceCompose> doGet(String svn_url) {
		
		String[] svn_split = svn_url.split("/");
		String svn_app_name = svn_split[svn_split.length-1];
		String template_file_name = "/data/"+svn_app_name+"/docker-compose-template.yml";
		
		//get docker-compose-template from SVN.
		log.debug("get docker-compose-template.yml from SVN.");
		getTemplateFromSVN(svn_url);
		
		//load docker-compose-template.
		log.debug("load docker-compose-template.yml.");
		return loadTemplate(template_file_name);
	}
	
	/**
	 * get docker compose template from SVN 
	 * @param svn_url
	 */
	private void getTemplateFromSVN(String svn_url) {
		//TODO:get docker compose template from SVN.
		//TODO:save docker compose template to local.
		
	}
	
	/**
	 * load docker compose template
	 * @param template_file_name
	 * @return template_file
	 */
	private Map<String,ServiceCompose> loadTemplate(String template_file_name){
		
		Map<String,Map<String,Object>> service_yaml = yamlfileParser.readFromFile(template_file_name);
		return yamlfileParser.parse(service_yaml);
	
	}
}
