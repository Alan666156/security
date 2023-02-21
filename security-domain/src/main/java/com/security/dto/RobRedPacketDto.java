package com.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 抢红包请求时接收的参数对象
 *
 * @author fuhongxing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RobRedPacketDto implements Serializable {

    /**
     * 发红包用户id
     */
    @NotBlank(message = "用户ID不能为空")
    private Long userId;

    /**
     * 红包编码
     */
    @NotBlank(message = "红包个数不能为空")
    private String redId;


}