package com.example.demo.controller;

import com.example.demo.model.ChatInfo;
import com.example.demo.model.Message;
import com.example.demo.model.NetMessage;
import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/msg/delete")
    public void deleteMessage(@RequestBody NetMessage netMessage) {
        mongoTemplate.remove(query(where("fromId").is(netMessage.getFromId()).and("toId").is(netMessage.getToId())
                .and("type").is(netMessage.getType())), Message.class);
    }

    /**
     * 返回所有用户ID为myId的type=3/4/5的消息
     * type=3 加好友申请
     * type=4 加群申请
     * type=5 加好友应答
     * @param myId Message中的toId
     * @return NetMessage List
     */
    @RequestMapping("/msg/addinfo/{myId}")
    public List<NetMessage> getAddInfoMessage(@PathVariable int myId) {
        List<NetMessage> netMessages = new ArrayList<>();
        List<Message> addUserList = mongoTemplate.find(query(where("toId").is(myId).and("type").is(3)), Message.class);
        List<Message> addRoomList = mongoTemplate.find(query(where("toId").is(myId).and("type").is(4)), Message.class);
        List<Message> verifyUserList = mongoTemplate.find(query(where("toId").is(myId).and("type").is(5)), Message.class);
        for (Message message : addUserList) {
            netMessages.add(new NetMessage(message));
        }
        for (Message message : addRoomList) {
            netMessages.add(new NetMessage(message));
        }
        for (Message message : verifyUserList) {
            netMessages.add(new NetMessage(message));
        }
        return netMessages;
    }

    /**
     * 获取myId的会话列表
     * @param myId 用户uid
     * @return ChatInfo列表
     */
    @RequestMapping("/msg/chatinfo/{myId}")
    public List<ChatInfo> getChatInfoList(@PathVariable int myId) {
        List<ChatInfo> chatInfoList = new ArrayList<>();
        List<UserSetting> userSettingList = mongoTemplate.find(query(where("myId").is(myId)), UserSetting.class);
        for (UserSetting userSetting : userSettingList) {
            List<Message> messages = getUnreadUserMessage(userSetting.getMyId(), userSetting.getUid(), userSetting.getLastReadTime());
            if (messages.size() > 0) {
                Message lastMsg = messages.get(messages.size() - 1);
                User user = mongoTemplate.findOne(query(where("_id").is(userSetting.getUid())), User.class, "user");
                if (user == null) {
                    continue;
                }
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
                if (room == null) {
                    continue;
                }
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

    /**
     * 发送单聊消息
     * @param netMessage 消息类
     * @return 发送时间
     */
    @PostMapping("/msg/single-chat")
    public Date singleChat(@RequestBody NetMessage netMessage) {
        Message message = new Message(netMessage);
        mongoTemplate.save(message);
        String destination = "/uni/chat/" + netMessage.getToId();
        messagingTemplate.convertAndSend(destination, netMessage);
        return message.getSendTime();
    }

    /**
     * 发送群聊消息
     * @param netMessage 消息类
     * @return 发送时间
     */
    @PostMapping("/msg/room-chat")
    public Date groupChat(@RequestBody NetMessage netMessage) {
        Message message = new Message(netMessage);
        mongoTemplate.save(message);
        String destination = "/broad/chat/" + netMessage.getToId();
        messagingTemplate.convertAndSend(destination, netMessage);
        return message.getSendTime();
    }

    /**
     * 主动方添加好友
     * @param netMessage 加好友消息
     */
    @PostMapping("/msg/add-friend")
    public void addFriend(@RequestBody NetMessage netMessage) {
        UserSetting userSetting = mongoTemplate.findOne(query(where("myId").is(netMessage.getFromId())
                .and("uid").is(netMessage.getToId())), UserSetting.class);
        if (userSetting != null) {
            return;
        }
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
        RoomSetting roomSetting = mongoTemplate.findOne(query(where("myId").is(netMessage.getToId())
                .and("rid").is(Integer.parseInt(netMessage.getContent()))), RoomSetting.class);
        if (roomSetting != null) {
            return;
        }
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
        UserSetting userSetting = mongoTemplate.findOne(query(where("myId").is(netMessage.getFromId())
                .and("uid").is(netMessage.getToId())), UserSetting.class);
        if (userSetting == null && "true".equals(netMessage.getContent())) {
            mongoTemplate.save(new UserSetting(netMessage.getFromId(), netMessage.getToId()));
            mongoTemplate.save(new UserSetting(netMessage.getToId(), netMessage.getFromId()));
            User user1 = mongoTemplate.findOne(query(where("_id").is(netMessage.getFromId())), User.class);
            User user2 = mongoTemplate.findOne(query(where("_id").is(netMessage.getToId())), User.class);
            user1.getFriendList().add(user2.getUID());
            user2.getFriendList().add(user1.getUID());
            mongoTemplate.save(user1);
            mongoTemplate.save(user2);
        }
        String destination = "/uni/add/" + netMessage.getToId();
        messagingTemplate.convertAndSend(destination, netMessage);
        mongoTemplate.remove(query(where("fromId").is(netMessage.getToId()).and("toId").is(netMessage.getFromId())
                .and("type").is(3)), Message.class);
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
