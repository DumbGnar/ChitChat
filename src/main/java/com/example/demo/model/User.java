package com.example.demo.model;



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

    private String intro = "这个人很懒，什么也没有留下~";

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
}
