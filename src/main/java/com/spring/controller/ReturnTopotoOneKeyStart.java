package com.spring.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.model.topo.Topo;
import com.spring.model.topo.TopoRepository;
@EnableMongoRepositories("com.spring.model")
@Controller
public class ReturnTopotoOneKeyStart {
	@Autowired
	private TopoRepository topoRepository;
	
	@RequestMapping(value="/OneKeyStart/topo.do",method=RequestMethod.POST)
    public @ResponseBody Topo handler(){
		   List<Topo> tps = new ArrayList<Topo>();
	       Topo tp = new Topo();
	       try {
	    	   tps = topoRepository.findAll();
				tp = tps.get(0);
			} catch (Exception e) {
				System.out.println("database failed");
			}
	       
	       return tp;
	}
}
