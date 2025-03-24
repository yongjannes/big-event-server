package com.jy.bigevent.service;

import com.jy.bigevent.pojo.User;

public interface UserService {
    // 根据用户名查询用户信息
    User findByUsername(String username);

    // 注册
    void register(String username, String password);

    // 更新用户信息
    void update(User user);
}
