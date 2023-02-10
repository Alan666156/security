package com.security.service;

import com.security.dao.RedDetailDao;
import com.security.dao.RedRecordDao;
import com.security.dao.RedRobRecordDao;
import com.security.domain.RedDetail;
import com.security.domain.RedRecord;
import com.security.domain.RedRobRecord;
import com.security.dto.RedPacketDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author fuhongxing
 * @date 2021/4/5 13:44
 */
@Slf4j
@Service
public class RedService {
    @Autowired
    private RedRecordDao redRecordDao;
    @Autowired
    private RedRobRecordDao redRobRecordDao;
    @Autowired
    private RedDetailDao redDetailDao;

    /**
     * 发红包记录
     *
     * @param dto
     * @param redId
     * @param list
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) {
        // 生成发红包记录
        RedRecord redRecord = new RedRecord();
        redRecord.setUserId(dto.getUserId());
        redRecord.setRedPacket(redId);
        redRecord.setTotal(dto.getTotal());
        redRecord.setAmount(BigDecimal.valueOf(dto.getAmount()));
        redRecordDao.save(redRecord);

        // 生成红包明细（二倍均值法，已提前生成红包个数和对应的金额）
        RedDetail detail;
        for (Integer i : list) {
            detail = new RedDetail();
            detail.setRecordId(redRecord.getId());
            detail.setAmount(BigDecimal.valueOf(i));
            redDetailDao.save(detail);
        }
    }

    /**
     * 抢红包记录
     *
     * @param userId 用户id
     * @param redId  红包key
     * @param amount 金额
     */
    @Async
    public void recordRobRedPacket(Long userId, String redId, BigDecimal amount) {
        RedRobRecord redRobRecord = new RedRobRecord();
        redRobRecord.setUserId(userId);
        redRobRecord.setRedPacket(redId);
        redRobRecord.setAmount(amount);
        redRobRecord.setRobTime(new Date());
        redRobRecordDao.save(redRobRecord);
    }
}
