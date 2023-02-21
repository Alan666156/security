package com.security.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 抢红包记录
 *
 * @author fuhongxing
 */
@Data
@ToString
@Entity
@Table(name = "red_rob_record")
public class RedRobRecord extends AbstractEntity {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 红包唯一编码
     */
    private String redPacket;
    /**
     * 红包金额（单位为分）
     */
    private BigDecimal amount;
    /**
     * 抢红包时间
     */
    private Date robTime;
    /**
     * 是否激活使用，0未使用，1已使用
     */
    @Column(name = "is_active", columnDefinition = "tinyint")
    private Integer active;
}