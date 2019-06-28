package com.dosimple.biz.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dosimple.biz.shop.entity.Order;
import com.dosimple.biz.shop.entity.OrderInfo;
import com.dosimple.biz.shop.dto.OrderInfoReqDto;

import java.util.List;

/**
 * @author baolw
 */
public interface OrderInfoService  extends IService<Order> {

    Order selectById(String id);

    List<OrderInfo> selectAll();

    boolean insert(OrderInfoReqDto dto);

    Order selectByOrderId(Long orderId);

    boolean insertIdFromApi(OrderInfoReqDto dto);

    boolean callRemoteRpc();
}
