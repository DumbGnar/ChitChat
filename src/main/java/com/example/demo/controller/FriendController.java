package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.NetMessage;
import com.example.demo.model.RoomSetting;
import com.example.demo.model.UserSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FriendController {

    MongoTemplate mongoTemplate;

    @Autowired
    public FriendController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/users/{myId}/friends/{uid}/messages")
    public List<NetMessage> getFriendMessages(@PathVariable int myId,
                                              @PathVariable int uid) {
        List<Message> messages = mongoTemplate.find(query(where("fromId").is(uid).and("toId").is(myId).and("type").is(1)), Message.class);
        List<NetMessage> netMessages = new ArrayList<>();
        for (Message message : messages) {
            netMessages.add(new NetMessage(message));
        }
        return netMessages;
    }

    @GetMapping("/users/{myId}/rooms/{rid}/messages")
    public List<NetMessage> getRoomMessages(@PathVariable int rid) {
        List<Message> messages = mongoTemplate.find(query(where("toId").is(rid).and("type").is(2)), Message.class);
        List<NetMessage> netMessages = new ArrayList<>();
        for (Message message : messages) {
            netMessages.add(new NetMessage(message));
        }
        return netMessages;
    }

    @PostMapping("/users/{myId}/friends/{uid}/delete")
    public void deleteFriend(@PathVariable int myId,
                             @PathVariable int uid) {
        mongoTemplate.remove(query(where("myId").is(myId).and("uid").is(uid)), UserSetting.class);
        mongoTemplate.remove(query(where("myId").is(uid).and("uid").is(myId)), UserSetting.class);
    }

    @PostMapping("/users/{myId}/rooms/{rid}/delete")
    public void deleteRoom(@PathVariable int myId,
                           @PathVariable int rid) {
        mongoTemplate.remove(query(where("myId").is(myId).and("rid").is(rid)), RoomSetting.class);
    }
}
