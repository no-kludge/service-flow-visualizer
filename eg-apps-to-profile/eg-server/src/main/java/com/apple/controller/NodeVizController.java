package com.apple.controller;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.model.Populator;

@RestController
public class NodeVizController {
	
	@RequestMapping(value= "/lookuptest", method=RequestMethod.GET)
	@ResponseBody
	public String lookup() throws InterruptedException {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		Populator p = ctx.getBean("populator", Populator.class);
		
		return p.getMessage();
	}
}
