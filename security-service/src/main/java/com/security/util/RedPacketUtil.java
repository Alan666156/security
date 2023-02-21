package com.security.util;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 二倍均值法
 * 根据每次剩余的总金额M和剩余人数N，执行M/N再乘以2的操作得到一个边界值E，然后制定一个从0到E的随机区间，在这个随机区间内将产生一个随机金额R，此时总金额M将更新为M-R，
 * 剩余人数N更新为N-1。再继续重复上述执行流程，以此类推，直至最终剩余人数N-1为0，即代表随机数已经产生完毕。
 *
 * @author: fuhx
 * @date: 2020/3/15
 */
@Slf4j
public class RedPacketUtil {

    public static final BigDecimal ONE = BigDecimal.valueOf(1);
    public static final BigDecimal TWO = BigDecimal.valueOf(2);

    /**
     * 二倍均值法，金额单位为元
     *
     * @param totalAmount    红包总金额
     * @param totalPeopleNum 红包总个数
     * @return
     */
    public static List<BigDecimal> divideRedPackage(BigDecimal totalAmount, Integer totalPeopleNum) {
        List<BigDecimal> amountList = new ArrayList<>(totalPeopleNum);
        if (totalAmount.compareTo(BigDecimal.ZERO) > 0 && totalPeopleNum > 0) {
            // 剩余总金额
            BigDecimal restAmount = totalAmount;
            // 剩余总人数
            BigDecimal restPeopleNum = BigDecimal.valueOf(totalPeopleNum);

            for (int i = 0; i < totalPeopleNum - 1; i++) {
                // 随机范围：[1，剩余人均金额的两倍)，左闭右开
                // 计算边界值，保留2位小数，四舍五入
                BigDecimal temp = restAmount.divide(restPeopleNum, 2, RoundingMode.HALF_UP).multiply(TWO).subtract(ONE);
                BigDecimal amount = RandomUtil.randomBigDecimal(temp).add(ONE);
                restAmount = restAmount.subtract(amount);
                restPeopleNum = restPeopleNum.subtract(ONE);
                amountList.add(amount);
            }
            amountList.add(restAmount);
        }
        return amountList;
    }

    /**
     * 二倍均值法，金额以分为单位，金额都为整数型
     * ps:传值需要注意：假设发10个红包，金额如果为5(说明只有5分钱)，那么此时金额是无法拆分为10份的，产生随机数时会异常
     *
     * @param totalAmount    红包总金额
     * @param totalPeopleNum 红包总个数
     * @return
     */
    public static List<Integer> divideRedPackage2(Integer totalAmount, Integer totalPeopleNum) {
        List<Integer> amountList = new ArrayList<>(totalPeopleNum);
        if (totalAmount > 0 && totalPeopleNum > 0) {
            // 剩余总金额
            int restAmount = totalAmount;
            // 剩余总人数
            int restPeopleNum = totalPeopleNum;
            Random random = new Random();
            for (int i = 0; i < totalPeopleNum - 1; i++) {
                // 随机范围：[1，剩余人均金额的两倍)，左闭右开
                int amount = 0;
                amount = random.nextInt(restAmount / restPeopleNum * 2 - 1) + 1;
                restAmount -= amount;
                restPeopleNum--;
                amountList.add(amount);
            }
            amountList.add(restAmount);
        }
        return amountList;
    }

    public static void main(String[] args) {
        // 原来的容量
        int oldCapacity = 6;
        // 新的容量，原来容量的1.5倍。
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        System.out.println(newCapacity);
    }
}