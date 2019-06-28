package com.dosimple.idgenerator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dosimple.common.constant.Constant;
import com.dosimple.common.constant.IdBizTags;
import com.dosimple.common.constant.RedisKeys;
import com.dosimple.common.util.LogUtils;
import com.dosimple.common.util.RedisLockUtils;
import com.dosimple.common.util.RedisUtil;
import com.dosimple.idgenerator.dao.IdGeneratorMapper;
import com.dosimple.idgenerator.entity.IdGenerator;
import com.dosimple.idgenerator.service.IIdGeneratorService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dosimple
 * @since 2019-05-13
 */
@Service
@Slf4j
public class IdGeneratorServiceImpl extends ServiceImpl<IdGeneratorMapper, IdGenerator> implements IIdGeneratorService {

    @Override
    public List<Long> generatorId(String bizTag) {
        String key = IdBizTags.transformToRedisKey(bizTag);
        if (key != null) {

            List<Long> list = Lists.newArrayList();
            long size = RedisUtil.lGetListSize(key);
            if (size < Constant.ID_MIN_IDLE) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        createIdsByBizTag(bizTag);
                    }
                }).start();
            }else{
                for (int i = 0; i < 500; i++) {
                    Object o = RedisUtil.lpop(key);
                    if (o != null) {
                        list.add(Long.parseLong(o.toString()));
                    }
                }
            }
            return list;
        } else {
            return  null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> createIdsByBizTag(String bizTag) {
        RedisLockUtils lockUtils = RedisLockUtils.getInstance();
        IdGenerator idGenerator;
        String lockKey = RedisKeys.LOCK_ID_GENERATOR.build();
        String lockValue = UUID.randomUUID().toString();
        int index = 0;
        while (!lockUtils.lock(lockKey, lockValue)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (index++ > 100) return null;
        }
        QueryWrapper<IdGenerator> qw = new QueryWrapper<>();
        qw.eq("biz_tag", bizTag);
        try {
            idGenerator = this.baseMapper.selectOne(qw);
            if (null == idGenerator) {
                LogUtils.error(log, "获取业务ID失败。{bizTag}", bizTag);
                throw new RuntimeException("获取业务ID失败");
            }
            idGenerator.setDateUdpated(new Date());
            idGenerator.setMaxId(idGenerator.getMaxId() + idGenerator.getStep());
            idGenerator.updateById();
        } finally {
            if (!lockUtils.releaseLock(lockKey, lockValue)) LogUtils.error(log, "释放redis锁失败。{key}{value}", lockKey, lockValue);
        }
        List<Long> result = new ArrayList<>();
        for (Integer i = 0; i < idGenerator.getStep(); i++) {
            result.add(idGenerator.getMaxId() - idGenerator.getStep() + i);
        }
        String key = IdBizTags.transformToRedisKey(bizTag);
        if (key != null) {
            RedisUtil.lpushCollection(key, result);
        } else {
            LogUtils.error(log, "没有将生成的ID放入redis。{bizTag}", bizTag);
        }
        return result;
    }
}
