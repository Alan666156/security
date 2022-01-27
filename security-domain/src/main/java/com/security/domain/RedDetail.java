package com.security.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 红包明细
 */
@Data
@ToString
@Entity
@Table(name="red_detail")
public class RedDetail  extends AbstractEntity{
    private Long recordId;
    private BigDecimal amount;
    private Byte isActive;
    private Date createTime;
}