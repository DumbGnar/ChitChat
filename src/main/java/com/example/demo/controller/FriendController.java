package com.example.demo.controller;

import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.data.mongodb.core.query.Criteria;
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

    @GetMapping("/users/{myId}/friends")
    public List<User> getFriends(@PathVariable int myId) {
        List<UserSetting> userSettings = mongoTemplate.find(query(where("myId").is(myId)), UserSetting.class);
        List<User> users = new ArrayList<>();
        for (UserSetting userSetting : userSettings) {
            User user = mongoTemplate.findOne(query(where("_id").is(userSetting.getUid())), User.class);
            users.add(user);
        }
        return users;
    }

    @GetMapping("/users/{myId}/rooms")
    public List<Room> getRooms(@PathVariable int myId) {
        List<RoomSetting> roomSettings = mongoTemplate.find(query(where("myId").is(myId)), RoomSetting.class);
        List<Room> rooms = new ArrayList<>();
        for (RoomSetting roomSetting : roomSettings) {
            Room room = mongoTemplate.findOne(query(where("_id").is(roomSetting.getRid())), Room.class);
            rooms.add(room);
        }
        return rooms;
    }

    @GetMapping("/users/{myId}/friends/{uid}/messages")
    public List<NetMessage> getFriendMessages(@PathVariable int myId,
                                              @PathVariable int uid) {
        List<Message> messages = mongoTemplate.find(query(where("type").is(1)
                .andOperator(new Criteria().orOperator(
                        where("fromId").is(myId).and("toId").is(uid),
                        where("fromId").is(uid).and("toId").is(myId))))
                .with(Sort.by("sendTime").ascending()), Message.class);
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
