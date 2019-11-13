package com.ling.redisweb.controller;

import com.ling.redisweb.domain.User;
import com.ling.redisweb.mapper.UserMapper;
import com.ling.redisweb.service.RedisService;
import com.ling.redisweb.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


/**
 * @ClassName TestController
 * @Deacription TODO
 * @Author ljxia
 * @Date 18:04
 * @Version 1.0
 **/
@RestController
public class UserController {

    private static final String key = "userCache_";

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisService redisService;

    @Resource
    private UserService userService;

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){

        return "hello world!";
    }

    @RequestMapping("/test")
    @ResponseBody
    public User getUser(String id){
        return userMapper.find(id);
    }

    @RequestMapping("/getUserCache")
    @ResponseBody
    public User getUserCache(String id){

        // step1 先从redis里面拿值
        User user = (User) redisService.get(key + id);

        // step2 如果拿不到就从DB中取值
        if(user == null){
            User userDB = userMapper.find(id);
            System.out.println("fresh value from DB id:" + id);

            // step3 DB非空情况刷新redis的值
            if (userDB != null){
                redisService.set(key + id, userDB);
                return userDB;
            }
        }
        return user;
    }

    @RequestMapping("/getByCache")
    @ResponseBody
    public User getByCache(String id){
        User user = userService.findById(id);
        return user;
    }

    @RequestMapping(value = "/getExpire", method = RequestMethod.GET)
    @ResponseBody
    public User findByIdTtl(String id){
        User u = new User();
        try {
            u = userService.findByIdTtl(id);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return u;
    }
}































