package com.distributed.demo.redislock.task;

import com.distributed.demo.redislock.utils.PropertiesUtil;
import com.distributed.demo.redislock.utils.RedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: redis-lock
 * @Auther: GERRY
 * @Date: 2018/11/18 18:58
 * @Description:
 */
@Component
@Slf4j
public class OrderTask {

    public static final String REDIS_LOCK = "redis_lock";

    @Scheduled(cron = "0 */1 * * * ?")
    //@Scheduled(cron = "0/20 * * * * ?")
    public void closeOrderTask() throws Exception {
        System.out.println("================");
        long timeout = Long.parseLong(PropertiesUtil.getProperty("lock.time"));
        Long result = RedisPoolUtil.setnx(REDIS_LOCK, String.valueOf(System.currentTimeMillis() + timeout));
        if (result != null && result.intValue()==1) {
            // 执行关闭订单的业务
            closeOrder(REDIS_LOCK);
        } else {
            log.info("没有获取到了分布式锁{}", REDIS_LOCK);
        }
    }

    private void closeOrder(String redisLock) {
        log.info("获取{},ThreadName:{}",REDIS_LOCK,Thread.currentThread().getName());
        // 设置过期时间
        RedisPoolUtil.expire(REDIS_LOCK, 50);
        // 执行关闭业务
        close();
        // 释放锁
        RedisPoolUtil.del(REDIS_LOCK);
        log.info("释放{},ThreadName:{}",REDIS_LOCK,Thread.currentThread().getName());
    }


    public void close() {
        System.out.println("执行了关闭订单操作");
    }

}

