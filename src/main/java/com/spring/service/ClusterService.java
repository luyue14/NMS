package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.spring.model.cluster.Cluster;
import com.spring.model.cluster.ClusterRepository;
import com.spring.model.topo.TopoRepository;
import com.spring.model.topoConfig.TopoCoordinateRepository;
import com.spring.model.vm.VmsettingRepository;

@EnableMongoRepositories("com.spring.model")
public class ClusterService {
		
	private static  Logger logger = LoggerFactory.getLogger(ConfigService.class);
	
	@Autowired
	public TopoCoordinateRepository topoCoordinateRepository; 
	@Autowired
	public TopoRepository topoRepository;
	@Autowired
	public ClusterRepository clusterRepository;
	@Autowired
	public  VmsettingRepository vmsettingRepository;
	
	public List<Cluster> getSelectedCluster(Boolean selected){
		List<Cluster> clusters = new ArrayList<Cluster>();
		try{
			clusters = clusterRepository.findBySelected(selected);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Can not get selected cluster from database!");
		}
		return clusters;
	}
	
	public Boolean deleteAllClusters(){
		try{
			 clusterRepository.deleteAll();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Can not delete all cluster from database!");
		}
		return true;
	}
	
	public Boolean saveCluster(Cluster cluster){
		try{
			 clusterRepository.save(cluster);
		}catch(Exception e){
			logger.error("Can not save cluster to database!");
		}
		return true;
	}	
}
