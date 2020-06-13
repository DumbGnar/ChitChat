package com.example.demo.controller;

import java.awt.image.BufferedImage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.base.user.User;
import com.example.demo.base.user.UserUtils;

@RestController
public class UserController {
	
	//根据用户UID获取用户信息
	@RequestMapping("user/{uid}")
	public User getUserByID(@PathVariable(value = "uid")Integer id) {
		
		//数据库检索...
		
		return null;	//要改的
	}
	
	//根据用户名loginID获取用户信息
	@RequestMapping("user/searchByLoginID/{loginID}")
	public boolean searchByLoginID(@PathVariable(value = "loginID")String loginID) {
		return false;
	}
	
	//根据用户UID获取用户头像图片,这个方法已完成
	@RequestMapping("user/headportrait/{uid}")
	public BufferedImage getUserHeadPortraitByID(@PathVariable(value = "uid")Integer id) {
		return UserUtils.getHeadPortraitByID(id);
	}
	
	//（登录用）验证用户名和密码是否匹配，匹配成功返回用户
	@RequestMapping("/user/login/{loginID}/{password}")
	public User userLogin(@PathVariable(value = "loginID")String loginID,
			@PathVariable(value = "password")String password) {
		return null;
	}
	
	//修改用户的某项信息
	/*
	 * method —— 参数详细
	 * 1:age		年龄
	 * 2:loginID	账号
	 * 3:password	密码
	 * 4:sex		性别
	 * 5.headportrait 头像
	 * 
	 * 
	 * */
	@RequestMapping("/user/changeInfo/{method}/{content}")
	public boolean userChangeInfo(@PathVariable(value = "method")Integer method,
			@PathVariable(value = "content")String content) {
		return false;
	}
	
}
