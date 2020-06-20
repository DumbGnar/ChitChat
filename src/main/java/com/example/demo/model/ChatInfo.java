package com.example.demo.model;

import java.util.Date;

public class ChatInfo {

    private int id;

    private String name;

    private int unreadNumber;

    private String latestContent;

    private Date latestTime;

    public ChatInfo(int id, String name, int unreadNumber, String latestContent, Date latestTime) {
        this.id = id;
        this.name = name;
        this.unreadNumber = unreadNumber;
        this.latestContent = latestContent;
        this.latestTime = latestTime;
    }
}
