package com.security.domain;

import lombok.Data;
import lombok.ToString;

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

    private Long userId;
    private String redPacket;
    /**
     * 红包金额（单位为分）
     */
    private BigDecimal amount;
    private Date robTime;
    private Byte isActive;
}