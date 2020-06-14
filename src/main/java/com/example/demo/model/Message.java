package com.example.demo.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.Date;

@Entity
public class Message {

    @Id
    private ObjectId id;

    public static final int SINGLE_CHAT = 1;
    public static final int GROUP_CHAT = 2;
    public static final int ADD_FRIEND = 3;
    public static final int DEL_FRIEND = 4;
    public static final int BLACK_FRIEND = 5;

    /**
     * 类型1: 单聊
     * 类型2: 群聊
     * 类型3: 添加好友
     * 类型4: 删除好友
     * 类型5: 拉黑好友
     * ...
     */
    private int type;

    private String fromId;

    private String toId;

    private Date sendTime = new Date();

    private String content = "";

    public Message(int type, String fromId, String toId, String content) {
        this.type = type;
        this.fromId = fromId;
        this.toId = toId;
        this.content = content;
    }

    public Message(int type, String fromId, String toId) {
        this.type = type;
        this.fromId = fromId;
        this.toId = toId;
    }

    public ObjectId getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public String getContent() {
        return content;
    }
}
