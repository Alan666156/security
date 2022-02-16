package com.security.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 发红包请求时接收的参数对象
 */
@Data
@Accessors(chain = true)
@ToString
public class RedPacketDto implements Serializable {

    /**发红包用户*/
    @NotBlank(message = "用户ID不能为空")
    private Long userId;
    private String name;
    /**红包个数*/
    @NotBlank(message = "红包个数不能为空")
    private Integer total;
    /**指定总金额-单位为分*/
    @NotBlank(message = "红包总金额不能为空")
    private Integer amount;
}