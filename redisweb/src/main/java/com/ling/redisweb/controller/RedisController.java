package com.ling.redisweb.controller;

import com.ling.redisweb.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @ClassName RedisController
 * @Deacription TODO
 * @Author ljxia
 * @Date 2019/11/7 15:50
 * @Version 1.0
 **/

@RestController
public class RedisController {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedisService redisService;

    @RequestMapping("/redis/setAndGet")
    public String setAndGetValue(String name, String value){
        redisTemplate.opsForValue().set(name, value);
        return (String)redisTemplate.opsForValue().get(name);
    }

    @RequestMapping("/redis/setAndGet1")
    public String setAndGetValue1(String name, String value){
        redisService.set(name, value);
        return (String)redisService.get(name);
    }


}
