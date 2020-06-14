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
	
	@RequestMapping("/hello")
	public User say() {
		return new User("123456", "cs","1@qq.com");
	}
	
	@RequestMapping("/testdb")
	public boolean db() {
		
		mongoTemplate.insert(new User("testUser", "123456","1@qq.com"));
		return true;
	}
}
