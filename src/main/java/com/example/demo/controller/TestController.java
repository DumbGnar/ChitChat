package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;

@RestController
public class TestController {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	//现在不许通过不正规方式添加用户
	@RequestMapping("/hello")
	public String say() {
		return "Yes, Indeed\n";
	}
	
}
