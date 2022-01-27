package com.security.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 发红包请求时接收的参数对象
 */
@Data
@ToString
public class RedPacketDto {
    /**发红包用户*/
    private Long userId;
    /**红包个数*/
    private Integer total;

    /**指定总金额-单位为分*/
    private Integer amount;
}