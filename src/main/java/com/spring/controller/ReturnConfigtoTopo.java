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
public class ReturnConfigtoTopo {
	@Autowired
	private TopoCoordinateRepository topoCoordinateRepository; 
	@Autowired
	private TopoRepository topoRepository;
	@RequestMapping(value="/Physical-Topo/config.do",method=RequestMethod.POST)
    public @ResponseBody TopoCoordinate handler(){
		List<TopoCoordinate> ltpc = new ArrayList<TopoCoordinate>();
		List<Topo> tps = new ArrayList<Topo>();
		Topo tp = new Topo();

		try{
			ltpc = topoCoordinateRepository.findAll();
			tps=topoRepository.findAll();
			tp=tps.get(0);
		}catch(Exception e){
			System.out.println("failed to find all topo coordinate");
		}
		
		TopoCoordinate tpc = new TopoCoordinate();
		Boolean jud = true;

		if(ltpc.size()>0){
			for(int i=0;i<ltpc.size();i++){
				if((ltpc.get(i).getConfigId()==0)&&(ltpc.get(i).getHashCode()==tp.hashCode())){//hashCode不同 造成不能成功使用topoconfig
					tpc=ltpc.get(i);
					jud=false;
				}
			}
		}

		if(jud){
			Integer  configId = 1;
		    List<Link> lk = new ArrayList<Link>();
		    List<Node> nd = new ArrayList<Node>();
			tpc.setConfigId(configId);
			tpc.setLink(lk);
			tpc.setNode(nd);
		}
		return tpc;
	}
}
