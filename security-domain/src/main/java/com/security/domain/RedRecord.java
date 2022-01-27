package com.security.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 抢红包记录
 */
@Data
@Entity
@Table(name="red_record")
public class RedRecord extends AbstractEntity{
    private Long userId;
    /**红包全局唯一标识串*/
    private String redPacket;
    /**人数*/
    private Integer total;
    /**总金额（单位为分）*/
    private BigDecimal amount;
    private Byte isActive;

}