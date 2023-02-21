package com.security.algorithm.lb;

import lombok.Data;

/**
 * 负载均衡基本参数信息
 *
 * @author: fuhongxing
 * @date: 2021/3/16
 **/
@Data
public class Weigth {
    private String ip;
    /**
     * 静态权重
     */
    private Integer weigth;
    /**
     * 动态权重
     */
    private Integer currentWeigth;

    public Weigth() {
    }

    public Weigth(String ip, Integer weigth) {
        this.ip = ip;
        this.weigth = weigth;
    }

    public Weigth(String ip, Integer weigth, Integer currentWeigth) {
        this.ip = ip;
        this.weigth = weigth;
        this.currentWeigth = currentWeigth;
    }
}
