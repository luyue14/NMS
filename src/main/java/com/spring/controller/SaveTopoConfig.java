package com.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.model.message.SuccessMessage;
import com.spring.model.topo.Topo;
import com.spring.model.topo.TopoRepository;
import com.spring.model.topoConfig.TopoCoordinate;
import com.spring.model.topoConfig.TopoCoordinateRepository;

@EnableMongoRepositories("com.spring.model")
@Controller
public class SaveTopoConfig {
	@Autowired
	private TopoCoordinateRepository topoCoordinateRepository; 
	@Autowired
	private TopoRepository topoRepository; 
	
	@RequestMapping(value="/Physical-Topo/saveConfig.do",method=RequestMethod.POST)
    public @ResponseBody SuccessMessage handler(@RequestBody TopoCoordinate tpc){
		List<Topo> tps= topoRepository.findAll();
		Topo tp=tps.get(0);
		topoCoordinateRepository.deleteByConfigId(0);
		try{
			tpc.setConfigId(0);
			tpc.setHashCode(tp.hashCode());
			topoCoordinateRepository.save(tpc);
		}catch(Exception e){
			System.out.println("failed to save!!!!!!!!!!!");
		}
		
		SuccessMessage sm = new SuccessMessage();
		sm.setIsSuccess("Success");
		return sm;
	}
}
