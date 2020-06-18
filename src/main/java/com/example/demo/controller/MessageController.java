package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.RequestMessage;
import com.example.demo.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final SimpMessagingTemplate template;

    @Autowired
    public MessageController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/single-chat")
    public void singleChat(@Payload RequestMessage message) {
        String destination = "/uni/chat/" + message.getToId();
//        AppService.save(message);
        this.template.convertAndSend(destination, message);
    }

    @MessageMapping("/group-chat")
    public void groupChat(@Payload RequestMessage message) {
        String destination = "/broad/chat/" + message.getToId();
//        AppService.save(message);
        this.template.convertAndSend(destination, message);
    }

    @MessageMapping("/add-friend")
    public void addFriend(@Payload RequestMessage message) {
        String destination = "/uni/add/" + message.getToId();
//        AppService.save(message);
        this.template.convertAndSend(destination, message);
    }
}
