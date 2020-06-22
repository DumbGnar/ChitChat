package com.example.demo.model;

import java.util.Date;

public class ChatInfo {

    /**
     * 1 用户，2 群
     */
    private int type;

    /**
     * 用户或群的id
     */
    private int id;

    /**
     * 用户昵称或群名
     */
    private String name;

    /**
     * 未读消息数
     */
    private int unreadNumber;

    /**
     * 最后一条消息的内容
     */
    private String latestContent;

    /**
     * 最后一条消息的发送时间
     */
    private Date latestTime;

    public ChatInfo(int type, int id, String name, int unreadNumber, String latestContent, Date latestTime) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.unreadNumber = unreadNumber;
        this.latestContent = latestContent;
        this.latestTime = latestTime;
    }
}
