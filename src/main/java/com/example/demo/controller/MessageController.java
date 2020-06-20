package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.NetMessage;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MessageController(SimpMessagingTemplate template, MongoTemplate mongoTemplate) {
        this.messagingTemplate = template;
        this.mongoTemplate = mongoTemplate;
    }

    @PostMapping("/msg/read/user/{myId}/{uid}")
    public void readUserMessage(@PathVariable int myId,
                                @PathVariable int uid) {
        MessageService.readUserMessage(myId, uid);
    }

    /**
     * 返回用户myId的好友uid发给他的所有未读消息
     */
    @PostMapping("/msg/user/{myId}/{uid}")
    public List<NetMessage> getUnreadUser(@PathVariable int myId,
                                          @PathVariable int uid) {
        Date lastReadTime = MessageService.getUserLastRead(myId, uid);
        List<Message> messages = MessageService.getUnreadMessage(myId, uid, lastReadTime);
        List<NetMessage> netMessages = new ArrayList<>();
        for (Message msg : messages) {
            netMessages.add(new NetMessage(msg));
        }
        return netMessages;
    }

    @PostMapping("/msg/room/{myId}/{rid}")
    public List<NetMessage> getUnreadRoom(@PathVariable int myId,
                                          @PathVariable int rid) {
        Date lastReadTime = MessageService.getRoomLastRead(myId, rid);
        List<Message> messages = MessageService.getUnreadMessage(myId, rid, lastReadTime);
        List<NetMessage> netMessages = new ArrayList<>();
        for (Message msg : messages) {
            netMessages.add(new NetMessage(msg));
        }
        return netMessages;
    }

    @MessageMapping("/single-chat")
    public void singleChat(@Payload Message message) {
        mongoTemplate.save(message);
        String destination = "/uni/chat/" + message.getToId();
        messagingTemplate.convertAndSend(destination, message);
    }

    @MessageMapping("/room-chat")
    public void groupChat(@Payload Message message) {
        mongoTemplate.save(message);
        String destination = "/broad/chat/" + message.getToId();
        messagingTemplate.convertAndSend(destination, message);
    }

    @MessageMapping("/add-friend")
    public void addFriend(@Payload Message message) {
        mongoTemplate.save(message);
        String destination = "/uni/add/" + message.getToId();
        messagingTemplate.convertAndSend(destination, message);
    }
}
