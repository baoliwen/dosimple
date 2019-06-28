package com.dosimple.biz.user.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dosimple.biz.user.entity.User;

import java.util.List;

/**
 * @author dosimple
 */
public interface UserService extends IService<User> {

    List<User> selectList(Wrapper<User> wrapper);

    List<User> selectListDb2(Wrapper<User> wrapper);

    List<User> selectListDb3(Wrapper<User> wrapper);

    void insertDb1();

    void insertDb3();
}
