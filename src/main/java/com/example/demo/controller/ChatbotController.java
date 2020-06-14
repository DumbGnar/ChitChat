package com.example.demo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.BotService;

@RestController
public class ChatbotController {
	
	// 聊天，uid应该是提问者的uid，用于加载提问者的偏好文件
	@RequestMapping("bot/{uid}/{content}")
	public String chat(@PathVariable(value = "uid")Integer uid, 
			@PathVariable(value = "content")String content) {
		return BotService.chatWithBot(uid, content);
	}
	
	
	// 根据uid创建个人机器人（个人偏好文件）
	@RequestMapping("bot/create/{uid}")
	public boolean create(@PathVariable(value = "uid")Integer uid) {
		return BotService.createSelfChatbot(uid);
	}
}
