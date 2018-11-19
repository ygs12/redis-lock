package com.distributed.demo.redislock.task;

import com.distributed.demo.redislock.service.OrderService;
import com.distributed.demo.redislock.utils.RedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    // 定义Redis锁名
    public static final String REDIS_ORDER_LOCK = "redis_order_lock";
    // 定义redis的key超时时间
    public static final Long timeOut = 50000L;

    @Autowired
    private OrderService orderService;

    @Scheduled(cron="0/1 * * * * ?")
    public void doTask() {
        System.out.println("执行订单关闭任务开始");
        Long result = RedisPoolUtil.setnx(REDIS_ORDER_LOCK, String.valueOf(System.currentTimeMillis() + timeOut));
        if (result != null && result.intValue() ==1) {
            // 执行关闭订单业务操作
            closeOrderTask(REDIS_ORDER_LOCK);
        } else {
            log.info("没有获取到锁{}", REDIS_ORDER_LOCK);
        }

        System.out.println("执行订单关闭任务结束");
    }


    private void closeOrderTask(String redisOrderLock) {
        log.info("获取到锁{}", REDIS_ORDER_LOCK);
        // 为了避免死锁问题，设置锁的过期时间
        RedisPoolUtil.expire(redisOrderLock, 50);
        // 执行业务操作
        orderService.closeOrder();
        log.info("释放锁{}", REDIS_ORDER_LOCK);
        RedisPoolUtil.del(REDIS_ORDER_LOCK);
    }
}

