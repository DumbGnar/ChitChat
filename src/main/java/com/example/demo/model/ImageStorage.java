package com.example.demo.model;

import java.util.ArrayList;

public class ImageStorage {

    /**
     * 创建 SingleObject 的一个对象
     */
    private static ImageStorage instance = new ImageStorage();

    private ArrayList<Integer> images;

    private ImageStorage() {
    }

    public static ImageStorage getInstance() {
        return instance;
    }

    public ArrayList<Integer> getImages() {
        return images;
    }

    public void deleteImage(int id) {
        images.remove(Integer.valueOf(id));
    }

    public void addImage(int id) {
        images.add(Integer.valueOf(id));
    }
}
