package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/single-chat")
    public void singleChat(Message message) {
        String destination = "/subscribe/user/" + message.getToId();
        AppService.save(message);
        messagingTemplate.convertAndSend(destination, message);
    }

    @MessageMapping("/group-chat")
    public void groupChat(Message message) {
        String destination = "/subscribe/group/" + message.getToId();
        AppService.save(message);
        messagingTemplate.convertAndSend(destination, message);
    }

    @MessageMapping("/add-friend")
    public void addFriend(Message message) {

    }

    @MessageMapping("/del-friend")
    public void delFriend(Message message) {

    }

    @MessageMapping("/black-friend")
    public void blackFriend(Message message) {

    }
}
