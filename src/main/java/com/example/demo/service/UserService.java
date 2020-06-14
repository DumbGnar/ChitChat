package com.example.demo.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

public class UserService {
	//public static String baseUserImagePath = "D:\\testimage";		//用户的各种图像，包括头像，表情包的总基地址
	public static String baseUserImagePath = "/home/ubuntu/ChitChat/back/images";
	
	public static int counts = UserService.getMaxUID() + 1;	//计数，分配User的uid
	
	public static BufferedImage getHeadPortraitByID(int id) {
		try {
			return ImageIO.read(new FileInputStream(new File(baseUserImagePath + "/" + id + "/" + "head.jpg")));
		}catch(IOException e) {
			//后续用日志处理代替
			System.out.println("error:get headPortrait error");
			System.out.println(baseUserImagePath + "/" + id + "/" + "head.jpg");
			return null;
		}
	}
	
	//返回最大UID的值
	public static int getMaxUID() {
		int res = -1;
		File base = new File(baseUserImagePath);
		File[] fs = base.listFiles();
		for(int i = 0; i < fs.length; i++) {
			if(fs[i].isDirectory()) {
				try {
					int uid = Integer.parseInt(fs[i].getName());
					res = (res <= uid)?uid:res;
				}catch(NumberFormatException e) {
					System.out.println(fs[i].getName());
				}
			}
		}
		System.out.println(res);
		return res;
	}
	
}
