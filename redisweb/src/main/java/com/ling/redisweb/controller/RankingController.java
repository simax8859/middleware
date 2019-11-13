package com.ling.redisweb.controller;

import com.ling.redisweb.service.RangingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName RankingController
 * @Deacription TODO
 * @Author ljxia
 * @Date 18:34
 * @Version 1.0
 **/

@RestController
public class RankingController {

    @Autowired
    private RangingService rankingService;

    @RequestMapping("/addScore")
    public String addRank(String uid, Integer score) {
        rankingService.rankAdd(uid, score);
        return "success";
    }

    @RequestMapping("/increScore")
    public String increScore(String uid, Integer score) {
        rankingService.increSocre(uid, score);
        return "success";
    }

    @RequestMapping("/rank")
    public Map<String, Long> rank(String uid) {
        Map<String, Long> map = new HashMap<>();
        map.put(uid, rankingService.rankNum(uid));
        return map;
    }

    @RequestMapping("/score")
    public Long rankNum(String uid) {
        return rankingService.score(uid);
    }

    @RequestMapping("/scoreByRange")
    public Set<ZSetOperations.TypedTuple<Object>> scoreByRange(Integer start, Integer end) {
        return rankingService.rankWithScore(start,end);
    }





    @RequestMapping("/sale/increScore")
    public String increSaleScore(String uid, Integer score) {
        rankingService.increSaleSocre(uid, score);
        return "success";
    }


    @RequestMapping("/sale/userScore")
    public Map<String,Object> userScore(String uid,String name) {
        return rankingService.userRank(uid,name);
    }

    @RequestMapping("/sale/top")
    public List<Map<String,Object>> reverseZRankWithRank(long start, long end) {
        return rankingService.reverseZRankWithRank(start,end);
    }


    @RequestMapping("/sale/scoreByRange")
    public List<Map<String,Object>> saleScoreByRange(Integer start, Integer end) {
        return rankingService.saleRankWithScore(start,end);
    }

}
