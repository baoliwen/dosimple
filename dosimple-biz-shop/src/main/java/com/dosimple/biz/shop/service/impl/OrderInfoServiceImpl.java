package com.dosimple.biz.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import com.dosimple.biz.shop.constant.Constant;
import com.dosimple.biz.shop.entity.Order;
import com.dosimple.biz.shop.entity.OrderInfo;
import com.dosimple.biz.shop.service.OrderInfoService;
import com.dosimple.common.constant.ApiResult;
import com.dosimple.common.constant.IdBizTags;
import com.dosimple.common.util.LogUtils;
import com.dosimple.biz.shop.dto.OrderInfoReqDto;
import com.dosimple.biz.shop.mapper.OrderInfoMapper;
import com.dosimple.rpc.client.IdGeneratorApiService;
import com.dosimple.rpc.client.UserServiceApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author baolw
 */
@Slf4j
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, Order> implements OrderInfoService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private IdGeneratorApiService idGeneratorApiService;
    @Autowired
    private CommonSelfIdGenerator commonSelfIdGenerator;
    @Autowired
    private UserServiceApiService userServiceApiService;
    @Override
    public Order selectById(String id) {
        return orderInfoMapper.selectById(id);
    }

    @Override
    public List<OrderInfo> selectAll() {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(OrderInfoReqDto dto) {
        for (int i = 0; i < 10; i++) {
            Order orderInfo = new Order();
            orderInfo.setId(commonSelfIdGenerator.generateId().longValue());
            orderInfo.setInserttime(new Date());
            orderInfo.setUpdatetime(new Date());
            orderInfo.setOrderId(commonSelfIdGenerator.generateId().longValue()+ RandomUtils.nextInt(0, 5000));
            orderInfo.setUserId(commonSelfIdGenerator.generateId().longValue()+ RandomUtils.nextInt(0, 5000));
            orderInfo.setOrderNo(RandomStringUtils.random(10, "askdjfgaslkflakwebmnvbmxznbvlagvw"));
            orderInfoMapper.insert(orderInfo);
        }
        return true;
    }

    @Override
    public boolean insertIdFromApi(OrderInfoReqDto dto) {
        BlockingQueue<Long> ids = Constant.CACHE.get(IdBizTags.PAY_ORDER.name());
        if(ids.size() == 0) return false;
        for (int i = 0; i < 1000; i++) {
            Order orderInfo = new Order();
            Long id = ids.poll();
            if (ids.size() < com.dosimple.common.constant.Constant.ID_MIN_IDLE) {
                ApiResult<List<Long>> apiResult = idGeneratorApiService.generator(IdBizTags.PAY_ORDER.name());
                if (apiResult.success()) {
                    ids.addAll(apiResult.getData());
                    Constant.CACHE.put(IdBizTags.PAY_ORDER.name(), ids);
                }
            }
            if (id != null) {
                orderInfo.setId(id);
                orderInfo.setInserttime(new Date());
                orderInfo.setUpdatetime(new Date());
                orderInfo.setOrderId(id);
                orderInfo.setUserId(id);
                orderInfo.setOrderNo(RandomStringUtils.random(10, "askdjfgaslkflakwebmnvbmxznbvlagvw"));
                orderInfoMapper.insert(orderInfo);
            }else{
                LogUtils.error(log, "获取ID异常");
            }
        }
        return true;
    }

    @Override
    public boolean callRemoteRpc() {
        userServiceApiService.firstDemo("222");
        userServiceApiService.txTranstion("222");

        Order orderInfo = new Order();
        orderInfo.setId(commonSelfIdGenerator.generateId().longValue());
        orderInfo.setInserttime(new Date());
        orderInfo.setUpdatetime(new Date());
        orderInfo.setOrderId(commonSelfIdGenerator.generateId().longValue()+ RandomUtils.nextInt(0, 5000));
        orderInfo.setUserId(commonSelfIdGenerator.generateId().longValue()+ RandomUtils.nextInt(0, 5000));
        orderInfo.setOrderNo(RandomStringUtils.random(10, "askdjfgaslkflakwebmnvbmxznbvlagvw"));
        orderInfoMapper.insert(orderInfo);

        return true;
    }

    @Override
    public Order selectByOrderId(Long orderId) {
        return orderInfoMapper.selectByOrderId(orderId);
    }


}
