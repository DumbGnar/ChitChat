package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.demo.service.ImageService;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.ImageStorage;

@RestController
public class ImageController {

    private final ImageStorage object = ImageStorage.getInstance();

    @PostMapping("/images/{uid}/faces/{fileName}/delete")
    public boolean deleteFace(@PathVariable int uid,
                             @PathVariable String fileName) {
        String path = UserService.baseUserImagePath + "/" + uid + "/faces/" + fileName + ".jpg";
        File file = new File(path);
        return file.delete();
    }

    @GetMapping("/images/{uid}/faces")
    public List<String> getFacesFileName(@PathVariable int uid) {
        String dirPath = UserService.baseUserImagePath + "/" + uid + "/faces";
        File dir = new File(dirPath);
        return new ArrayList<String>(Arrays.asList(dir.list()));
    }

    @PostMapping("/upload/image/{uid}/{type}")
    public String handleImageUpload(@RequestBody HashMap<String,String> map,
                                    @PathVariable int uid,
                                    @PathVariable int type) {
        String base64Image = map.get("image");
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
        } catch (IllegalArgumentException e) {
            return "Illegal Base64 image";
        } catch (IOException e) {
            return "Writing to file failed";
        }
        return dateString;
    }

    /**
     * 查看所有图片
     * @return
     */
    @RequestMapping("images/view")//返回所有图片的序号
    public ArrayList<Integer> viewImage() {
        return object.getImages();
    }

    /**
     * 删除某个图片
     * @param id
     * @return
     */
    @RequestMapping("images/delete/{id}")//将此图片的序号在仓库中删除
    public boolean deleteOne(@PathVariable(value = "id") int id) {

        object.deleteImage(id);
        return true;
    }

    /**
     * 添加某图片
     * @param id
     * @return
     */
    @RequestMapping("images/add/{id}")//将此图片存放进仓库
    public boolean addImageById(@PathVariable(value = "id") int id) {
        object.addImage(id);
        return true;
    }
}
