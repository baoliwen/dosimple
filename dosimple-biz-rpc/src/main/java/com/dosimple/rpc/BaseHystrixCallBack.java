package com.dosimple.rpc;

import com.dosimple.rpc.entity.HystrixErrorMessage;
import com.dosimple.common.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class BaseHystrixCallBack {
    @Autowired
    private MongoTemplate mongoTemplate;

    protected void logging(String jsonParam) {
        hystrix(super.getClass(), jsonParam);
    }

    // 熔断日志
    private void hystrix(Class clazz, String jsonParam) {
        String className = clazz.getName();
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        LogUtils.error(log, "hystrix err >> {className}{methodName}", className, methodName);
        HystrixErrorMessage message = new HystrixErrorMessage();
        message.setDateCreated(new Date());
        message.setServiceMethodName(Thread.currentThread().getStackTrace()[3].getMethodName());
        message.setClassName(super.getClass().getName());
        message.setMethodParam(jsonParam);
        mongoTemplate.save(message);
//        TODO 可发送异常到钉钉，或者邮件通知开发，运维，测试等人员
    }
}
