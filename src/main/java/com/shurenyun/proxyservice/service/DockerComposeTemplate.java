package com.shurenyun.proxyservice.service;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shurenyun.proxyservice.entity.ServiceCompose;
import com.shurenyun.proxyservice.util.YamlFileParser;
import com.shurenyun.proxyservice.util.Properties;


@Service
public class DockerComposeTemplate {
	
	// Define the logger object for this class
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private String template_file_name;

	@Resource
	private YamlFileParser yamlfileParser;
	
	@Resource
	private Properties configuration;
	
	
	/**
	 * get docker-compose-template.yml.
	 * @param svn_url
	 * @return template_file
	 */
	public Map<String,ServiceCompose> doGet(String svn_url) {
		
		//get docker-compose-template.yml.
		log.debug("get docker-compose-template.yml from SVN.");
		getTemplateFromSVN(svn_url);
		
		//load docker-compose-template.yml.
		log.debug("parse docker-compose-template.yml.");
		return parseTemplate();
	}
	
	/**
	 * get docker compose template from SVN 
	 * @param svn_url
	 */
	private void getTemplateFromSVN(String svn_url) {
		//TODO:get docker compose template from SVN and save docker compose template to local.
		String[] svn_split = svn_url.split("/");
		String svn_app_name = svn_split[svn_split.length-1];
		template_file_name = this.configuration.getDatadir()+ "/" +svn_app_name+"/docker-compose-template.yml";
	}
	
	/**
	 * load docker compose template
	 * @param template_file_name
	 * @return template_file
	 */
	private Map<String,ServiceCompose> parseTemplate(){
		Map<String,Map<String,Object>> serviceObj = yamlfileParser.load(template_file_name);
		return yamlfileParser.parse(serviceObj);
	}
}
