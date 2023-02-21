package com.security.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发红包请求时接收的参数对象
 *
 * @author fuhongxing
 */
@Data
@Accessors(chain = true)
@ToString
public class RedPacketDto implements Serializable {

    /**
     * 发红包用户id
     */
    @NotBlank(message = "用户ID不能为空")
    private Long userId;
    /**
     * 发红包用户
     */
    private String name;
    /**
     * 红包个数
     */
    @NotBlank(message = "红包个数不能为空")
    private Integer quantity;
    /**
     * 指定总金额-单位为分
     */
    @NotBlank(message = "红包总金额不能为空")
    private Integer amount;
    /**
     * 指定总金额-单位为分
     */
    @NotBlank(message = "红包总金额不能为空")
    private BigDecimal totalAmount;

    public RedPacketDto() {
    }

    public RedPacketDto(Long userId, Integer quantity, Integer amount) {
        this.userId = userId;
        this.quantity = quantity;
        this.amount = amount;
    }
}