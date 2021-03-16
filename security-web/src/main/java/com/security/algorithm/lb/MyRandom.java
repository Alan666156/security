package com.security.algorithm.lb;

import java.util.Random;

/**
 * 随机算法
 * @Author: fuhongxing
 * @Date: 2021/3/16
 **/
public class MyRandom {
    /**
     * 随机返回ip
     * @return
     */
    public static String getServer(){
        Random random = new Random();
        int temp = random.nextInt(ServerIps.ip.size());
        return ServerIps.ip.get(temp);
    }

    /**
     * 根据权重返回ip
     * @return
     */
    public static String getIpByWeigth(){
        //总的权重
        int totalWeigth = 0;
        for (Integer weigth : ServerIps.weigth.values()) {
            totalWeigth += weigth;
        }

        Random random = new Random();
        int temp = random.nextInt(ServerIps.weigth.size());
        for (String ip : ServerIps.weigth.keySet()) {
            Integer weigth = ServerIps.weigth.get(ip);
            if (temp < weigth){
                return ip;
            }
            temp  = temp - weigth;
        }
        return null;
    }
}
