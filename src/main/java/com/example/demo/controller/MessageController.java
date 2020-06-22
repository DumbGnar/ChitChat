package com.example.demo.controller;

<<<<<<< HEAD


import com.example.demo.model.ChatInfo;
import com.example.demo.model.Message;
import com.example.demo.model.NetMessage;
import com.example.demo.service.MessageService;


import com.example.demo.model.*;

=======
import com.example.demo.model.*;
>>>>>>> 20518fadc63b61fd80894af12a37fe6dbb41ac8c
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MessageController(SimpMessagingTemplate template, MongoTemplate mongoTemplate) {
        this.messagingTemplate = template;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 获取myId的会话列表
     * @param myId
     * @return
     */
    @PostMapping("/msg/chatinfo/{myId}")
    public List<ChatInfo> getChatInfoList(@PathVariable int myId) {
        List<ChatInfo> chatInfoList = new ArrayList<>();
        List<UserSetting> userSettingList = mongoTemplate.find(query(where("myId").is(myId)), UserSetting.class);
        for (UserSetting userSetting : userSettingList) {
            List<Message> messages = getUnreadUserMessage(userSetting.getMyId(), userSetting.getUid(), userSetting.getLastReadTime());
            if (messages.size() > 0) {
                Message lastMsg = messages.get(messages.size() - 1);
                User user = mongoTemplate.findOne(query(where("_id").is(userSetting.getUid())), User.class, "user");
                chatInfoList.add(new ChatInfo(1, userSetting.getUid(), user.getNickname(), messages.size(),
                        lastMsg.getContent(), lastMsg.getSendTime()));
            }
        }
        List<RoomSetting> roomSettingList = new ArrayList<>();
        roomSettingList = mongoTemplate.find(query(where("myId").is(myId)), RoomSetting.class);
        for (RoomSetting roomSetting : roomSettingList) {
            List<Message> messages = getUnreadRoomMessage(roomSetting.getMyId(), roomSetting.getRid(), roomSetting.getLastReadTime());
            if (messages.size() > 0) {
                Message lastMsg = messages.get(messages.size() - 1);
                Room room = mongoTemplate.findOne(query(where("_id").is(roomSetting.getRid())), Room.class);
                chatInfoList.add(new ChatInfo(2, roomSetting.getRid(), room.getRoomname(), messages.size(),
                        lastMsg.getContent(), lastMsg.getSendTime()));
            }
        }
        return chatInfoList;
    }

    /**
     * 将uid发给myId的所有消息设为已读
     * @param myId
     * @param uid
     */
    @PostMapping("/msg/read/user/{myId}/{uid}")
    public void readUserMessage(@PathVariable int myId,
                                @PathVariable int uid) {
        mongoTemplate.updateMulti(query(where("myId").is(myId).and("uid").is(uid)),
                new Update().set("lastReadTime", new Date()), UserSetting.class);
    }

    /**
     * 将rid发给myId的所有消息设为已读
     * @param myId
     * @param rid
     */
    @PostMapping("/msg/read/room/{myId}/{rid}")
    public void readRoomMessage(@PathVariable int myId,
                                @PathVariable int rid) {
        mongoTemplate.updateMulti(query(where("myId").is(myId).and("rid").is(rid)),
                new Update().set("lastReadTime", new Date()), RoomSetting.class);
    }

    /**
     * 返回用户myId的好友uid发给他的所有未读消息
     * @param myId 我的uid
     * @param uid 好友的uid
     * @return 所有未读消息
     */
    @PostMapping("/msg/user/{myId}/{uid}")
    public List<NetMessage> getUnreadUser(@PathVariable int myId,
                                          @PathVariable int uid) {
        Date lastReadTime = getUserLastRead(myId, uid);
        List<Message> messages = getUnreadUserMessage(myId, uid, lastReadTime);
        List<NetMessage> netMessages = new ArrayList<>();
        for (Message msg : messages) {
            netMessages.add(new NetMessage(msg));
        }
        return netMessages;
    }

    /**
     * 获取rid发给myId的所有未读消息
     * @param myId
     * @param rid
     * @return
     */
    @PostMapping("/msg/room/{myId}/{rid}")
    public List<NetMessage> getUnreadRoom(@PathVariable int myId,
                                          @PathVariable int rid) {
        Date lastReadTime = getRoomLastRead(myId, rid);
        List<Message> messages = getUnreadRoomMessage(myId, rid, lastReadTime);
        List<NetMessage> netMessages = new ArrayList<>();
        for (Message msg : messages) {
            netMessages.add(new NetMessage(msg));
        }
        return netMessages;
    }

    @PostMapping("/msg/single-chat")
    public void singleChat(@RequestBody Message message) {
        mongoTemplate.save(message);
        String destination = "/uni/chat/" + message.getToId();
        messagingTemplate.convertAndSend(destination, message);
    }

    @PostMapping("/msg/room-chat")
    public void groupChat(@RequestBody Message message) {
        mongoTemplate.save(message);
        String destination = "/broad/chat/" + message.getToId();
        messagingTemplate.convertAndSend(destination, message);
    }

    /**
     * 主动方添加好友
     * @param netMessage 加好友消息
     */
    @PostMapping("/msg/add-friend")
    public void addFriend(@RequestBody NetMessage netMessage) {
        mongoTemplate.save(new Message(netMessage));
        String destination = "/uni/add/" + netMessage.getToId();
        messagingTemplate.convertAndSend(destination, netMessage);
    }

    /**
     * 邀请好友进群
     * @param netMessage 邀请消息
     */
    @PostMapping("/msg/add-room")
    public void addRoom(@RequestBody NetMessage netMessage) {
        mongoTemplate.save(new Message(netMessage));
        String destination = "/uni/add" + netMessage.getToId();
        messagingTemplate.convertAndSend(destination, netMessage);
    }

    /**
     * 被动方处理好友申请
     * @param netMessage 应答消息
     */
    @PostMapping("/msg/verify-friend")
    public void verifyFriend(@RequestBody NetMessage netMessage) {
        mongoTemplate.save(new Message(netMessage));
        if ("true".equals(netMessage.getContent())) {
            mongoTemplate.save(new UserSetting(netMessage.getFromId(), netMessage.getToId()));
            mongoTemplate.save(new UserSetting(netMessage.getToId(), netMessage.getFromId()));
            User user1 = mongoTemplate.findOne(query(where("uid").is(netMessage.getFromId())), User.class);
            User user2 = mongoTemplate.findOne(query(where("uid").is(netMessage.getToId())), User.class);
            user1.getFriendList().add(user2.getUID());
            user2.getFriendList().add(user1.getUID());
            mongoTemplate.save(user1);
            mongoTemplate.save(user2);
        }
        String destination = "/uni/add/" + netMessage.getToId();
        messagingTemplate.convertAndSend(destination, netMessage);
    }

    /**
     * 获取好友id发给用户myId的在lastReadTime之后发送的消息。
     * @param myId 我的ID
     * @param id 好友id
     * @param lastReadTime 上次阅读的时间
     * @return 未读消息列表
     */
    public List<Message> getUnreadUserMessage(int myId, int id, Date lastReadTime) {
        List<Message> messageList = new ArrayList<>();
        messageList = mongoTemplate.find(query(where("type").is(1).and("fromId").is(id).and("toId").is(myId).and("sendTime").gte(lastReadTime))
                .with(Sort.by("sendTime").ascending()), Message.class);
        return messageList;
    }

    /**
     * 获取群id发给用户myId的在lastReadTime之后发送的消息。
     * @param myId 我的ID
     * @param id 群id
     * @param lastReadTime 上次阅读的时间
     * @return 未读消息列表
     */
    public List<Message> getUnreadRoomMessage(int myId, int id, Date lastReadTime) {
        List<Message> messageList = new ArrayList<>();
        messageList = mongoTemplate.find(query(where("type").is(2).and("fromId").is(id).and("toId").is(myId).and("sendTime").gte(lastReadTime))
                .with(Sort.by("sendTime").ascending()), Message.class);
        return messageList;
    }

    /**
     * 获取好友发给我的消息最后阅读的时间
     * @param myId 我的uid
     * @param uid 好友的uid
     * @return 最后阅读的时间
     */
    public Date getUserLastRead(int myId, int uid) {
        UserSetting userSetting = mongoTemplate.findOne(query(where("myId").is(myId).and("uid").is(uid)),
                UserSetting.class);
        if (userSetting == null) {
            return new Date();
        }
        return userSetting.getLastReadTime();
    }

    public Date getRoomLastRead(int myId, int rid) {
        RoomSetting roomSetting = mongoTemplate.findOne(query(where("myId").is(myId).and("rid").is(rid)),
                RoomSetting.class);
        if (roomSetting == null) {
            return new Date();
        }
        return roomSetting.getLastReadTime();
    }
}
