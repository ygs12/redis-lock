package com.distributed.demo.redislock.service;

import org.springframework.stereotype.Service;

/**
 * @ProjectName: redis-lock
 * @Auther: GERRY
 * @Date: 2018/11/19 14:53
 * @Description:
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public void closeOrder() {
        System.out.println("执行了关闭订单的操作");
    }
}
