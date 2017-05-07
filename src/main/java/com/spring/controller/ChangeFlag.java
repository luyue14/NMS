package com.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.model.flag.Flag;
import com.spring.model.flag.FlagSessionFactory;
import com.spring.model.message.SuccessMessage;

@Controller
public class ChangeFlag {
	
	@RequestMapping(value="/changeFlag.do",method=RequestMethod.POST)
	public @ResponseBody SuccessMessage handler(@RequestBody String flagInfo){
		Flag flag = FlagSessionFactory.getFlag();
		flag.putFlag(flagInfo);
		SuccessMessage sm= new SuccessMessage("success");
		return sm;
	}
}
