package com.security.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 发红包记录
 *
 * @author fuhongxing
 */
@Data
@Entity
@Table(name = "red_record")
public class RedRecord extends AbstractEntity {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 红包全局唯一标识串
     */
    private String redPacket;
    /**
     * 人数
     */
    private Integer total;
    /**
     * 总金额（单位为分）
     */
    private BigDecimal amount;

    /**
     * 是否激活使用，0未使用，1已使用
     */
    @Column(name = "is_active", columnDefinition = "tinyint")
    private Integer active;

}