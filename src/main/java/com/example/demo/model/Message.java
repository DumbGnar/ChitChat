package com.example.demo.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "test_message")
public class Message {

    @Id
    private ObjectId id;

    /**
     * 1: 单聊
     * 2: 群聊
     * 3: 加好友
     * 4: 加群
     * 5: 同意加好友
     * 6: 同意加群
     */
    private int type;

    private int fromId;

    /**
     * 用户或者房间的Id
     */
    private int toId;
 
    private Date sendTime = new Date();

    /**
     * 辨别记录是文字还是图片
     * 1 文字
     * 2 图片
     */
    private int style;

    private String content = "";

    public Message() {
    }

    public Message(NetMessage netMessage) {
        this.type = netMessage.getType();
        this.fromId = netMessage.getFromId();
        this.toId = netMessage.getToId();
        this.sendTime = netMessage.getSendTime();
        this.style = netMessage.getStyle();
        this.content = netMessage.getContent();
    }

    public Message(int type, int fromId, int toId, String content , int style) {
        this.type = type;
        this.fromId = fromId;
        this.toId = toId;
        this.content = content;
        this.style = style;
    }

    public Message(int type, int fromId, int toId) {
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

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public String getContent() {
        return content;
    }

	public int getStyle() {
		return style;
	}
}
