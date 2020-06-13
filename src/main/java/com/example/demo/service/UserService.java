package com.example.demo.service;

import com.example.demo.dao.SingleDatastore;
import com.example.demo.model.User;
import dev.morphia.Datastore;
import dev.morphia.query.Query;

import java.util.List;

import static dev.morphia.query.experimental.filters.Filters.and;
import static dev.morphia.query.experimental.filters.Filters.eq;

public class UserService {

    private static final Datastore datastore = SingleDatastore.getInstance();

    public static boolean checkLogin(String username, String password) {
        Query<User> query = datastore.find(User.class)
                .filter(and(eq("username", username), eq("password", password)));
        List<User> users = query.iterator().toList();
        return users.size() > 0;
    }

    public static boolean checkUsername(String username) {
        Query<User> query = datastore.find(User.class)
                .filter(eq("username", username));
        List<User> users = query.iterator().toList();
        return users.size() == 0;
    }

    public static <T> void save(T entity) {
        datastore.save(entity);
    }
}
