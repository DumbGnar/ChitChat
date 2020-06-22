package com.example.demo.service;

import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.*;

public class MessageService {

    @Autowired
    private static MongoTemplate mongoTemplate;
    

    public static List<ChatInfo> getChatInfoList(int myId) {
        List<ChatInfo> chatInfoList = new ArrayList<>();
        List<UserSetting> userSettingList = mongoTemplate.find(query(where("myId").is(myId)), UserSetting.class,"test_usersetting");
        List<RoomSetting> roomSettingList = mongoTemplate.find(query(where("myId").is(myId)), RoomSetting.class,"test_roomsetting");
        for (UserSetting userSetting : userSettingList) {
            List<Message> messages = getUnreadMessage(userSetting.getMyId(), userSetting.getUid(), userSetting.getLastReadTime());
            if (messages.size() > 0) {
                Message lastMsg = messages.get(messages.size() - 1);
                User user = mongoTemplate.findOne(query(where("uid").is(userSetting.getUid())), User.class,"test_user");
                chatInfoList.add(new ChatInfo(userSetting.getUid(), user.getNickname(), messages.size(),
                        lastMsg.getContent(), lastMsg.getSendTime()));
            }
        }
        for (RoomSetting roomSetting : roomSettingList) {
            List<Message> messages = getUnreadMessage(roomSetting.getMyId(), roomSetting.getRid(), roomSetting.getLastReadTime());
            if (messages.size() > 0) {
                Message lastMsg = messages.get(messages.size() - 1);
                Room room = mongoTemplate.findOne(query(where("rid").is(roomSetting.getRid())), Room.class,"test_roomsetting");
                chatInfoList.add(new ChatInfo(roomSetting.getRid(), room.getRoomname(), messages.size(),
                        lastMsg.getContent(), lastMsg.getSendTime()));
            }
        }
        return chatInfoList;
    }

    public static void readUserMessage(int myId, int uid) {
        mongoTemplate.updateMulti(query(where("myId").is(myId).and("uid").is(uid)),
                new Update().set("lastReadTime", new Date()), UserSetting.class,"test_usersetting");
    }

    public static void readRoomMessage(int myId, int rid) {
        mongoTemplate.updateMulti(query(where("myId").is(myId).and("rid").is(rid)),
                new Update().set("lastReadTime", new Date()), RoomSetting.class,"test_roomsetting");
    }

    public static List<Message> getUnreadMessage(int myId, int id, Date lastReadTime) {
        return mongoTemplate.find(query(where("fromId").is(myId).and("toId").is(id).and("sendTime").gte(lastReadTime))
                        .with(Sort.by("sendTime").ascending()), Message.class,"test_message");
    }

    public static Date getUserLastRead(int myId, int uid) {
        UserSetting userSetting = mongoTemplate.findOne(query(where("myId").is(myId).and("uid").is(uid)),
                UserSetting.class,"test_usersetting");
        if (userSetting == null) {
            return new Date();
        }
        return userSetting.getLastReadTime();
    }

    public static Date getRoomLastRead(int myId, int rid) {
        RoomSetting roomSetting = mongoTemplate.findOne(query(where("myId").is(myId).and("rid").is(rid)),
                RoomSetting.class,"test_roomsetting");
        if (roomSetting == null) {
            return new Date();
        }
        return roomSetting.getLastReadTime();
    }
}
