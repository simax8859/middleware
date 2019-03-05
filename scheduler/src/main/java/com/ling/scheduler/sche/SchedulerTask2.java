package com.ling.scheduler.sche;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class SchedulerTask2 {

    private static final SimpleDateFormat  dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 2000)
    public void reportCurrentTime(){
        System.out.println("当前时间为：" + dateFormat.format(new Date()));
    }
}
