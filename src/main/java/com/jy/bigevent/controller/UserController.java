package com.jy.bigevent.controller;


import ch.qos.logback.core.util.StringUtil;
import com.jy.bigevent.pojo.Result;
import com.jy.bigevent.pojo.User;
import com.jy.bigevent.service.UserService;
import com.jy.bigevent.utils.JwtUtil;
import com.jy.bigevent.utils.Md5Util;
import com.jy.bigevent.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/userInfo")
    public Result<User> userInfo(/*@RequestHeader(name="Authorization") String token*/) {
        //根据用户名查询用户
/*        Map<String, Object> map = JwtUtil.parseToken(token);
        String username = (String) map.get("username");*/

        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");

        User user = userService.findByUsername(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        //更新用户信息
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
            //更新用户头像
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params) {
//        1. 校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd)||!StringUtils.hasLength(newPwd)||!StringUtils.hasLength(rePwd)){
            return Result.error("参数不能为空");
        }

        //2. 校验密码
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUsername(username);
        if(!user.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码错误");
        }

        //3. 校验密码是否一致
        if(!rePwd.equals(newPwd)){
            return Result.error("两次密码不一致");
        }

        //更新用户密码
        userService.updatePwd(newPwd);
        return Result.success();

    }

}
