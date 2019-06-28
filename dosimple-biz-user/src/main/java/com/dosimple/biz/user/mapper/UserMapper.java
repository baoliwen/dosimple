package com.dosimple.biz.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dosimple.biz.user.annotation.DataSourceTarget;
import com.dosimple.biz.user.entity.User;
import com.dosimple.biz.user.enums.DataSourceEnum;
import org.springframework.stereotype.Repository;

/**
 * @author dosimple
 */
@Repository
@DataSourceTarget(DataSourceEnum.DB3)
public interface UserMapper extends BaseMapper<User> {
}
