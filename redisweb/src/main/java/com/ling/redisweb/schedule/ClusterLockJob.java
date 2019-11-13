package com.ling.redisweb.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @ClassName ClusterLockJob
 * @Deacription TODO
 * @Author ljxia
 * @Date 2019/11/12 14:04
 * @Version 1.0
 **/

@Service
public class ClusterLockJob {

    @Scheduled(cron = "0/5 * * * * *")
    public void lock(){
        System.out.println("enter job" + System.currentTimeMillis());
    }

}
