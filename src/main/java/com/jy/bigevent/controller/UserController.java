package com.jy.bigevent.controller;


import com.jy.bigevent.pojo.Result;
import com.jy.bigevent.pojo.User;
import com.jy.bigevent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(String username, String password) {
        //查询用户
        User user = userService.findByUsername(username);
        if(user==null) {
            //没有占用
            //注册
            userService.register(username, password);
            return Result.success();
        } else {
            //占用
            return Result.error("用户名已被占用");
        }



    }
}
