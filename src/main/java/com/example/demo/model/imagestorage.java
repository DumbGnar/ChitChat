package com.example.demo.model;

import java.util.ArrayList;

public class imagestorage {
	 
	   //创建 SingleObject 的一个对象
	   private static imagestorage instance = new imagestorage();
	 

	   private imagestorage(){}
	   private ArrayList<Integer> images;
	  
	   public static imagestorage getInstance(){
	      return instance;
	   }
	
	   public ArrayList<Integer> getImages() {
		return images;
	   }
	   public void deleteImage(int id)
	   {
		   images.remove(Integer.valueOf(id));
	   }
	   public void addImage(int id)
	   {
		   images.add(Integer.valueOf(id));
	   }
}
