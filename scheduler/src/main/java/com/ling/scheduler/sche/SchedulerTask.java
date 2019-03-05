package com.ling.scheduler.sche;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerTask {

    private int count = 0;

    @Scheduled(cron = "*/6 * * * * ?")
    private void process(){
        System.out.println("测试spring 自带的定时任务 第" + (++count) + "次！");
    }
}
