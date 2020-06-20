package com.example.demo.service;

import com.example.demo.model.Message;
import com.example.demo.model.RoomSetting;
import com.example.demo.model.UserSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Date;
import java.util.List;

public class MessageService {

    @Autowired
    private static MongoTemplate mongoTemplate;

    public static void readUserMessage(int myId, int uid) {
        mongoTemplate.updateMulti(query(where("myId").is(myId).and("uid").is(uid)),
                new Update().set("lastReadTime", new Date()), UserSetting.class);
    }

    public static void readRoomMessage(int myId, int rid) {
        mongoTemplate.updateMulti(query(where("myId").is(myId).and("rid").is(rid)),
                new Update().set("lastReadTime", new Date()), RoomSetting.class);
    }

    public static List<Message> getUnreadMessage(int myId, int id, Date lastReadTime) {
        return mongoTemplate.find(query(where("fromId").is(myId).and("toId").is(id).and("sendTime").gte(lastReadTime)),
                Message.class);
    }

    public static Date getUserLastRead(int myId, int uid) {
        UserSetting userSetting = mongoTemplate.findOne(query(where("myId").is(myId).and("uid").is(uid)),
                UserSetting.class);
        if (userSetting == null) {
            return new Date();
        }
        return userSetting.getLastReadTime();
    }

    public static Date getRoomLastRead(int myId, int rid) {
        RoomSetting roomSetting = mongoTemplate.findOne(query(where("myId").is(myId).and("rid").is(rid)),
                RoomSetting.class);
        if (roomSetting == null) {
            return new Date();
        }
        return roomSetting.getLastReadTime();
    }
}
