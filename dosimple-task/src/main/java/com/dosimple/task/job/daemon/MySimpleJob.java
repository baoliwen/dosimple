package com.dosimple.task.job.daemon;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dosimple.task.config.elasticjob.annotation.ElasticJobConf;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@ElasticJobConf(name = "MySimpleJob", cron = "0/10 * * * * ?", shardingItemParameters = "0=0,1=1"
        , description = "简单任务", eventTraceRdbDataSource = "jobDataSource", overwrite = true)
@Slf4j
public class MySimpleJob implements SimpleJob {

    public void execute(ShardingContext context) {
        String shardParamter = context.getShardingParameter();
        System.out.println("分片参数："+shardParamter);
        log.info("ThreadId={},当前分片项={}，任务总片数={}", Thread.currentThread().getId(),context.getShardingItem()
                ,context.getShardingTotalCount());
        int value = Integer.parseInt(shardParamter);
        for (int i = 0; i < 10; i++) {
            if (i % 2 == value) {
                String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
                System.out.println(time + ":开始执行简单任务" + i);
            }
        }
    }

}
