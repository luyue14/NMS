package com.spring.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spring.model.portconfig.PortConfig;
import com.spring.model.portconfig.PortConfigs;

public class GetPortConfig {
	Logger logger = LoggerFactory.getLogger(GetPortConfig.class);

	private String configFile;	
	
	public GetPortConfig(String configFile) {
		// TODO Auto-generated constructor stub
		setConfigFile(configFile);
	}
	
	@SuppressWarnings("resource")  
	public void load(List<PortConfig> portConfigs ){
		String path="";
		try{
			path = this.getClass().getResource("").toURI().getPath();
		} catch (URISyntaxException e1){
			logger.error("URI syntax Exception!");
		}
        File com = new File(path).getParentFile().getParentFile().getParentFile();  
        File root=com.getParentFile().getParentFile().getParentFile().getParentFile();  
        File external = new File(root.getAbsolutePath()+"/"+configFile);  
        
		if(external.exists()) {
			try{
				String buffer =  new String("");
				
	            FileReader reader = new FileReader(external);
	            BufferedReader br = new BufferedReader(reader);	                     

	            while((buffer = br.readLine()) != null) {
	            	
	            	String[] tmp = buffer.split(" ");
	            	String dpId = tmp[0].split(":")[1];
	            	String port = tmp[1];
	            	String macAddress = tmp[2];
	            	String ipAddress = tmp[3];
	            	String network = tmp[4];
	            	PortConfig portConfig = new PortConfig(dpId, port, macAddress, ipAddress, network);
	            	portConfigs.add(portConfig);
	            	
	                System.out.println(buffer);
	                System.out.println(dpId+"-----"+port+"-----"+macAddress+"-----"+ipAddress+"-----"+network);

	            }

			}catch (FileNotFoundException e){
				logger.error("File not found Exception!");
			}
			catch(IOException e) {
                e.printStackTrace();
          }
		}else{
			logger.error("can not find config file from:"+external.getAbsolutePath());
		}
		
		
		logger.info("Config loaded");

	}
	
	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
//	
//	public static void main(String args[]) throws IOException { 
//		String file ="";
//		GetPortConfig test = new GetPortConfig();
//		test.setConfigFile(file);
//		List<PortConfig> portConfigs = new ArrayList<PortConfig>();
//		test.load(portConfigs);
//	}
//	
}
