package com.example.demo.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import com.example.demo.service.UserService;

@Document(collection = "test_user")
public class User {
	@ Id
	private int UID;	//主键
	private int age;	//年龄
	private ArrayList<Integer> blackList;	//黑名单
	private ArrayList<Integer> friendList;	//好友列表
	//private Image headPortrait;	//取消原因：头像存放在一个默认路径，前端通过uid访问返回图像的信息不必放到类属性里
	private String loginID;	//用户名
	private String password;	//用户密码
	private String sex;		//性别
	private String nickname;	//昵称
	private String email;	//邮箱
	private String intro;	//个人简介
	
	
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	// 列表添加一个好友
	public ArrayList<Integer> addOneFriend(int uid){
	    this.friendList.add(Integer.valueOf(uid));
	    return this.friendList;
    }

	//构造方法
	@PersistenceConstructor
	public User(String loginID, String password) {	//至少要提供用户名和密码
		this.age = 0;
		this.blackList = new ArrayList<Integer>();
		this.friendList = new ArrayList<Integer>();
		this.loginID = loginID;
		this.password = password;
		this.sex = "男";
		this.nickname = "小明";
		this.UID = ++UserService.counts;
		this.intro = "这个人很懒，什么也没有留下~\n";
		this.email = null;
	}
	
	//构造方法;虽然类似创建者模式的很好看，但是这样的实体类不能用于mongodb的数据检索
//	@PersistenceConstructor
//	public User(UserInitializer user) {
//		this.age = user.getAge();
//		this.blackList = new ArrayList<Integer>();
//		this.friendList = new ArrayList<Integer>();
//		this.loginID = user.getLoginID();
//		this.password = user.getPassword();
//		this.sex = user.getSex();
//		this.UID = ++UserUtils.counts;
//	}	//修改为以下形式
	
	@PersistenceConstructor
	public User() {
		
	}
	
//	public boolean setUserInfo(UserInitializer user) {
//		try{
//			this.age = user.getAge();
//			this.blackList = new ArrayList<Integer>();
//			this.friendList = new ArrayList<Integer>();
//			this.loginID = user.getLoginID();
//			this.password = user.getPassword();
//			this.sex = user.getSex();
//			this.UID = ++UserService.counts;
//		}catch(Exception e) {
//			return false;
//		}
//
//		return true;
//	}
}
