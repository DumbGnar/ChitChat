package com.example.demo.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	//进行检索的位于数据库mongoTemplate中的集合名称，测试用的是test_user，正式使用未定
	private String collection_name = "test_user";
	
	
	//根据用户UID获取用户信息
	@RequestMapping("user/{uid}")
	public User getUserByID(@PathVariable(value = "uid")int id) {
		//数据库检索
		User res = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), User.class, this.collection_name);
		return res;	
	}
	
	
	//根据用户名loginID获取用户信息
	@RequestMapping("user/searchByLoginID/{loginID}")
	public User searchByLoginID(@PathVariable(value = "loginID")String loginID) {
		//数据库检索
		User res = this.mongoTemplate.findOne(new Query(Criteria.where("loginID").is(loginID)), User.class, this.collection_name);
		return res;
	}
	
	
	//根据用户UID获取用户头像图片,这个方法已完成
	//现在前端用的URL方式访问图片资源，此接口暂不需要
	@RequestMapping("user/headportrait/{uid}")
	public BufferedImage getUserHeadPortraitByID(@PathVariable(value = "uid")int id) {
		return UserService.getHeadPortraitByID(id);
	}
	
	
	//把某个用户的头像设置成默认头像
	@RequestMapping("user/headportrait/setto/{uid}/{num}")
	public boolean setUserHeadPortraitByID(@PathVariable(value = "uid")Integer id,
			@PathVariable(value = "num")Integer num) {
		return UserService.setUserToDefaultHeadtrait(id, num);
	}
	
	
	//（登录用）验证用户名和密码是否匹配，匹配成功返回用户
	@RequestMapping("/user/login/{loginID}/{password}")
	public User userLogin(@PathVariable(value = "loginID")String loginID,
			@PathVariable(value = "password")String password) {
		//数据库检索
		User res = this.mongoTemplate.findOne(new Query(Criteria.where("loginID").is(loginID).and("password").is(password)), User.class, this.collection_name);
		return res;
	}
	
	
	//修改用户的某项信息
	/*
	 * method —— 参数详细
	 * 1:age		年龄
	 * 2:loginID	账号
	 * 3:password	密码
	 * 4:sex		性别
	 * 5.headportrait 头像
	 * 6.nickname	昵称
	 * 7.intro		简介
	 * 8.email		邮箱
	 * */
	@RequestMapping("/user/changeInfo/{uid}/{method}/{content}")
	public boolean userChangeInfo(@PathVariable(value = "uid")Integer uid,
			@PathVariable(value = "method")Integer method,
			@PathVariable(value = "content")String content) {
		//数据的合理性——如账号必须有数字和字母组成，由前端来保证
		Query query = new Query(Criteria.where("_id").is(uid));
		Update update = new Update();
		switch(method) {
		case 1:
			//年龄修改
			update.set("age", new Integer(content));
			this.mongoTemplate.updateFirst(query, update, User.class, this.collection_name);
			return true;
		case 2:
			//账号修改（需要由前端判断是否重复）
			update.set("loginID", content);
			this.mongoTemplate.updateFirst(query, update, User.class, this.collection_name);
			return true;
		case 3:
			//密码修改
			update.set("password", content);
			this.mongoTemplate.updateFirst(query, update, User.class, this.collection_name);
			return true;
		case 4:
			//性别修改
			update.set("sex", content);
			this.mongoTemplate.updateFirst(query, update, User.class,this.collection_name);
			return true;
		case 5:
			//头像修改(未完成)
			return true;
		case 6:
			//昵称修改
			update.set("nickname", content);
			this.mongoTemplate.updateFirst(query, update, User.class, this.collection_name);
			return true;
		case 7:
			//简介修改
			update.set("intro", content);
			this.mongoTemplate.updateFirst(query, update, User.class, this.collection_name);
			return true;
		case 8:
			//邮箱修改
			update.set("email", content);
			this.mongoTemplate.updateFirst(query, update, User.class, this.collection_name);
			return true;
			
		}
		return false;
	}
	
	
	//添加新用户
	@RequestMapping("/user/add/{loginID}/{password}")
	public boolean addUser(@PathVariable(value = "loginID")String loginID, 
			@PathVariable(value = "password")String password) {
		try {
			adduserProduce(loginID, password);
		}catch(Exception e) {
			System.out.println("USER ADD ERROR\n");
			return false;
		}
		return true;
	}
	private void adduserProduce(String loginID, String password) throws Exception{
		User tobeAdded = new User(loginID, password);
		//image cache folder
		File base = new File(UserService.baseUserImagePath + "//" + tobeAdded.getUID());
		if(base.exists()) throw new Exception();
		else base.mkdir();
		/*
		 * 6月20日补充
		 * 现在伴随创建imagecache和faces
		 * */
		File faces = new File(UserService.baseUserImagePath + "//" + tobeAdded.getUID() + "//faces");
		if(!faces.exists()) faces.mkdir();
		File imagecache = new File(UserService.baseUserImagePath + "//" + tobeAdded.getUID() + "//imagecache");
		if(!imagecache.exists()) imagecache.mkdir();
		//create a default head.jpg in folder
		UserService.setUserToDefaultHeadtrait(tobeAdded.getUID(), UserService.HEAD_DEFAULT);
		//user collection 
		this.mongoTemplate.insert(tobeAdded);
	}
	
	
	//删除已有用户
	@RequestMapping("/user/delete/{uid}")
	public boolean deleteUser(@PathVariable(value = "uid")Integer uid) {
		try {
			deleteuserProduce(uid);
		}catch(Exception e) {
			System.out.println("USER DELETE ERROR\n");
			return false;
		}
		return true;
	}
	private void deleteuserProduce(Integer uid) throws Exception{
		//判断是否存在该用户
		User tobeDeleted = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(uid)), User.class, this.collection_name);
		if(tobeDeleted == null) throw new Exception();
		//user collection
		this.mongoTemplate.remove(new Query(Criteria.where("_id").is(uid)), User.class, this.collection_name);
		//image cache folder not deleted with user
	}
	
	
	//返回好友列表和黑名单可以通过uid返回用户实例
	//好友列表的增删
	/*
	 * method = 1 : 增加操作，双向
	 * method = 2 : 删除操作，单向
	 * 
	 * 返回String值
	 * 前端可以直接前端打印出来
	 * 此方法未测试完成 2020/6/17 19点47分
	 * */
	@RequestMapping("/user/friendlist/{method}/{whose}/{operand}")
	public String friendlistOperation(@PathVariable(value = "method")Integer method, 
			@PathVariable(value = "whose")Integer my_uid, 
			@PathVariable(value = "operand")Integer friend_uid) {
		User me = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(my_uid)), User.class, this.collection_name);
		User friend = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(friend_uid)), User.class, this.collection_name);
		if(friend == null) return "用户不存在！";
		switch(method) {
		case 1:
			if(me.getFriendList().contains(friend_uid)) {
				return "您已添加其为好友！";
			}
			//涉及到message类，暂时放置;；这一步是产生一条message；message同意后双方加为好友
			return "好友邀请已发送，请等待对方回应。";
		case 2:
			//因为是选中一个好友来进行删除操作，故不会产生用户不在好友列表的情况
			//删除好友（双向）
			me.getFriendList().remove(me.getFriendList().indexOf(friend_uid));
			friend.getFriendList().remove(friend.getFriendList().indexOf(my_uid));
			//数据库操作
			this.mongoTemplate.findAllAndRemove(new Query(Criteria.where("_id").is(my_uid)), User.class, this.collection_name);
			this.mongoTemplate.findAllAndRemove(new Query(Criteria.where("_id").is(friend_uid)), User.class, this.collection_name);
			this.mongoTemplate.insert(me);
			this.mongoTemplate.insert(friend);
			return "您已删除好友。";
		}
		return "未知错误，清联系系统管理员";
	}
	
	
	//黑名单的增删
	/*
	 * 返回操作的成功与否
	 * method = 1 : 添加某人到黑名单
	 * method = 2 : 删除某人到黑名单
	 * */
	@RequestMapping("/user/blacklist/{method}/{whose}/{operand}")
	public String blacklistOperation(@PathVariable(value = "method")Integer method, 
										@PathVariable(value = "whose")Integer my_uid, 
										@PathVariable(value = "operand")Integer black_uid) {
		User me = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(my_uid)), User.class, this.collection_name);
		User black = this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(black_uid)), User.class, this.collection_name);
		if(black == null) return "用户不存在!";
		switch(method) {
		case 1:
			//拉黑某人
			if(me.getBlackList().contains(black_uid)) return "您已将其拉入黑名单!";
			//单项操作，只需要修改“我”的黑名单，而且不会产生message向被拉黑方通知
			me.getBlackList().add(black_uid);
			this.mongoTemplate.findAllAndRemove(new Query(Criteria.where("_id").is(my_uid)), User.class, this.collection_name);
			this.mongoTemplate.insert(me);
			return "您已将用户成功拉黑，请刷新页面!";
		case 2:
			//解除某人的拉黑,因为是通过黑名单操作，所以一定是解除已经在黑名单里的人，后端不进行不合理性判断
			me.getBlackList().remove(me.getBlackList().indexOf(black_uid));
			this.mongoTemplate.findAllAndRemove(new Query(Criteria.where("_id").is(my_uid)), User.class, this.collection_name);
			this.mongoTemplate.insert(me);
			return "您已将用户解除拉黑，请刷新页面!";
		default:
			return "不合理的Method = " + method +"值!";
		}
	}
	
	
	//用户查找功能
	/*
	 * method
	 * 1 :按用户名查找
	 * 2 :按昵称查找
	 * 
	 * 返回地都是一个list<User>
	 * 
	 * */
	@RequestMapping("user/search/{method}/{key}")
	public ArrayList<User> searchUser(@PathVariable(value = "method")Integer method, 
			@PathVariable(value = "key")String keyword){
		ArrayList<User> res = new ArrayList<User>();
		List<User> result = null;
		switch(method) {
		case 1:
			//按照用户名查找，理论上list只有一个User
			result = (List<User>)this.mongoTemplate.find(new Query(Criteria.where("loginID").is(keyword)), User.class, this.collection_name);
			break;
		case 2:
			result = (List<User>)this.mongoTemplate.find(new Query(Criteria.where("nickname").is(keyword)), User.class, this.collection_name);
		}
		res.addAll(result);
		return res;
	}

}
