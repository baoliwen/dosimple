package com.dosimple.biz.shop;

import com.dosimple.common.constant.ApiResult;
import com.dosimple.common.constant.IdBizTags;
import com.dosimple.common.util.GsonHelper;
import com.dosimple.common.util.LogUtils;
import com.dosimple.common.util.RedisLockUtils;
import com.dosimple.biz.shop.constant.Constant;
import com.dosimple.biz.shop.dto.OrderInfoReqDto;
import com.dosimple.biz.shop.entity.Order;
import com.dosimple.biz.shop.service.OrderInfoService;
import com.dosimple.rpc.client.IdGeneratorApiService;
import com.dosimple.rpc.client.UserServiceApiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestShop {
    @Autowired
    private IdGeneratorApiService idGeneratorApiService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private UserServiceApiService userServiceApiService;

    /**
     * 测试RPC获取主键ID
     * 1.本地缓存一部分分布式主键ID
     * 2.本地缓存中的ID不足时RPC调用取ID
     */
    @Test
    public void rpcGeneratorId() {
        for (IdBizTags idBizTags : IdBizTags.values()) {
            Constant.CACHE.put(idBizTags.name(), new LinkedBlockingDeque<>());
        }
        OrderInfoReqDto dto = new OrderInfoReqDto();
        dto.setName("sdfsdf");
        BlockingQueue<Long> ids = Constant.CACHE.get(IdBizTags.PAY_ORDER.name());
        ApiResult<List<Long>> apiResult = idGeneratorApiService.generator(IdBizTags.PAY_ORDER.name());
        if (apiResult.success()) {
            ids.addAll(apiResult.getData());
            Constant.CACHE.put(IdBizTags.PAY_ORDER.name(), ids);
        }
        orderInfoService.insertIdFromApi(dto);
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
    }
    @Test
    public void testCallRemoteRpcAndTxTranstion() {
        long old = System.currentTimeMillis();
        ApiResult apiResult = userServiceApiService.firstDemo("hello world");
        ApiResult apiResult1 = userServiceApiService.txTranstion("hello world");
        LogUtils.logInfo(log, "rpc调用结果", apiResult);
        log.info("time use >>>> {}", System.currentTimeMillis()-old);
    }
    public class TestRedisLockThread implements Runnable{
        private int i = 0;
        private TestRedisLockThread(int i) {
            this.i = i;
        }
        @Override
        public void run() {
            long oldTime = System.currentTimeMillis();
            int index = 0;
            RedisLockUtils lockUtils = RedisLockUtils.getInstance();
            String lockValue = UUID.randomUUID().toString();
            while (!lockUtils.lock("111", lockValue)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (index++ > 100) return;
            }
            try {
                System.out.println(i);
            }finally {
                boolean releaseFlag = lockUtils.releaseLock("111", lockValue);
                if (!releaseFlag) System.out.println("锁释放失败");
            }
            System.out.println("线程执行耗时 >>>>"+ Thread.currentThread().getName()+">>>>"+(System.currentTimeMillis() - oldTime));
        }
    }

    /**
     * 测试redis分布式锁
     */
    @Test
    public void redisLock() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 500; i++) {
            TestRedisLockThread thread = new TestRedisLockThread(i);
            service.execute(thread);
        }
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * shardingJdbc保存测试
     */
    @Test
    public void shardingJdbcSave() {
        OrderInfoReqDto dto = new OrderInfoReqDto();
        dto.setName("sdfsdf");
        orderInfoService.insert(dto);
    }

    /**
     * shardingJdbc查询测试
     */
    @Test
    public void shardingJdbcSelect() {
        String id = "10000";
        Long orderId = 335347827620184684L;
        Order o = orderInfoService.selectById(id);
        LogUtils.info(log, "byId{data}", GsonHelper.toJson(o));

        o = orderInfoService.selectByOrderId(orderId);
        LogUtils.info(log, "ByOrderId{data}", GsonHelper.toJson(o));
    }

    /**
     * 测试RPC
     */
    @Test
    public void rpc() {
        long old = System.currentTimeMillis();
        ApiResult apiResult = userServiceApiService.firstDemo("hello world");
        LogUtils.logInfo(log, "rpc调用结果", apiResult);
        log.info("time use >>>> {}", System.currentTimeMillis()-old);
    }
}
