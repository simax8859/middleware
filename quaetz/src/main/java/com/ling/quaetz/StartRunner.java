package com.ling.quaetz;

import com.ling.quaetz.cron.CronScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartRunner implements CommandLineRunner {

    @Autowired
    public CronScheduler scheduleJobs;

    //项目启动就调用schedulerJobs，启动定时任务
    @Override
    public void run(String... args) throws Exception {
        scheduleJobs.schedulerJobs();
        System.out.println(">>>>>>>>>>>>>>>定时任务开始执行<<<<<<<<<<<<<");
    }
}
