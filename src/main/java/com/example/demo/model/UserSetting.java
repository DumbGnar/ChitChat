package com.example.demo.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class UserSetting {

    @Id
    private ObjectId id;

    private int myId;

    private int uid;

    private Date lastReadTime;

    private boolean blacked;

    public UserSetting() {
    }

    public UserSetting(int myId, int uid) {
        this.myId = myId;
        this.uid = uid;
        this.lastReadTime = new Date();
        this.blacked = false;
    }

    public UserSetting(ObjectId id, int myId, int uid, Date lastReadTime, boolean blacked) {
        this.id = id;
        this.myId = myId;
        this.uid = uid;
        this.lastReadTime = lastReadTime;
        this.blacked = blacked;
    }

    public ObjectId getId() {
        return id;
    }

    public int getMyId() {
        return myId;
    }

    public int getUid() {
        return uid;
    }

    public Date getLastReadTime() {
        return lastReadTime;
    }

    public boolean isBlacked() {
        return blacked;
    }
}
