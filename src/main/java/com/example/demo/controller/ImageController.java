package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.demo.service.ImageService;
import com.example.demo.service.StorageService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.example.demo.model.ImageStorage;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class ImageController {

    private ImageStorage object = ImageStorage.getInstance();

    @PostMapping("/upload/image/{uid}/{type}")
    public String handleImageUpload(@RequestParam("image") String base64Image,
                                    @PathVariable int uid,
                                    @PathVariable int type) {
        String path = UserService.baseUserImagePath + "/" + uid + "/";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String dateString = formatter.format(new Date());
        switch (type) {
            case 1:
                // head.jpg
                path += "head.jpg";
                break;
            case 2:
                // faces/<dateString>.jpg
                path += "faces/" + dateString + ".jpg";
                break;
            case 3:
                // imagecache/<dateString>.jpg
                path += "imagecache/" + dateString + ".jpg";
                break;
            default:
        }
        try {
            ImageService.saveBase64Image(base64Image, path);
        } catch (IOException e) {
            return "Writing to file failed";
        }
        return "Upload successfully";
    }

    // 查看所有图片
    @RequestMapping("image/view")//返回所有图片的序号
    public ArrayList<Integer> viewimage() {
        return object.getImages();
    }

    // 删除某个图片
    @RequestMapping("image/delete/{id}")//将此图片的序号在仓库中删除
    public boolean deleteone(@PathVariable(value = "id") int id) {

        object.deleteImage(id);
        return true;
    }

    // 添加某图片
    @RequestMapping("image/add/{id}")//将此图片存放进仓库
    public boolean addImageById(@PathVariable(value = "id") int id) {
        object.addImage(id);
        return true;
    }
}
