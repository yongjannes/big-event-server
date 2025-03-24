package com.jy.bigevent.mapper;

import com.jy.bigevent.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户信息
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    //增加用户
    @Insert("INSERT INTO user (username, password,create_time, update_time)" +
            " VALUES (#{username}, #{md5Password}, NOW(), NOW())")
    void add(String username, String md5Password);


    //更新用户信息
    @Update("UPDATE user SET nickname = #{nickname}, email = #{email}, update_time = #{updateTime} WHERE id = #{id}")
    void update(User user);
}
