package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.base.user.*;;

@RestController
public class TestController {
	@RequestMapping("/hello")
	public User say() {
		return new User("123456", "cs");
	}
}
