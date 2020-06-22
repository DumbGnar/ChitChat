package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.demo.service.ImageService;
import com.example.demo.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.ImageStorage;

/**
 * 图片相关接口 ❓
 */
@RestController
public class ImageController {

    private final ImageStorage object = ImageStorage.getInstance();

    /**
     * 删除头像 👌
     * @param uid 用户ID
     */
    @PostMapping("/images/{uid}/head/delete")
    public void deleteHead(@PathVariable int uid) {
        String path = UserService.baseUserImagePath + "/" + uid + "/head.jpg";
        File file = new File(path);
        file.delete();
    }

    /**
     * 删除某个图片缓存 👌
     * @param uid 用户ID
     * @param map .get("image") 文件名数组，带后缀.jpg
     */
    @PostMapping("/images/{uid}/imagecache/delete")
    public void deleteImagecache(@PathVariable int uid,
                                 @RequestBody HashMap<String,List<String>> map) {
        List<String> fileNames = map.get("image");
        String path = UserService.baseUserImagePath + "/" + uid + "/imagecache/";
        for (String fileName : fileNames) {
            File file = new File(path + fileName);
            file.delete();
        }
    }

    /**
     * 删除某个表情 👌
     * @param uid 用户ID
     * @param map .get("image") 文件名数组，带后缀.jpg
     */
    @PostMapping("/images/{uid}/faces/delete")
    public void deleteFace(@PathVariable int uid,
                           @RequestBody HashMap<String,List<String>> map) {
        List<String> fileNames = map.get("image");
        String path = UserService.baseUserImagePath + "/" + uid + "/faces/";
        for (String fileName : fileNames) {
            File file = new File(path + fileName);
            file.delete();
        }
    }

    /**
     * 读取全部图片缓存 👌
     * @param uid 用户ID
     * @return 图片缓存文件名List
     */
    @RequestMapping("/images/{uid}/imagecache")
    public List<String> getImagecacheFileName(@PathVariable int uid) {
        String dirPath = UserService.baseUserImagePath + "/" + uid + "/imagecache";
        File dir = new File(dirPath);
        return new ArrayList<String>(Arrays.asList(dir.list()));
    }

    /**
     * 读取全部表情 👌
     * @param uid 用户ID
     * @return 表情文件名List
     */
    @RequestMapping("/images/{uid}/faces")
    public List<String> getFacesFileName(@PathVariable int uid) {
        String dirPath = UserService.baseUserImagePath + "/" + uid + "/faces";
        File dir = new File(dirPath);
        return new ArrayList<String>(Arrays.asList(dir.list()));
    }

    /**
     * 上传图片 👌
     * @param map {"image": "Base64字符串"}
     * @param uid 用户uid
     * @param type 1：头像，2：表情，3：图片缓存
     * @return 上传成功：日期字符串；上传失败：提示信息。
     */
    @PostMapping("/upload/image/{uid}/{type}")
    public String handleImageUpload(@RequestBody HashMap<String,String> map,
                                    @PathVariable int uid,
                                    @PathVariable int type) {
        String base64Image = map.get("image");
        if (StringUtils.isEmpty(base64Image)) {
            return "Illegal Base64 image";
        }
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
     * 查看所有图片 ❌
     * @return 所有图片的序号
     */
    @RequestMapping("images/view")
    public ArrayList<Integer> viewImage() {
        return object.getImages();
    }

    /**
     * 删除某个图片，将此图片的序号在仓库中删除 ❌
     * @param id
     * @return
     */
    @RequestMapping("images/delete/{id}")
    public boolean deleteOne(@PathVariable(value = "id") int id) {
        object.deleteImage(id);
        return true;
    }

    /**
     * 添加某图片，将此图片存放进仓库 ❌
     * @param id
     * @return
     */
    @RequestMapping("images/add/{id}")
    public boolean addImageById(@PathVariable(value = "id") int id) {
        object.addImage(id);
        return true;
    }
}
