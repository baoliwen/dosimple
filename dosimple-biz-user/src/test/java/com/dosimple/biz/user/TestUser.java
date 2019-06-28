package com.dosimple.biz.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dosimple.common.util.GsonHelper;
import com.dosimple.common.util.LogUtils;
import com.dosimple.biz.user.entity.User;
import com.dosimple.biz.user.enums.DataSourceEnum;
import com.dosimple.biz.user.service.UserService;
import com.dosimple.biz.user.util.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestUser {
    @Autowired
    private UserService userService;


    /**
     * 测试多数据源下的保存和查询
     */
    @Test
    public void dataSourcesInsert() {
        List list1 =  CollectionUtils.transform(userService.selectList(new QueryWrapper<>()), o -> {
            User user = (User) o;
            return user.getMobile();
        });
        List list2 = CollectionUtils.transform(userService.selectListDb2(new QueryWrapper<>()), o -> {
            User user = (User) o;
            return user.getMobile();
        });
        DynamicDataSourceContextHolder.setDataSourceRouterKey(DataSourceEnum.DB3.getValue());
        List list3 = CollectionUtils.transform(userService.selectListDb3(new QueryWrapper<>()), o -> {
            User user = (User) o;
            return user.getMobile();
        });
        DynamicDataSourceContextHolder.removeDataSourceRouterKey();
        LogUtils.info(log, "1", GsonHelper.toJson(list1));
        LogUtils.info(log, "2", GsonHelper.toJson(list2));
        LogUtils.info(log, "3", GsonHelper.toJson(list3));
        userService.insertDb1();
        userService.insertDb3();

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
