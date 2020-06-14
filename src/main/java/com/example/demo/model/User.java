package com.example.demo.model;

import java.util.ArrayList;

import com.example.demo.base.user.UserUtils;
import com.example.demo.base.user.UserInitializer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity
public class User {

    @Id
    private ObjectId id;

    private String username;

    private String password;

    private String email;

    private String nickname;

    /**
     * 状态: 0 离线, 1 在线
     */
    private int status = 0;

    /**
     * 性别: 0 男, 1 女
     */
    private int sex = 0;

    private int age = 0;

    private ArrayList<Integer> blackList = new ArrayList<Integer>();;

    private ArrayList<Integer> friendList = new ArrayList<Integer>();;

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = username;
    }

//    public User(UserInitializer user) {
//        this.age = user.getAge();
//        this.blackList = new ArrayList<Integer>();
//        this.friendList = new ArrayList<Integer>();
//        this.loginID = user.getLoginID();
//        this.password = user.getPassword();
//        this.sex = user.getSex();
//        this.UID = ++UserUtils.counts;
//    }
}
