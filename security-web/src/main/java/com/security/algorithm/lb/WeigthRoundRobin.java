package com.security.algorithm.lb;

import java.util.HashMap;
import java.util.Map;

/**
 * 平滑轮询
 * @Author: fuhongxing
 * @Date: 2021/3/16
 **/
public class WeigthRoundRobin {

    private static Map<String, Weigth> weigthMap = new HashMap<>();

    public static String getServer(){

        int totalWeigth = 0;
        for (Integer weigth : ServerIps.weigth.values()) {
            totalWeigth += weigth;
        }

        if(weigthMap.isEmpty()){
            ServerIps.weigth.forEach((ip, weigth) ->{
                weigthMap.put(ip, new Weigth(ip, weigth, 0));
            });
        }

        //总的权重
        for (Weigth weigth : weigthMap.values()) {
            //当前权重 + 动态权重
            weigth.setCurrentWeigth(weigth.getCurrentWeigth() + weigth.getWeigth());
        }

        Weigth maxWeigth = null;
        for (Weigth weigth : weigthMap.values()) {
            if(maxWeigth == null || weigth.getCurrentWeigth() > maxWeigth.getCurrentWeigth()){
                maxWeigth = weigth;
            }
        }
        maxWeigth.setCurrentWeigth(maxWeigth.getCurrentWeigth() - totalWeigth);
        return maxWeigth.getIp();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println("负载均衡：-->" + WeigthRoundRobin.getServer());
        }
    }
}
