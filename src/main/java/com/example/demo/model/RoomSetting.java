package com.example.demo.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class RoomSetting {

    @Id
    private ObjectId id;

    private int myId;

    private int rid;

    private Date lastReadTime = new Date();

    public RoomSetting(int myId, int rid) {
        this.myId = myId;
        this.rid = rid;
    }

    public ObjectId getId() {
        return id;
    }

    public int getMyId() {
        return myId;
    }

    public int getRid() {
        return rid;
    }

    public Date getLastReadTime() {
        return lastReadTime;
    }
}
