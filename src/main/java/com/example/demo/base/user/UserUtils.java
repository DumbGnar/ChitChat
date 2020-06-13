package com.example.demo.base.user;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

public class UserUtils {

    public static int counts = 0;    //计数，分配User的uid

    public static String baseUserImagePath = "D:\\testimage";        //用户的各种图像，包括头像，表情包的总基地址

    public static BufferedImage getHeadPortraitByID(int id) {
        try {
            return ImageIO.read(new FileInputStream(new File(baseUserImagePath + "\\" + id + "\\" + "head.jpg")));
        } catch (IOException e) {
            //后续用日志处理代替
            System.out.println("error:get headPortrait error");
            System.out.println(baseUserImagePath + "\\" + id + "\\" + "head.jpg");
            return null;
        }
    }
}
