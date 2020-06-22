package com.example.demo.controller;

import com.example.demo.model.ChatInfo;
import com.example.demo.model.Message;
import com.example.demo.model.NetMessage;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息相关接口 ❓
 */
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
     * 获取需要更新的会话列表
     * @param myId 用户uid
     * @return ChatInfo的列表
     */
    @PostMapping("/msg/chatinfo/{myId}")
    public List<ChatInfo> getChatInfoList(@PathVariable int myId) {
        return MessageService.getChatInfoList(myId);
    }

    /**
     *
     * @param myId
     * @param uid
     */
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
     * @param myId
     * @param uid
     * @return
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

    /**
     * 单聊消息
     * @param netMessage
     */
    @PostMapping("/app/single-chat")
    public String singleChat(@RequestBody NetMessage netMessage) {
        mongoTemplate.save(new Message(netMessage));
        String destination = "/uni/chat/" + netMessage.getToId();
        messagingTemplate.convertAndSend(destination, netMessage);
        return "send successfully";
    }

    /**
     * 群聊消息
     * @param netMessage
     */
    @PostMapping("/app/room-chat")
    public String groupChat(@RequestBody NetMessage netMessage) {
        mongoTemplate.save(new Message(netMessage));
        String destination = "/broad/chat/" + netMessage.getToId();
        messagingTemplate.convertAndSend(destination, netMessage);
        return "send successfully";
    }

    /**
     * 加好友<br><br>
     * 1. 将消息保存到数据库。<br><br>
     * 2. 将原消息转发给目标用户。
     * @param netMessage
     */
    @PostMapping("/app/add-friend")
    public void addFriend(@RequestBody NetMessage netMessage) {
        mongoTemplate.save(new Message(netMessage));
        String destination = "/uni/add/" + netMessage.getToId();
        messagingTemplate.convertAndSend(destination, netMessage);
    }

    /**
     * 加群<br><br>
     * 1. 将消息保存到数据库。<br><br>
     * 2. 将原消息转发给目标用户。
     * @param netMessage
     */
    @PostMapping("/app/add-room")
    public void addRoom(@RequestBody NetMessage netMessage) {
        mongoTemplate.save(new Message(netMessage));
        String destination = "/uni/add/" + netMessage.getToId();
        messagingTemplate.convertAndSend(destination, netMessage);
    }

    /**
     * 对加好友的应答<br><br>
     * 1. 将消息保存到数据库。<br><br>
     * 2. 如果同意，两个用户之间建立好友关系。<br><br>
     * 3. 将原消息转发给目标用户。
     * @param netMessage
     */
    @PostMapping("/app/reply-friend")
    public void replyFriend(@RequestBody NetMessage netMessage) {
        mongoTemplate.save(new Message(netMessage));
        if ("true".equals(netMessage.getContent())) {

        }
        String destination = "/uni/reply/" + netMessage.getToId();
        messagingTemplate.convertAndSend(destination, netMessage);
    }

    /**
     * 对加群的应答
     * @param netMessage
     */
    @PostMapping("/app/reply-room")
    public void replyRoom(@RequestBody NetMessage netMessage) {
        mongoTemplate.save(new Message(netMessage));
        String destination = "/uni/reply" + netMessage.getToId();
        messagingTemplate.convertAndSend(destination, netMessage);
    }
}
