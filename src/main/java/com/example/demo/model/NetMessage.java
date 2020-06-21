package com.example.demo.model;

import java.util.Date;

public class NetMessage {

    private int fromId;

    private int toId;

    private Date sendTime;

    /**
     * 辨别记录是文字还是图片
     * 1 文字
     * 2 图片
     */
    private int style;

    private String content;

    public NetMessage(Message message) {
        this.fromId = message.getFromId();
        this.toId = message.getToId();
        this.sendTime = message.getSendTime();
        this.style = message.getStyle();
        this.content = message.getContent();
    }
}
