package com.example.demo.service;

import com.example.demo.dao.SingleDatastore;
import dev.morphia.Datastore;

public class AppService {

    private static final Datastore datastore = SingleDatastore.getInstance();

    public static <T> void save(T entity) {
        datastore.save(entity);
    }
}
