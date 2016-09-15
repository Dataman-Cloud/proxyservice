package com.shurenyun.proxyservice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.shurenyun.proxyservice.entity.ServiceCompose;

@Component
public class YamlFileParser {

	// Define the logger object for this class
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * read from docker-compose.yml
	 * @param yaml_file_name
	 * @return
	 */
	public Map<String,Map<String,Object>> load(String yaml_file_name) {	
		
		File yaml_file = new File(yaml_file_name);
		InputStream is = null;
		try {
			is = new FileInputStream(yaml_file);
			Yaml yaml = new Yaml();
			
			@SuppressWarnings("unchecked")
			Map<String,Map<String,Object>> serviceObj  = (Map<String,Map<String,Object>>) yaml.load(is);
			is.close();
			return serviceObj;
		} catch (FileNotFoundException e) {
			log.error("Unable to open file '" +yaml_file_name+ "'");      
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Error reading file '" +yaml_file_name+ "'"); 
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * parse yaml file.
	 * @param service_yaml
	 */
	public Map<String,ServiceCompose> parse(Map<String,Map<String,Object>> serviceObj){
		
			Map<String,ServiceCompose> services = new HashMap<String,ServiceCompose>();
			
			//get service map.
			for (String service_name : serviceObj.keySet()) {
				//service_name
				Map<String,Object> service_config = serviceObj.get(service_name);
				ServiceCompose servicecompose = new ServiceCompose();
				for(String conf_name: service_config.keySet()) {
					//image
					if(conf_name.equals("image")) {
						String image = service_config.get(conf_name).toString();
						servicecompose.setImage(image.replaceAll(" ", ""));
					}
					
					//environment
					if(conf_name.equals("environment")) {
						String env = service_config.get(conf_name).toString();
						env = env.replace("{", "");
						env = env.replace("}", "");
						List<String> env_list  = new ArrayList<String>();
						String[] env_array = env.split(",");
						for(int i=0;i<env_array.length;i++) {
							env_list.add(env_array[i].replaceAll(" ", ""));
						}
						servicecompose.setEnv(env_list);
					}
					//links
					if(conf_name.equals("links")) {
						String links = service_config.get(conf_name).toString();
						links = links.replace("[", "");
						links = links.replace("]", "");
						List<String> links_list  = new ArrayList<String>();
						String[] links_array = links.split(",");
						for(int i=0;i<links_array.length;i++) {
							links_list.add(links_array[i].replaceAll(" ", ""));
						}
						servicecompose.setLinks(links_list);
					}
					//ports
					if (conf_name.equals("ports")) {
						String ports = service_config.get(conf_name).toString();
						ports = ports.replace("[", "");
						ports = ports.replace("]", "");
						List<String> ports_list = new ArrayList<String>();
						String[] ports_array = ports.split(",");
						for(int i=0;i<ports_array.length;i++) {
							ports_list.add(ports_array[i].replaceAll(" ", ""));
						}
						servicecompose.setPorts(ports_list);
					}
					//volumes
					if (conf_name.equals("volumes")) {
						String volumes = service_config.get(conf_name).toString();
						volumes = volumes.replace("[", "");
						volumes = volumes.replace("]", "");
						List<String> volume_list = new ArrayList<String>();
						String[] volume_array = volumes.split(",");
						for(int i=0;i<volume_array.length;i++) {
							volume_list.add(volume_array[i].replaceAll(" ", ""));
						}
						servicecompose.setVolumes(volume_list);
					}
				}
				services.put(service_name, servicecompose);
			}
			printYamlMap(services);
			return services;
	}
	
	/**
	 * print yaml map.
	 */
	public void printYamlMap(Map<String,ServiceCompose> services){
		
		log.debug("print yaml map.");
		
		for(String service_name: services.keySet()) {
			log.debug("service_name:"+service_name);
			ServiceCompose service_config = services.get(service_name);
			log.debug("image:"+service_config.getImage());
			if(service_config.getEnv()!=null)
				log.debug("environment:"+service_config.getEnv().toString());
			if(service_config.getLinks()!=null)
				log.debug("links:"+service_config.getLinks().toString());
			if(service_config.getPorts()!=null)
				log.debug("ports:"+service_config.getPorts().toString());
			if(service_config.getVolumes()!=null)
				log.debug("volumes:"+service_config.getVolumes().toString());
			
		}
		
	}
}