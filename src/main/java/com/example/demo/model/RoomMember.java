package com.example.demo.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity
public class RoomMember {

    @Id
    private ObjectId id;

    private int rid;

    private int uid;

    private int lastReadMsgId;
}
