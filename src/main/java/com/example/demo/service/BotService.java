package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BotService {

    // public static String BotPath = "D:\\Python\\ChatRobot\\ChatRobot\\ChatRobot";
    public static String BotPath = "/home/ubuntu/ChitChat/back/pythons";

    public static String chatWithBot(Integer uid, String content) {
        Process proc;
        try {
            int id = uid.intValue();
            String command = "python " + BotPath + "/Main.py " + id + " " + content;
            proc = Runtime.getRuntime().exec(command);    //需要修改路径
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "utf-8"));
            String line = null;
            String res = "";
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            in.close();
            proc.waitFor();
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean createSelfChatbot(Integer uid) {
        Process proc;
        try {
            int id = uid;
            String command = "python " + BotPath + "/createSelfBot.py " + id;
            proc = Runtime.getRuntime().exec(command);    // 需要修改路径
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "GBK"));
            String line = null;
            String res = "";
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            in.close();
            proc.waitFor();
            return !(res == null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
