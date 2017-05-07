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
import com.spring.model.topoConfig.Link;
import com.spring.model.topoConfig.Node;
import com.spring.model.topoConfig.TopoCoordinate;
import com.spring.model.topoConfig.TopoCoordinateRepository;

@EnableMongoRepositories("com.spring.model")
@Controller
public class ReturnConfigtoOneKeyStart {
	
	@Autowired
	private TopoCoordinateRepository topoCoordinateRepository; 
	@Autowired
	private TopoRepository topoRepository; 
	
	@RequestMapping(value="/OneKeyStart/config.do",method=RequestMethod.POST)
    public @ResponseBody TopoCoordinate handler(){
		List<TopoCoordinate> topoCoordinates = new ArrayList<TopoCoordinate>();
		TopoCoordinate topoCoordinate = new TopoCoordinate();
		Topo topo = new Topo();;
		Boolean judge = false;

		try{
			topoCoordinates = topoCoordinateRepository.findByConfigId(0);
		}catch(Exception e){
		}
	
		
		try{
			topo = topoRepository.findAll().get(0);
		}catch(Exception e){
		}
		
		if(topoCoordinates.size()>0){
			for(int i=0;i<topoCoordinates.size();i++){
				if((topoCoordinates.get(i).getConfigId()==0)&&(topoCoordinates.get(i).getHashCode()==topo.hashCode())){//hashCode不同 造成不能成功使用topoconfig
					topoCoordinate=topoCoordinates.get(i);
					judge=true;
				}
			}
		}
		if(!judge){
			Integer  configId = 1;
			long timeStamp=0;
		    List<Link> lk = new ArrayList<Link>();
		    List<Node> nd = new ArrayList<Node>();
			topoCoordinate.setConfigId(configId);
			topoCoordinate.setTimeStamp(timeStamp);
			topoCoordinate.setLink(lk);
			topoCoordinate.setNode(nd);
		}
		return topoCoordinate;
	}
}
