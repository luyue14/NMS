package com.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.model.flag.Flag;
import com.spring.model.flag.FlagSessionFactory;


@Controller
public class GetFlag {
	@RequestMapping(value="/OneKeyStart/getFlag.do",method=RequestMethod.GET)
	public @ResponseBody String handler(){
		Flag flag = FlagSessionFactory.getFlag();
		return flag.getFlag();
	}
	
}
