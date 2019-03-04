package com.ling.redis;


import com.ling.redis.model.User;
import com.ling.redis.service.RedisService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedisService {

    @Autowired
    private RedisService redisService;

    @Test
    public void testString() throws Exception {
        redisService.set("ling", "test001");
        System.out.println(redisService.get("ling"));
        Assert.assertEquals("test001", redisService.get("ling"));
    }

    @Test
    public void testObj() throws Exception {
        User user=new User("lingsemail@outlook.com", "ling", "123456", "ling","2019");
        redisService.set("user",user);
        User userR=(User) redisService.get("user");
        System.out.println("userR== "+userR.toString());
    }

}
