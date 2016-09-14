package com.shurenyun.proxyservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shurenyun.proxyservice.domain.ServiceCompose;
import com.shurenyun.proxyservice.util.YamlFileParser;
import com.shurenyun.proxyservice.domain.EQImage;

@Service
public class CreateDabCompose {
	
	// Define the logger object for this class
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	Map<String,ServiceCompose> services;
	
	String dab;
	
	public void doCreate(Map<String,ServiceCompose> docker_compose_template_yaml,
			List<EQImage> images,
			List<Long> not_occupied_ports) {
		
		//initial to get services.
		services = docker_compose_template_yaml;
		
		//get service tag.
		getServiceTagResource(images);
		
		//get service port.
		getServicePortResource(not_occupied_ports);
		
		//create service dab.
		createServiceDab();
		
		
	}
	
	/**
	 * get image name and tag.
	 * @param images
	 */
	private void getServiceTagResource(List<EQImage> images) {
		
		//create compose image name with tag.
		for (String service_name : this.services.keySet()) {
			ServiceCompose service_config = this.services.get(service_name);
			String template_image_name = service_config.getImage();
			String[] template_image_name_array = template_image_name.split(":");
			//replace template image name with user's input.
			for(EQImage image: images) {
				String image_name = image.getName();
				if(template_image_name_array[0].equals(image_name)){
					String image_tag = image.getTag();
					String compose_name = image_name+":"+image_tag;
					service_config.setImage(compose_name);
				}
			}
		}	
	}
	
	/**
	 * get port.
	 * @param docker_componse_template_yaml
	 */
	private void getServicePortResource(List<Long> not_occupied_ports) {
		//create service port map.
		Map<String,String> service_port_map = new HashMap<String,String>();
		
		int i = 0;
		//replace template port with not_occupy_port.
		for (String service_name : this.services.keySet()) {
		
			Long not_occupy_port = not_occupied_ports.get(i);
			ServiceCompose service_config = this.services.get(service_name);
			List<String> template_ports = (List<String>)service_config.getPorts();
			List<String> ports = new ArrayList<String>();
			if(template_ports!=null) {
				for(String template_port: template_ports) {
					String template_port_tag = template_port.split(":")[0];
					ports.add(Long.toString(not_occupy_port));
					service_port_map.put(template_port_tag, Long.toString(not_occupy_port));
					i++;
				}
			}
			service_config.setPorts(ports);
		}
		//replace template environment with service_port_map.
		for(String service_name :this.services.keySet()) {
			ServiceCompose service_config = this.services.get(service_name);
			List<String> template_envs = (List<String>)service_config.getEnv();
			List<String> envs = new ArrayList<String>();
			if(template_envs!=null) {
				for(String template_env: template_envs) {
					String[] template_env_array = template_env.split("=");
					//print service_port_map.
					for(String service_port: service_port_map.keySet()) {
						//replace template port.
						if(template_env_array != null && template_env_array.length==2){
							if(template_env_array[1].trim().equals(service_port)) {
								envs.add("\""+template_env_array[0]+"="+service_port.replace(template_env_array[1].trim(), service_port_map.get(service_port))+"\"");
							}
						}	
					}
				}
			}	
			service_config.setEnv(envs);
		}

	}
	
	/**
	 * create dab.
	 *	dab example for service....	      
		
		"sampleconfig": {
	      "Name": "sampleconfig",
	      "TaskTemplate": {
	        "ContainerSpec": {
	          "Image": "index.shurenyun.com/library/sample-hystrix-config:1.0.1-SNAPSHOT",
	          "Env": [
	            "EUREKA_HOST=sampleeureka",
	            "EUREKA_PORT=$EUREKA_PORT$",
	            "SERVER_PORT=$SERVER_PORT$"
	          ]
	        }
	      },
	      "Networks": [
	        "ingress"
	      ],
	      "EndpointSpec": {
	        "Mode": "vip",
	        "Ports": [
	          {
	            "Name": "pbport",
	            "Protocol": "tcp",
	            "TargetPort": $CONFIG_PORT$,
	            "PublishedPort": 8888
	          }
	        ]
	      }
	    },
	 */
	private void createServiceDab() {
		
		dab = "";
	
		ServiceCompose serviceCompose = null;
		int j=0;
		for(String service_name: services.keySet()) {
			serviceCompose = (ServiceCompose)services.get(service_name);
			
			//Env in dab.
			String envIndab = "";
			int i = 0;
			for(String env:serviceCompose.getEnv()){
				if(i==0) {
					envIndab += "\"Env\": [";
				}		 
				envIndab += env.replaceAll("^\\s+|\\s+$", "");
				if(i<serviceCompose.getEnv().size()-1) {
					envIndab += ",";
				}	
				if(i==serviceCompose.getEnv().size()-1) {
					envIndab += "]";
				}
				i++;
				
			}
			
			//Ports in dab.
			String portsIndab = "";
			i =0;
			for(String port:serviceCompose.getPorts()) {
				if(i==0) {
					portsIndab += "\"Ports\": [";
					
				}
				
				portsIndab += "{"+
				 "\"Name\": \"pbport\","+
		         "\"Protocol\": \"tcp\","+
		         "\"TargetPort\": \""+port+"\","+
		         "\"PublishedPort\": 8888"+
		         "}";
				
				if(i<serviceCompose.getPorts().size()-1) {
					portsIndab += ",";
				}
				if(i==serviceCompose.getPorts().size()-1) {
					portsIndab += "]";
				}
				i++;
				
			}

			dab += 
				 "\""+service_name+"\": { "+
				 "\"Name\": \""+service_name+"\","+
				 "\"TaskTemplate\": {"+
				 "\"ContainerSpec\": {"+
				 "\"Image\": \""+serviceCompose.getImage()+"\""+(envIndab.equals("")?"":",")+
				 envIndab+"}},"+
				 "\"Networks\": [\"ingress\"],"+
				 "\"EndpointSpec\": {"+
				 "\"Mode\": \"vip\""+(portsIndab.equals("")?"":",")+
				 portsIndab+"}}";
			if(j<services.size()-1) {
				dab += ",";
			}
			j++;

		}
	}
	

	/**
	 * get services.
	 * @return
	 */
	public Map<String, ServiceCompose> getServices() {
		return services;
	}

	public String getDab() {
		return dab;
	}

	public void setDab(String dab) {
		this.dab = dab;
	}
	

}
