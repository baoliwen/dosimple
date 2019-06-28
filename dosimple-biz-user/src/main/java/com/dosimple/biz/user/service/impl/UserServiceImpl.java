package com.dosimple.biz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dosimple.biz.user.annotation.DataSourceTarget;
import com.dosimple.biz.user.mapper.UserMapper;
import com.dosimple.common.util.ObjectId;
import com.dosimple.biz.user.entity.User;
import com.dosimple.biz.user.enums.DataSourceEnum;
import com.dosimple.biz.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author dosimple
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    @DataSourceTarget(DataSourceEnum.DB1)
    public List<User> selectList(Wrapper<User> wrapper) {
        return this.list(wrapper);
    }

    @Override
    @DataSourceTarget(DataSourceEnum.DB2)
    public List<User> selectListDb2(Wrapper<User> wrapper) {
        return this.list(wrapper);
    }

    @Override
    public List<User> selectListDb3(Wrapper<User> wrapper) {
        return this.list(wrapper);
    }

    @Override
//    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @DataSourceTarget(DataSourceEnum.DB1)
    @Transactional(rollbackFor = Exception.class)
    public void insertDb1() {
        User user = new User();
        user.setId(ObjectId.get().toString());
        user.setPassword("1234");
        user.setDateCreated(new Date());
        user.setSalt("1234");
        user.setMobile("13333333333");
        user.setSessionToken("1234");
        this.save(user);
    }

    @Override
//    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @DataSourceTarget(DataSourceEnum.DB3)
    @Transactional(rollbackFor = Exception.class)
    public void insertDb3() {
        User user = new User();
        user.setId(ObjectId.get().toString());
        user.setPassword("1234");
        user.setDateCreated(new Date());
        user.setSalt("1234");
        user.setMobile("13333333333");
        user.setSessionToken("1234");
        this.save(user);
    }
}
