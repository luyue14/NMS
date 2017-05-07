package com.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.spring.model.cluster.ClusterRepository;
import com.spring.model.topo.TopoRepository;
import com.spring.model.topoConfig.TopoCoordinateRepository;
import com.spring.model.vm.Vmsetting;
import com.spring.model.vm.VmsettingRepository;

@EnableMongoRepositories("com.spring.model")
public class VmsettingService{
	
	private static  Logger logger = LoggerFactory.getLogger(TopoService.class);

	@Autowired
	public TopoCoordinateRepository topoCoordinateRepository; 
	@Autowired
	public  TopoRepository topoRepository;
	@Autowired
	public ClusterRepository clusterRepository;
	@Autowired
	public VmsettingRepository vmsettingRepository;
	
	public Boolean deleteAllVmsettings(){
		try{
			 vmsettingRepository.deleteAll();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Can not delete all vmsetting from database!");
		}
		return true;
	}
	
	public Boolean saveVmsetting(Vmsetting vmsetting){
		try{
			 vmsettingRepository.save(vmsetting);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Can not save vmsetting to database!");
		}
		return true;
	}	
	
}