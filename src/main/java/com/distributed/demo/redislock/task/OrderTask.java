package com.distributed.demo.redislock.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: redis-lock
 * @Auther: GERRY
 * @Date: 2018/11/18 18:58
 * @Description:
 */
@Component
public class OrderTask {

    //@Scheduled(cron = "0 */1 * * * ?")
    @Scheduled(cron = "0/20 * * * * ?")
    public void closeOrderTask() throws Exception {
        System.out.println("================");
    }
}
