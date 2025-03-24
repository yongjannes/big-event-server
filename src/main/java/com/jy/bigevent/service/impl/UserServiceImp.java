package com.jy.bigevent.service.impl;

import com.jy.bigevent.mapper.UserMapper;
import com.jy.bigevent.pojo.User;
import com.jy.bigevent.service.UserService;
import com.jy.bigevent.utils.Md5Util;
import com.jy.bigevent.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
       User user = userMapper.findByUsername(username);
        return user;
    }

    @Override
    public void register(String username, String password) {
        //加密
        String md5Password = Md5Util.getMD5String(password);
        //添加
        userMapper.add(username, md5Password);
    }

    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id =(Integer) map.get("id");
        userMapper.updateAvatar(avatarUrl,id);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id =(Integer) map.get("id");
        userMapper.updatePwd(Md5Util.getMD5String(newPwd),id);
    }
}
