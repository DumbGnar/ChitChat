package com.example.demo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.BotService;

/**
 * 聊天机器人相关接口 ❓
 */
@RestController
public class ChatbotController {

    /**
     * 聊天，uid应该是提问者的uid，用于加载提问者的偏好文件 ❌
     *
     * @param uid
     * @param content
     * @return
     */
    @RequestMapping("bot/{uid}/{content}")
    public String chat(@PathVariable(value = "uid") Integer uid,
                       @PathVariable(value = "content") String content) {
        return BotService.chatWithBot(uid, content);
    }

    /**
     * 根据uid创建个人机器人（个人偏好文件）👌
     *
     * @param uid
     * @return
     */
    @RequestMapping("bot/create/{uid}")
    public boolean create(@PathVariable(value = "uid") Integer uid) {
        return BotService.createSelfChatbot(uid);
    }
}
