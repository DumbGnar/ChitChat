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

    private int fromId;

    private int toId;//用户或者房间Id
 
    private Date sendTime = new Date();
    
    private int style; //辨别记录是文字还是图片,1文字2图片;
    
    private int read; // 已读未读,1已读,2未读;

    private String content = "";

    public Message(int type, int fromId, int toId, String content ,int style) {
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

	public int getRead() {
		return read;
	}

}
