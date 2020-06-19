package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.Message;
import com.example.demo.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MessageController {

    private final SimpMessagingTemplate template;

    @Autowired
    public MessageController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/single-chat")
    public void singleChat(@Payload Message message) {
        AppService.save(message);
        String destination = "/uni/chat/" + message.getToId();
        this.template.convertAndSend(destination, message);
    }

    @MessageMapping("/room-chat")
    public void groupChat(@Payload Message message) {
        AppService.save(message);
        String destination = "/broad/chat/" + message.getToId();
        this.template.convertAndSend(destination, message);
    }

    @MessageMapping("/add-friend")
    public void addFriend(@Payload Message message) {
        AppService.save(message);
        String destination = "/uni/add/" + message.getToId();
        this.template.convertAndSend(destination, message);
    }

    @ResponseBody
    @PostMapping("/msg/single")
    public List<Message> singleMsg(@RequestParam int myId,
                                   @RequestParam int friendId) {
        return AppService.getUnreadSingle(myId, friendId);
    }

    @ResponseBody
    @PostMapping("/msg/room")
    public List<Message> roomMsg(@RequestParam int uid,
                                 @RequestParam int gid) {
        return AppService.getUnreadRoom(uid, gid);
    }
}
