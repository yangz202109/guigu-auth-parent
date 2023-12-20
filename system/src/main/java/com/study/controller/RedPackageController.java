package com.study.controller;

import cn.hutool.core.util.IdUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 抢红包
 *
 * @author yangz
 * @createTime 2023/12/19 - 14:21
 */
@RestController
public class RedPackageController {

    public static final String RED_PACKAGE_KEY = "redpackage:";
    public static final String RED_PACKAGE_CONSUME_KEY = "redpackage:consume:";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 拆分+发送红包
     * /send?totalMoney=100&redPackageNumber=5
     *
     * @param totalMoney       总金额
     * @param redPackageNumber 红包的个数
     * @return
     */
    @PostMapping("/send")
    public String sendRedPackage(int totalMoney, int redPackageNumber) {
        //1 拆红包，总金额拆分成多少个红包，每个小红包里面包多少钱
        Integer[] splitRedPackages = splitRedPackage(totalMoney, redPackageNumber);

        //2 红包的全局ID
        String key = RED_PACKAGE_KEY + IdUtil.simpleUUID();

        //3 采用list存储红包并设置过期时间
        redisTemplate.opsForList().leftPushAll(key, splitRedPackages);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);

        int[] num = Arrays.stream(splitRedPackages).mapToInt(Integer::valueOf).toArray();

        return key + "\t" + "\t" + Arrays.toString(num);
    }

    /**
     * 抢红包
     * /rob?redPackageKey=红包UUID&userId=1
     *
     * @param redPackageKey 红包id
     * @param userId        用户id
     * @return 提示
     */
    @RequestMapping("/rob")
    public String rodRedPackage(String redPackageKey, String userId) {
        //1 验证某个用户是否抢过红包
        Object redPackage = redisTemplate.opsForHash().get(RED_PACKAGE_CONSUME_KEY + redPackageKey, userId);

        //2 没有抢过就开抢，否则返回-2表示抢过
        if (redPackage == null) {
            // 2.1 从list里面出队一个红包，抢到了一个
            Object partRedPackage = redisTemplate.opsForList().leftPop(RED_PACKAGE_KEY + redPackageKey);
            if (partRedPackage != null) {
                //2.2 抢到手后，记录进去hash表示谁抢到了多少钱的某一个红包
                redisTemplate.opsForHash().put(RED_PACKAGE_CONSUME_KEY + redPackageKey, userId, partRedPackage);
                System.out.println("用户: " + userId + "\t 抢到多少钱红包: " + partRedPackage);
                //TODO 后续异步进mysql或者RabbitMQ进一步处理

                return String.valueOf(partRedPackage);
            }
            //抢完
            return "errorCode:-1,红包抢完了";
        }
        //3 某个用户抢过了，不可以作弊重新抢
        return "errorCode:-2, message: " + "\t" + userId + " 用户你已经抢过红包了";
    }

    /**
     * 1 拆完红包总金额+每个小红包金额别太离谱
     *
     * @param totalMoney       总金额
     * @param redPackageNumber 红包的个数
     * @return 红包金额数组
     */
    private Integer[] splitRedPackage(int totalMoney, int redPackageNumber) {
        int useMoney = 0;
        Integer[] redPackageNumbers = new Integer[redPackageNumber];
        Random random = new Random();

        for (int i = 0; i < redPackageNumber; i++) {
            if (i == redPackageNumber - 1) {
                redPackageNumbers[i] = totalMoney - useMoney;
            } else {
                int avgMoney = (totalMoney - useMoney) * 2 / (redPackageNumber - i);
                redPackageNumbers[i] = 1 + random.nextInt(avgMoney - 1);
            }
            useMoney = useMoney + redPackageNumbers[i];
        }
        return redPackageNumbers;
    }
}
