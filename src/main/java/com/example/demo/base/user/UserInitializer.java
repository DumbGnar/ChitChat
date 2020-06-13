package com.example.demo.base.user;

import java.util.ArrayList;

//用于初始化User实例的类
public class UserInitializer {

    private int age = 0;    //年龄

    private String loginID;    //用户名

    private String password;    //用户密码

    private String sex = "男";        //性别

    //构造方法
    public UserInitializer(int age, String loginID, String password, String sex) {
        this.age = (age >= 0) ? age : this.age;
        this.loginID = loginID;
        this.password = password;
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public String getLoginID() {
        return loginID;
    }

    public String getPassword() {
        return password;
    }

    public String getSex() {
        return sex;
    }
}
