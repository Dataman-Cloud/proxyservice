package com.shurenyun.proxyservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shurenyun.proxyservice.entity.EQImage;
import com.shurenyun.proxyservice.entity.ServiceCompose;

@Service
public class DabCompose {
	
	public String doCreate(Map<String,ServiceCompose> serviceComposes,
			List<EQImage> images,
			List<Long> notOccupiedPorts) {
		
		//get service tag.
		getServiceTagResource(serviceComposes, images);
		
		//get service port.
		getServicePortResource(serviceComposes, notOccupiedPorts);
		
		//create service dab.
		String dab = createServiceDab(serviceComposes);
		return dab;
		
	}
	
	/**
	 * get image name and tag.
	 * @param images
	 */
	private void getServiceTagResource(Map<String,ServiceCompose> serviceComposes,
										List<EQImage> images) {
		
		//get services.
		for (String serviceName : serviceComposes.keySet()) {
			
			ServiceCompose serviceCompose = serviceComposes.get(serviceName);
			String serviceImage = serviceCompose.getImage();
			String[] ServiceImageAndTag = serviceImage.split(":");
			
			//replace template image name with api's input.
			for(EQImage image: images) {
				String imageName = image.getName();
				String imageTag = image.getTag();
			
				if(ServiceImageAndTag[0].equals(imageName)){
					String newServiceImage = imageName+":"+imageTag;
					serviceCompose.setImage(newServiceImage);
				}
				
			}
			
		}	
	}
	
	/**
	 * get port.
	 * @param services
	 */
	private void getServicePortResource(Map<String,ServiceCompose> serviceComposes,
										List<Long> notOccupiedPorts) {
	
		//create service/port map.
		Map<String,String> serviceAndport = new HashMap<String,String>();
		
		int i = 0;
		//replace template port with notOccupiedPort.
		for (String serviceName : serviceComposes.keySet()) {
		
			Long notOccupiedPort = notOccupiedPorts.get(i);
			
			ServiceCompose serviceCompose = serviceComposes.get(serviceName);
			List<String> servicePorts = (List<String>)serviceCompose.getPorts();
		
			List<String> ports = new ArrayList<String>();
			if(servicePorts!=null) {
				for(String servicePort: servicePorts) {
					String serviceTargetPortTag = servicePort.split(":")[0];
					String servicePublishedPort = servicePort.split(":")[1];
					ports.add(Long.toString(notOccupiedPort)+":"+servicePublishedPort);
					serviceAndport.put(serviceTargetPortTag, Long.toString(notOccupiedPort));
					i++;
				}
			}
			serviceCompose.setPorts(ports);
		}
		
		//replace template environment with map serviceAndport.
		for(String serviceName :serviceComposes.keySet()) {
			
			ServiceCompose serviceCompose = serviceComposes.get(serviceName);
			List<String> serviceEnvs = (List<String>)serviceCompose.getEnv();
			
			List<String> envs = new ArrayList<String>();
			if(serviceEnvs!=null) {
				for(String serviceEnv: serviceEnvs) {
					String[] serviceEnvNameValue = serviceEnv.split("=");
				
					if(serviceEnvNameValue != null && serviceEnvNameValue.length==2){
						
						for(String servicePort: serviceAndport.keySet()) {
				
							if(serviceEnvNameValue[1].trim().equals(servicePort)) {
								envs.add("\""+serviceEnvNameValue[0]+"="+servicePort.replace(serviceEnvNameValue[1].trim(), serviceAndport.get(servicePort))+"\"");
							}
							
						}	
					}
				}
			}	
			serviceCompose.setEnv(envs);
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
	private String createServiceDab(Map<String,ServiceCompose> serviceComposes) {
		
		String dab = "";
	
		ServiceCompose serviceCompose = null;
		
		int serviceNum=0;
		for(String serviceName: serviceComposes.keySet()) {
			serviceCompose = (ServiceCompose)serviceComposes.get(serviceName);
			
			//Env in dab.
			String envIndab = "";
			int envNum = 0;
			for(String serviceEnv:serviceCompose.getEnv()){
				if(envNum==0) {
					envIndab += "\"Env\": [";
				}		 
				envIndab += serviceEnv.replaceAll("^\\s+|\\s+$", "");
				if(envNum<serviceCompose.getEnv().size()-1) {
					envIndab += ",";
				}	
				if(envNum==serviceCompose.getEnv().size()-1) {
					envIndab += "]";
				}
				envNum++;
				
			}
			
			//Ports in dab.
			String portsIndab = "";
			int portNum =0;
			for(String servicePort:serviceCompose.getPorts()) {
				if(portNum==0) {
					portsIndab += "\"Ports\": [";
					
				}
				
				portsIndab += "{"+
				 "\"Name\": \"pbport\","+
		         "\"Protocol\": \"tcp\","+
		         "\"TargetPort\": \""+servicePort.split(":")[0]+"\","+
		         "\"PublishedPort\": \""+servicePort.split(":")[1]+"\","+
		         "}";
				
				if(portNum<serviceCompose.getPorts().size()-1) {
					portsIndab += ",";
				}
				if(portNum==serviceCompose.getPorts().size()-1) {
					portsIndab += "]";
				}
				portNum++;
				
			}

			//dab.
			dab += 
				 "\""+serviceName+"\": { "+
				 "\"Name\": \""+serviceName+"\","+
				 "\"TaskTemplate\": {"+
				 "\"ContainerSpec\": {"+
				 "\"Image\": \""+serviceCompose.getImage()+"\""+(envIndab.equals("")?"":",")+
				 envIndab+"}},"+
				 "\"Networks\": [\"ingress\"],"+
				 "\"EndpointSpec\": {"+
				 "\"Mode\": \"vip\""+(portsIndab.equals("")?"":",")+
				 portsIndab+"}}";
		
			if(serviceNum<serviceComposes.size()-1) {
				dab += ",";
			}
			serviceNum++;

		}
		return dab;
	}

}
