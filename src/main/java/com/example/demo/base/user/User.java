package com.example.demo.base.user;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.base.user.UserUtils;
import com.example.demo.base.user.UserInitializer;

public class User {
	private int age;	//年龄
	private ArrayList<Integer> blackList;	//黑名单
	private ArrayList<Integer> friendList;	//好友列表
	//private Image headPortrait;	//取消原因：头像存放在一个默认路径，前端通过uid访问返回图像的信息不必放到类属性里
	private String loginID;	//用户名
	private String password;	//用户密码
	private String sex;		//性别
	private int UID;	//主键
	
	//Generate Automatically
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
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
	public String getLoginID() {
		return loginID;
	}
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getUID() {
		return UID;
	}
	public void setUID(int uID) {
		UID = uID;
	}
	
	//构造方法
	public User(String loginID, String password) {	//至少要提供用户名和密码
		this.age = 0;
		this.blackList = new ArrayList<Integer>();
		this.friendList = new ArrayList<Integer>();
		this.loginID = loginID;
		this.password = password;
		this.sex = "男";
		this.UID = ++UserUtils.counts;
	}
	
	//构造方法
	public User(UserInitializer user) {
		this.age = user.getAge();
		this.blackList = new ArrayList<Integer>();
		this.friendList = new ArrayList<Integer>();
		this.loginID = user.getLoginID();
		this.password = user.getPassword();
		this.sex = user.getSex();
		this.UID = ++UserUtils.counts;
	}
}
