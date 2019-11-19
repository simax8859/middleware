package com.ling.redisweb.controller;

import com.ling.redisweb.config.RedisConfig;
import com.ling.redisweb.domain.RedPacketInfo;
import com.ling.redisweb.domain.RedPacketInfoExample;
import com.ling.redisweb.domain.RedPacketRecord;
import com.ling.redisweb.mapper.RedPacketInfoMapper;
import com.ling.redisweb.mapper.RedPacketRecordMapper;
import com.ling.redisweb.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

/**
 * @ClassName RedPacketController
 * @Deacription TODO
 * @Author ljxia
 * @Date 2019/11/15 18:02
 * @Version 1.0
 **/

@RestController
public class RedPacketController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedPacketInfoMapper redPacketInfoMapper;

    @Autowired
    private RedPacketRecordMapper redPacketRecordMapper;

    private static final String TOTAL_NUM = "_totalNum";
    private static final String TOTAL_AMOUNT = "_totalAmount";

    /***
     *
     * @param uid
     * @param totalNum
     * @return
     */
    @ResponseBody
    @RequestMapping("/addPacket")
    public String saveRedPacket(Integer uid, Integer totalNum, Integer totalAmount) {
        RedPacketInfo record = new RedPacketInfo();
        record.setUid(uid);
        record.setTotalAmount(totalAmount);
        record.setTotalPacket(totalNum);
        record.setCreateTime(new Date());
        record.setRemainingAmount(totalAmount);
        record.setRemainingPacket(totalNum);
        long redPacketId = System.currentTimeMillis();   //此时无法保证红包id唯一，最好是用雪花算法进行生成分布式系统唯一键
        record.setRedPacketId(redPacketId);
        redPacketInfoMapper.insert(record);
        redisService.set(redPacketId + "_totalNum", totalNum + "");
        redisService.set(redPacketId + "_totalAmount", totalAmount + "");
        return "success";
    }

    /**
     * 抢红包
     *
     * @param redPacketId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getPacket")
    public Integer getRedPacket(long redPacketId) {
        String redPacketName = redPacketId + TOTAL_NUM;
        String num = (String) redisService.get(redPacketName);
        if (StringUtils.isNotBlank(num)) {
            return Integer.parseInt(num);
        }
        return 0;
    }

    /**
     * 拆红包
     *
     * @param redPacketId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getRedPacketMoney")
    public String getRedPacketMoney(int uid, long redPacketId) {
        Integer randomAmount = 0;
        String redPacketName = redPacketId + TOTAL_NUM;
        String totalAmountName = redPacketId + TOTAL_AMOUNT;
        String num = (String) redisService.get(redPacketName);
        if (StringUtils.isBlank(num) || Integer.parseInt(num) == 0) {
            return "抱歉！红包已经抢完了";
        }
        // 加一个判断，如果红包剩余的个数只有1了，那么直接返回再执行删除key的操作？
        String totalAmount = (String) redisService.get(totalAmountName);
        if (StringUtils.isNotBlank(totalAmount)) {
            Integer totalAmountInt = Integer.parseInt(totalAmount);
            Integer totalNumInt = Integer.parseInt(num);
            Integer maxMoney = totalAmountInt / totalNumInt * 2;
            Random random = new Random();
            // 这里有可能为0，所以是不是判断一下如果为0自动处理成1分
            // 这里取不到上限，所以无论如何还是能剩下1分钱的
            randomAmount = random.nextInt(maxMoney);
        }
        // 课堂作业：lua脚本将这两个命令一起请求
        redisService.decr(redPacketName, 1);
        //redis decreby功能
        redisService.decr(totalAmountName,randomAmount);
        updateRacketInDB(uid, redPacketId,randomAmount);
        return randomAmount + "";
    }

    public void updateRacketInDB(int uid, long redPacketId, int amount) {
        RedPacketRecord redPacketRecord = new RedPacketRecord();
        redPacketRecord.setUid(uid);
        redPacketRecord.setRedPacketId(redPacketId);
        redPacketRecord.setAmount(1111);
        redPacketRecord.setCreateTime(new Date());
        redPacketRecordMapper.insertSelective(redPacketRecord);
        //这里应该查出RedPacketInfo的数量，将总数量和总金额减去
    }


}