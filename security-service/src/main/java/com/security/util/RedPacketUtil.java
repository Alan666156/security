package com.security.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 二倍均值法
 * 根据每次剩余的总金额M和剩余人数N，执行M/N再乘以2的操作得到一个边界值E，然后制定一个从0到E的随机区间，在这个随机区间内将产生一个随机金额R，此时总金额M将更新为M-R，
 * 剩余人数N更新为N-1。再继续重复上述执行流程，以此类推，直至最终剩余人数N-1为0，即代表随机数已经产生完毕。
 * @author: fuhx
 * @date: 2020/3/15
 */
public class RedPacketUtil {

    /**
     * 二倍均值法，金额以分为单位
     * @param totalAmount
     * @param totalPeopleNum
     * @return
     */
    public static List<Integer> divideRedPackage(Integer totalAmount, Integer totalPeopleNum) {
        List<Integer> amountList = new ArrayList<Integer>();
        if (totalAmount>0 && totalPeopleNum>0){
            Integer restAmount = totalAmount;
            Integer restPeopleNum = totalPeopleNum;
            Random random = new Random();
            for (int i = 0; i < totalPeopleNum - 1; i++) {
                // 随机范围：[1，剩余人均金额的两倍)，左闭右开
                int amount = random.nextInt(restAmount / restPeopleNum * 2 - 1) + 1;
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
    }
}