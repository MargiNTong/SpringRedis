package com.example.demo.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
public class DemoController {

    private static String redis_Lock = "RedisLock";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DefaultRedisScript redisScript;

    @Autowired
    private Redisson redisson;

    @Value("${server.port}")
    private String port;

    @GetMapping("/buyGood")
    public String buyGood(){
            String currentUser = UUID.randomUUID().toString()+Thread.currentThread().getName();
        RLock lock = redisson.getLock(redis_Lock);
        lock.lock();
        try {
//                boolean isPass = stringRedisTemplate.opsForValue().setIfAbsent(redis_Lock,currentUser);
//                if (!isPass) {
//                    System.out.println(currentUser + "抢锁失败！");
//                    return "抢锁失败！";
//                }

                String result;
                String num = stringRedisTemplate.opsForValue().get("goods:001");
                int currentNum = num == null ? 0 : Integer.parseInt(num);
                if (currentNum > 0) {
                    int realNum = currentNum - 1;
                    stringRedisTemplate.opsForValue().set("goods:001", String.valueOf(realNum));
                    System.out.println("成功购买商品，库存还剩下：" + realNum + "件，当前服务端口为：" + port + "!");
                    result = "成功购买商品，库存还剩下：" + realNum + "件，当前服务端口为：" + port + "!";
                } else {
                    System.out.println("该商品已售罄，当前服务端口为：" + port + "!");
                    result = "该商品已售罄，当前服务端口为：" + port + "!";
                }
                return result;
            }finally {
            if(lock.isLocked()&& lock.isHeldByCurrentThread())
                lock.unlock();
                /*调用Lua脚本*/
//                    stringRedisTemplate.execute(redisScript, Collections.singletonList(redis_Lock),currentUser);
                /*使用Redis事务保证原子性*/
//                while(true){
//                    stringRedisTemplate.setEnableTransactionSupport(true);
//                    stringRedisTemplate.watch(redis_Lock);
//                    if (stringRedisTemplate.opsForValue().get(redis_Lock).equalsIgnoreCase(currentUser)){
//                        stringRedisTemplate.multi();
//                        stringRedisTemplate.delete(redis_Lock);
//                        List<Object> list = stringRedisTemplate.exec();
//                        if (list == null){
//                            continue;
//                        }
//                    }
//                    stringRedisTemplate.unwatch();
//                    break;
//                }
                /*若存在多个客户端，无法保证原子性*/
//                if (stringRedisTemplate.opsForValue().get(redis_Lock).equals(currentUser)){
//                    stringRedisTemplate.delete(redis_Lock);
//                }
            }
    }
}
