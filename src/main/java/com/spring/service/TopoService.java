package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.spring.model.cluster.ClusterRepository;
import com.spring.model.topo.Topo;
import com.spring.model.topo.TopoRepository;
import com.spring.model.topoConfig.TopoCoordinateRepository;
import com.spring.model.vm.VmsettingRepository;

@EnableMongoRepositories("com.spring.model")
public class TopoService {
	
	private static  Logger logger = LoggerFactory.getLogger(TopoService.class);
	
	@Autowired
	public TopoCoordinateRepository topoCoordinateRepository; 
	@Autowired
	public TopoRepository topoRepository;
	@Autowired
	public ClusterRepository clusterRepository;
	@Autowired
	public VmsettingRepository vmsettingRepository;
	
	public List<Topo> getAllTopo(){
	       List<Topo> topos = new ArrayList<Topo>();
			try {
	    	   topos  = topoRepository.findAll();	    	   
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Can not get all topo from database.");;
			}
			return topos;
	}
	
	public Topo getTopo(int index){
		   List<Topo> topos = new ArrayList<Topo>();
	       Topo topo = new Topo();
			try {
	    	   topos  = topoRepository.findAll();	    	   
			} catch (Exception e) {
				logger.error("Can not get all topo from database.");;
			}
			try{
				topo = topos.get(index);
			} catch (Exception e){
				e.printStackTrace();
				logger.error("Can not get" + index + " topo.");
			}
			return topo;
	}
}
