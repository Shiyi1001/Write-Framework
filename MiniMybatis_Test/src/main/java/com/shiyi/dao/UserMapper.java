package com.shiyi.dao;

import com.shiyi.pojo.User;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Description
 * @Author FengL
 * @Date 2021/06/17 14:41
 * @Version V1.0
 */
public interface UserMapper {

  User selectOne(User user);

  List<User> selectList();

  Integer updateUserById(User user);

  Integer deleteUserById(User user);

  Integer insertUser(User user);
}
