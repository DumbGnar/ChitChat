package com.example.demo.service;

import java.util.concurrent.ConcurrentHashMap;

public class CodeService {

    private static ConcurrentHashMap<String,String> codeMap = new ConcurrentHashMap<>();

    public static String getIntegerCode(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = (int) (Math.random() * 9 + 1);
            sb.append(number);
        }
        return sb.toString();
    }

    
    public static synchronized void addCodeMap(String email, String code) {
        codeMap.put(email, code);
    }

    
    public static synchronized boolean checkCode(String email, String code) {
        if (!codeMap.containsKey(email) || !codeMap.get(email).equals(code)) {
            return false;
        } else {
            codeMap.remove(email);
            return true;
        }
    }

}