package com.example.demo.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.Date;

@Entity
public class Message {

    @Id
    private ObjectId id;

    /**
     * 1: 单聊
     * 2: 群聊
     * 3: 添加好友
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

    /**
     *  1 已读
     *  2 未读
     */
    private int read = 2;

    private String content = "";

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

	public int getRead() {
		return read;
	}

}
