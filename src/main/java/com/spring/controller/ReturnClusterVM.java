package com.spring.controller;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.model.clusterVM.ClusterVM;
import com.spring.model.topo.Topo;
import com.spring.model.topo.TopoRepository;
import com.spring.util.InitClusterVM;

@EnableMongoRepositories("com.spring.model")
@Controller
public class ReturnClusterVM {
	@Autowired
	private TopoRepository topoRepository;
	
	private static  Logger logger = LoggerFactory.getLogger(ReturnClusterVM.class);
	
	@RequestMapping(value="/OneKeyStart/cluster.do",method=RequestMethod.POST)
	public @ResponseBody ClusterVM handler(){
		ClusterVM cv = new ClusterVM();
		Topo tp = new Topo();
		List<Topo> tps =  new ArrayList<Topo>();
	       try {
	    	   tps = topoRepository.findAll();
				tp = tps.get(0);
				cv = InitClusterVM.initClusterVM(tp);
			} catch (Exception e) {
				logger.error("can not get topo");
			}
		
		return cv;
	}
	
}
