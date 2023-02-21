package com.security.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 红包明细
 *
 * @author fuhongxing
 */
@Data
@ToString
@Entity
@Table(name = "red_detail")
public class RedDetail extends AbstractEntity {
    /**
     * 关联红包记录表id
     */
    private Long recordId;
    /**
     * 明细金额
     */
    private BigDecimal amount;
    /**
     * 是否激活使用，0未使用，1已使用
     */
    @Column(name = "is_active", columnDefinition = "tinyint")
    private Integer active;

}