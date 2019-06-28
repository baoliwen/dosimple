package com.dosimple.biz.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dosimple.biz.shop.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author baolw
 */
@Repository
public interface OrderInfoMapper extends BaseMapper<Order> {
    int insert(@Param("id")Long id,@Param("userId")String userId, @Param("orderId")String orderId
            , @Param("orderNo")String orderNo, @Param("isactive")int isactive, @Param("inserttime")Date inserttime
            , @Param("updatetime")String updatetime);

    Order selectById(@Param("id")Long id);

    Order selectByOrderId(@Param("orderId")Long orderId);
}
