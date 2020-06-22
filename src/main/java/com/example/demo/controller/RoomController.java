package com.example.demo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Message;
import com.example.demo.model.Room;
import com.example.demo.model.User;

/**
 * 群相关接口 ❌
 */
@RestController


public class RoomController{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	//表的名字
	private String collection_name = "test_room";
	
	
	//创建房间接口
	@RequestMapping("room/add/{uid}/{roomname}")//创建完,返回创建者的id
	public int addRoom(@PathVariable(value = "uid")int uid,@PathVariable(value = "roomname")String Roomname){
		int max_rid = 0;
		int max_index = this.mongoTemplate.findAll(Room.class, collection_name).size()-1;//找到最后一条记录下标
		if(max_index >= 0)
		{
		max_rid = this.mongoTemplate.findAll(Room.class, collection_name).get(max_index).getRid()+1;//获取最后一条记录的rid++
		}	
		int rid  = addRoomProduce(uid,Roomname,max_rid);	
		return rid;
	}
	private int addRoomProduce(int uid,String name,int max_rid){		
	Room tobeAdded = new Room(name,max_rid);
	tobeAdded.addAllusers(uid);
	mongoTemplate.insert(tobeAdded,this.collection_name);
	return tobeAdded.getRid();
	}
	
	
	//删除房间接口
	@RequestMapping("room/delete/{rid}")//前端控制删除者
	public boolean deleteRoom(@PathVariable(value = "rid")int rid) {
		try {
			deleteroomProduce(rid);
		}catch(Exception e) {
			System.out.println("ROOM DELETE ERROR\n");
			return false;
		}
		return true;
	}
	private void deleteroomProduce(int rid) throws Exception{
		//判断是否存在该房间
		Room tobeDeleted = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(rid)), Room.class, collection_name);
		if(tobeDeleted == null) 
		{
			throw new Exception();
		}
		
		this.mongoTemplate.remove(new Query(Criteria.where("_id").is(rid)), Room.class, collection_name);
		//image cache folder not deleted with user
	}
	
	
	//返回房间内历史信息的接口
	@RequestMapping("room/viewmessages/{rid}")//返回Message ArrayList
	public ArrayList<Message> viewmessages(@PathVariable(value = "rid")int rid){
		
		Query query = new Query(Criteria.where("_id").is(rid));
		
		return this.mongoTemplate.findOne(query, Room.class, this.collection_name).getMessages();
	}
	
	//发送信息接口(只做到了将消息更新到了消息记录，并未实现消息的发送）
	@RequestMapping("room/sendmessages/{rid}/{uid}/{style}/{content}")//返回Message ArrayList
	public boolean sendmessages(@PathVariable(value = "rid")int rid,
			@PathVariable(value = "uid")int uid,
			@PathVariable(value = "style")int style,
			@PathVariable(value = "content")String content) {
		try {
			sendmessageProduce(rid,uid,style,content);;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("SEND ERROR\n");
		}
		return true;		
	}
	private void sendmessageProduce(int rid,int uid,int style,String content) throws Exception{
		//判断是否存在该房间
		Room sended = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(rid)), Room.class, this.collection_name);
		if(sended == null) throw new Exception();
		
		Query query = new Query(Criteria.where("_id").is(rid));
		//将信息添加进消息记录去
		Update update = new Update();
        update.set("Messages",sended.addMessages(2, uid,rid, content, style));
		this.mongoTemplate.updateFirst(query, update, Room.class, this.collection_name);
	}
	
	//查看公告接口
	@RequestMapping("room/viewannouncement/{rid}")//返回uid ArrayList
	public ArrayList<String> announcement(@PathVariable(value = "rid")int rid){
		
		Query query = new Query(Criteria.where("_id").is(rid));
		
		return this.mongoTemplate.findOne(query, Room.class, this.collection_name).getAnnouncement();
	}
		
	//添加公告接口
	@RequestMapping("room/addannouncement/{rid}/{announcement}")//返回uid ArrayList
	public boolean addannouncement(@PathVariable(value = "rid")int rid,@PathVariable(value = "announcement")String announcement){
		
		try {
			addannouncementProduce(rid,announcement);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ADD ANNOUNCEMENT ERROR\n");
		}
		return true;
	} 
	private void addannouncementProduce(int rid,String announcement) throws Exception{
		//判断是否存在该房间
		Room added = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(rid)), Room.class, this.collection_name);
		if(added== null) throw new Exception();
		
		Query query = new Query(Criteria.where("_id").is(rid));

		Update update = new Update();
		update.set("Announcement",added.addAnnouncement(announcement));
		this.mongoTemplate.updateFirst(query, update, Room.class, this.collection_name);
	}
	

	//查看群昵称接口
	@RequestMapping("room/viewroomname/{rid}")//返回String Roomname
	public String viewroomname(@PathVariable(value = "rid")int rid){
		
		Query query = new Query(Criteria.where("_id").is(rid));
		
		return this.mongoTemplate.findOne(query, Room.class, this.collection_name).getRoomname();
		
		
	}
	//设置群昵称接口
	@RequestMapping("room/setroomname/{rid}/{name}")//返回uid ArrayList
	public boolean setroomname(@PathVariable(value = "rid")int rid,@PathVariable(value = "name")String name){
		
		Query query = new Query(Criteria.where("_id").is(rid));
		Update update = new Update();
        update.set("Roomname",name);
		this.mongoTemplate.updateFirst(query, update, Room.class, this.collection_name);
		
		return true;
	}
	
	
	
	//查看消息免打扰成员；
	@RequestMapping("room/viewfreeusers/{rid}")//返回uid ArrayList
	public ArrayList<Integer> viewfreeusers(@PathVariable(value = "rid")int rid) {

		Query query = new Query(Criteria.where("_id").is(rid));

		return mongoTemplate.findOne(query, Room.class,this.collection_name).getFreeusers();
	}
	
	
	//消息免打扰接口
	@RequestMapping("room/addfree/{rid}/{uid}")
	public boolean addRoomfreeuser(@PathVariable(value = "rid")int rid,@PathVariable(value = "uid")int uid){
		 try {
				addroomfreeuserProduce(rid,uid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("ADDUSER ERROR\n");
			}
			return true;
	} 
	private void addroomfreeuserProduce(int rid,int uid) throws Exception{
		//判断是否存在该房间
		Room added = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(rid)), Room.class, this.collection_name);
		if(added == null) throw new Exception();
		
		Query query = new Query(Criteria.where("_id").is(rid));
	
		Update update = new Update();
        update.set("Freeusers",added.addoneFreeuser(Integer.valueOf(uid)));
		this.mongoTemplate.updateFirst(query, update, Room.class, this.collection_name);
	}
	
	//取消 消息免打扰接口
		@RequestMapping("room/deletefree/{rid}/{uid}")
		public boolean deleteRoomfreeuser(@PathVariable(value = "rid")int rid,@PathVariable(value = "uid")int uid){
			 try {
					deleteroomfreeuserProduce(rid,uid);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("UNDO FREE ERROR\n");
				}
				return true;
		} 
		private void deleteroomfreeuserProduce(int rid,int uid) throws Exception{
			//判断是否存在该房间
			Room deleted = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(rid)), Room.class, this.collection_name);
			if(deleted == null) throw new Exception();
			
			Query query = new Query(Criteria.where("_id").is(rid));
		
			Update update = new Update();
	        update.set("Freeusers",deleted.deleteoneFreeuser(Integer.valueOf(uid)));
			this.mongoTemplate.updateFirst(query, update, Room.class, this.collection_name);
		}
	
		
	//查看房间成员接口
	@RequestMapping("room/viewusers/{rid}")//返回uid ArrayList
	public ArrayList<Integer> viewnomalusers(@PathVariable(value = "rid")int rid) {

		Query query = new Query(Criteria.where("_id").is(rid));

		return mongoTemplate.findOne(query, Room.class,this.collection_name).getAllusers();
	}
	
	
	//添加成员接口
	@RequestMapping("room/adduser/{rid}/{uid}")
	public boolean addRoomuser(@PathVariable(value = "rid")int rid,@PathVariable(value = "uid")int uid){
		 try {
				addroomuserProduce(rid,uid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("ADDUSER ERROR\n");
			}
			return true;
	} 
	private void addroomuserProduce(int rid,int uid) throws Exception{
		//判断是否存在该房间
		Room added = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(rid)), Room.class, this.collection_name);
		if(added== null) throw new Exception();
		
		Query query = new Query(Criteria.where("_id").is(rid));
	
		Update update = new Update();
        update.set("Allusers",added.addoneuser(Integer.valueOf(uid)));
		this.mongoTemplate.updateFirst(query, update, Room.class, this.collection_name);
	}
	
	
	//删除成员接口
	@RequestMapping("room/deleteuser/{rid}/{uid}")
	public boolean deleteRoomuser(@PathVariable(value = "rid")int rid,@PathVariable(value = "uid")int uid)  {

		 try {
			deleteroomuserProduce(rid,uid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("DELETEUSER ERROR\n");
		}
		return true;
	}
	private void deleteroomuserProduce(int rid,int uid) throws Exception{
		//判断是否存在该房间
		Room tobeDeleted = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(rid)), Room.class, this.collection_name);
		if(tobeDeleted == null) throw new Exception();
		
		Query query = new Query(Criteria.where("_id").is(rid));
	
		Update update = new Update();
        update.set("Allusers",tobeDeleted.deleteoneuser(Integer.valueOf(uid)));
		this.mongoTemplate.updateFirst(query, update, User.class, this.collection_name);
	}

	 



}





