package com.security.algorithm.lb;

import cn.hutool.core.map.MapUtil;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 负载均衡基本参数信息
 * @Author: fuhongxing
 * @Date: 2021/3/16
 **/
@Data
public class ServerIps {
    /**
     * ip
     */
    public static List<String> ip = null;
    /**
     * 权重
     */
    public static Map<String, Integer> weigth = MapUtil.newConcurrentHashMap();

    static {
        ip = Arrays.asList("192.168.0.1", "192.168.0.2", "192.168.0.3", "192.168.0.4", "192.168.0.5", "192.168.0.6");

        weigth.put("192.168.0.1", 100);
        weigth.put("192.168.0.2", 20);
        weigth.put("192.168.0.3", 30);
        weigth.put("192.168.0.4", 40);
        weigth.put("192.168.0.5", 50);
        weigth.put("192.168.0.6", 10);
    }


}
