package com.spring.controller;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.service.SortService;
import com.spring.util.FlowtableGetter;

@EnableMongoRepositories("com.spring.model")
@Controller
public class ReturnFlowtable2 {
	SortService sortService = new SortService();

	@RequestMapping(value="/OneKeyStart/flowtable.do",method=RequestMethod.POST)
    public @ResponseBody String handler(@RequestBody String nodeId){
		nodeId=nodeId.substring(1,nodeId.length()-1);
		FlowtableGetter flowtableGetter = new FlowtableGetter(nodeId);
		String result = new String();
		try{
			result = sortService.sortFlowtable(flowtableGetter.getFlowtable());
		}catch(Exception e){
		}
		return result;
	}
}
