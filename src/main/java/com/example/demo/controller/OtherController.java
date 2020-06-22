package com.example.demo.controller;

import com.example.demo.model.MyResponse;
import com.example.demo.service.CodeService;
import com.example.demo.service.MailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 其他未分类的接口 👌
 */
@RestController
public class OtherController {

    @PostMapping("sendcode")
    public MyResponse sendCode(@RequestParam String email) {
        String code = CodeService.getIntegerCode(6);
        try {
            MailService.sendCodeEmail(email, code);
        } catch (Exception e) {
            return new MyResponse(false, "验证码发送失败");
        }
        CodeService.addCodeMap(email, code);
        return new MyResponse(true, "验证码发送成功");
    }

//    @PostMapping("register")
//    public MyResponse register(@RequestParam String username,
//                               @RequestParam String email,
//                               @RequestParam String password,
//                               @RequestParam String code) {
//        if (!UserService.checkUsername(username)) {
//            return new MyResponse(false, "用户名已存在");
//        } else if (!CodeService.checkCode(email, code)) {
//            return new MyResponse(false, "验证码错误");
//        }
//        UserService.save(new User(username, email, password));
//        return new MyResponse(true, "注册成功");
//    }
}
