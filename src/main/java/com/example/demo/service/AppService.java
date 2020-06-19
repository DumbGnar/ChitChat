package com.example.demo.service;

import com.example.demo.dao.SingleDatastore;
import com.example.demo.model.Message;
import dev.morphia.Datastore;

import java.util.List;

public class AppService {

    private static final Datastore datastore = SingleDatastore.getInstance();

    public static <T> void save(T entity) {
        datastore.save(entity);
    }

    public static List<Message> getUnreadSingle(int myId, int friendId) {
        return null;
    }

    public static List<Message> getUnreadRoom(int uid, int gid) {
        return null;
    }
}
