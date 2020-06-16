package com.example.demo.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.model.imagestorage;

@RestController
public class ImageController 
{
	
	private imagestorage object = imagestorage.getInstance();
	
	//查看所有图片
	@RequestMapping("image/view")//返回所有图片的序号
	public ArrayList<Integer> viewimage() {
		return object.getImages();	
	}
	
	//删除某个图片
	@RequestMapping("image/delete/{id}")//将此图片的序号在仓库中删除
	public boolean deleteone(@PathVariable(value = "id")int id) {
	
		object.deleteImage(id);
		return true;	
	}
	
	//添加某图片
	@RequestMapping("image/add/{id}")//将此图片存放进仓库
	public boolean getUserByID(@PathVariable(value = "uid")int id) {
		object.addImage(id);
		return true;	
	}
	
	
	
	
		
}
