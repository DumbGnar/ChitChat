package com.example.demo.model;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import dev.morphia.annotations.Entity;

@Entity
public class User {

    @Id
    private int Uid;

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

    private String intro = "这个人很懒，什么也没有留下~";

    private ArrayList<Integer> blackList = new ArrayList<Integer>();;

    private ArrayList<Integer> friendList = new ArrayList<Integer>();;

    public User() {
    }
    public int  getUid()
    {
    	return Uid;
    }
    public User(String username, String password, String email) {
		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
		this.setNickname(username);
	}

	public ArrayList<Integer> getBlackList() {
		return blackList;
	}

	public void setBlackList(ArrayList<Integer> blackList) {
		this.blackList = blackList;
	}

	public ArrayList<Integer> getFriendList() {
		return friendList;
	}

	public void setFriendList(ArrayList<Integer> friendList) {
		this.friendList = friendList;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
