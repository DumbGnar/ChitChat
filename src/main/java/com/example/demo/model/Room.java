package com.example.demo.model;
import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Room {
    @Id
    private int Rid;//房间号
    
    private ArrayList<Integer> Allusers;//所有聊天室的成员id
    private ArrayList<Integer> Freeusers;//免打扰的成员id
    private String Roomname;//房间昵称
    private ArrayList<String> Announcement;//房间公告
    private ArrayList<Message> Messages;//消息

    public Room() 
    {
    	
    }
    public Room(String name,int rid) {
    	
    	this.Allusers = new ArrayList<Integer>();
    	this.Freeusers = new ArrayList<Integer>();
    	this.Announcement = new ArrayList<String>();
    	this.Messages = new ArrayList<Message>();

        this.Roomname = name;
        this.Rid = rid;
       
        
    }
    
    public void setRid(int rid) {
		this.Rid = rid;
	}
    public int getRid() {
        return this.Rid;
    }

    public void addAllusers(int id) {
        this.Allusers.add(Integer.valueOf(id));
    }

    public ArrayList<Integer> deleteoneuser(int id) {
        this.Allusers.remove(Integer.valueOf(id));
        return this.Allusers;
    }

    public ArrayList<Integer> addoneuser(int id) {
        this.Allusers.add(Integer.valueOf(id));
        return this.Allusers;
    }

    public ArrayList<Integer> getAllusers() {
        return this.Allusers;

    }

    public ArrayList<Integer> getFreeusers() {
        return this.Freeusers;

    }

    public ArrayList<Integer> addoneFreeuser(int id) {
        this.Freeusers.add(Integer.valueOf(id));
        return this.Freeusers;
    }

    public ArrayList<Integer> deleteoneFreeuser(int id) {
        this.Freeusers.remove(Integer.valueOf(id));
        return this.Freeusers;
    }

    public String setRoomname(String roomname) {
        this.Roomname = roomname;
        return this.Roomname;
    }

    public String getRoomname() {
        return this.Roomname;
    }

    public ArrayList<String> getAnnouncement() {
        return Announcement;
    }

    public ArrayList<String> addAnnouncement(String Announcement) {
        this.Announcement.add(Announcement);
        return this.Announcement;
    }

    public ArrayList<Message> getMessages() {
        return Messages;
    }

    public ArrayList<Message> addMessages(int type, int fromId, int toId, String content, int style) {
        Message newmessage = new Message(2, fromId, toId, content, style);
        this.Messages.add(newmessage);
        return this.Messages;
    }
}

