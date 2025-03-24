package com.jy.bigevent.controller;


import com.jy.bigevent.pojo.Result;
import com.jy.bigevent.pojo.User;
import com.jy.bigevent.service.UserService;
import com.jy.bigevent.utils.JwtUtil;
import com.jy.bigevent.utils.Md5Util;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^.{5,16}$") String username, @Pattern(regexp = "^.{5,16}$") String password) {
        //查询用户
        User user = userService.findByUsername(username);
        if (user == null) {
            //没有占用
            //注册
            userService.register(username, password);
            return Result.success();
        } else {
            //占用
            return Result.error("用户名已被占用");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^.{5,16}$") String username, @Pattern(regexp = "^.{5,16}$") String password) {
        //根据用户名查询用户
        User loginUser = userService.findByUsername(username);
        //判断用户是否存在
        if (loginUser == null) {
            return Result.error("用户名不存在");
        }
        //判断密码是否正确
        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
            //登录成功
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);

            return Result.success(token);
        } else {
            //密码错误
            return Result.error("密码错误");
        }
    }
}
