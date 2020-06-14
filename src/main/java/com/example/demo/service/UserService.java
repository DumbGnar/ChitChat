package com.example.demo.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

public class UserService {
	//public static String baseUserImagePath = "D:\\testimage";		//用户的各种图像，包括头像，表情包的总基地址
	public static String baseUserImagePath = "/home/ubuntu/ChitChat/back/images";
	
	public static int counts = UserService.getMaxUID();	//计数，分配User的uid
	
	//根据用户的UID返回其头像在服务器目录地址，通过new一个File(返回地path)来获取对头像head.jpg的引用；欢迎使用
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
	
	//遍历images下所有文件夹，返回最大UID的值，一般来说这个是除了User部分别的用不到的
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
	
	public static int HEAD_DEFAULT = 1;	//系统默认头像选择为1.jpg
	//将uid的用户头像设置成系统默认的头像
	public static boolean setUserToDefaultHeadtrait(Integer uid, Integer headportrait) {
		String src = baseUserImagePath + "/default_headportrait/" + headportrait + ".jpg";
		String to = baseUserImagePath + "/" + uid + "/" +"head.jpg";
		System.out.println(src);
		System.out.println(to);
		//原头像删除
//		File head = new File(to);
//		if(head.exists()) {
//			if(!head.delete()) return false;
//		}
		//图片复制
		try {
			copyFile(src, to);
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static void copyFile(String srcPath, String destPath) throws IOException {
	    // 打开输入流
	    FileInputStream fis = new FileInputStream(srcPath);
	    // 打开输出流
	    FileOutputStream fos = new FileOutputStream(destPath);
	    // 读取和写入信息
	    int len = 0;
	    // 创建一个字节数组，当做缓冲区
	    byte[] b = new byte[1024];
	    while ((len = fis.read(b)) != -1) {
	        fos.write(b, 0, len);
	    }
	    // 关闭流  先开后关  后开先关
	    fos.close(); // 后开先关
	    fis.close(); // 先开后关  
	}
	
}
