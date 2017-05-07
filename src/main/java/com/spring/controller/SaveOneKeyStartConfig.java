package com.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.model.message.SuccessMessage;
import com.spring.model.topoConfig.TopoCoordinate;
import com.spring.model.topoConfig.TopoCoordinateRepository;

@EnableMongoRepositories("com.spring.model")
@Controller
public class SaveOneKeyStartConfig {
	@Autowired
	private TopoCoordinateRepository topoCoordinateRepository; 
	@RequestMapping(value="/OneKeyStart/saveConfig.do",method=RequestMethod.POST)
    public @ResponseBody SuccessMessage handler(@RequestBody TopoCoordinate tpc){
		topoCoordinateRepository.deleteByConfigId(1);
		SuccessMessage sm = new SuccessMessage();

		try{
			tpc.setConfigId(1);
			//timeStamp需要在savecluster里同步
			//tpc.setTimeStamp(timeStamp);
			topoCoordinateRepository.save(tpc);
		}catch(Exception e){
			System.out.println("failed to save!!!!!!!!!!!");
		}finally{
			System.out.println("after try");
		}
		//System.out.println(topoCoordinateRepository.findAll());
		
		sm.setIsSuccess("success");
		return sm;
	}
}
