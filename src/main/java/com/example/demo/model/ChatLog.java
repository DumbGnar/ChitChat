package com.example.demo.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.Date;

@Entity
public class ChatLog {

    @Id
    private ObjectId id;

    public static final int PRIVATE_CHAT = 1;
    public static final int GROUP_CHAT = 2;
    /**
     * 类型1: 私聊
     * 类型2: 群聊
     */
    private int type;

    private String fromUserName;
    private String toUserName;
    private String toGroupName;
    private Date sendTime;
    private String content = "";

    // toUserName, toGroupName 其中一个为空
    public ChatLog(int type, String fromUserName, String toUserName, String toGroupName, String content) {
        this.type = type;
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.toGroupName = toGroupName;
        this.sendTime = new Date();
        this.content = content;
    }
}
