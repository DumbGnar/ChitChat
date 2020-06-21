package com.example.demo.controller;

import com.example.demo.model.ChatInfo;
import com.example.demo.model.Message;
import com.example.demo.model.NetMessage;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MessageController(SimpMessagingTemplate template, MongoTemplate mongoTemplate) {
        this.messagingTemplate = template;
        this.mongoTemplate = mongoTemplate;
    }

    @ResponseBody
    @PostMapping("/msg/chatinfo/{myId}")
    public List<ChatInfo> getChatInfoList(@PathVariable int myId) {
        return MessageService.getChatInfoList(myId);
    }

    @PostMapping("/msg/read/user/{myId}/{uid}")
    public void readUserMessage(@PathVariable int myId,
                                @PathVariable int uid) {
        MessageService.readUserMessage(myId, uid);
    }

    @PostMapping("/msg/read/room/{myId}/{rid}")
    public void readRoomMessage(@PathVariable int myId,
                                @PathVariable int rid) {
        MessageService.readRoomMessage(myId, rid);
    }

    /**
     * 返回用户myId的好友uid发给他的所有未读消息
     */
    @ResponseBody
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

    @ResponseBody
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

    // 主动方发送好友申请
    @MessageMapping("/add-friend")
    public void addFriend(@Payload Message message) {
        mongoTemplate.save(message);
        String destination = "/uni/add/" + message.getToId();
        messagingTemplate.convertAndSend(destination, message);
    }

    // 被动方处理好友申请
    @PostMapping("/verify-friend")
    public void verifyFriend(@Payload Message message) {
        mongoTemplate.save(message);
        String destination = "/uni/add/" + message.getToId();
        messagingTemplate.convertAndSend(destination, message);
        if(message.getContent() == "accept"){
            UserService.becomeFriends(message.getFromId(), message.getToId());
        }
        // content=="reject" 则无数据库操作
    }
}
