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
     * @throws Exception
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) throws Exception {
        RedRecord redRecord = new RedRecord();
        redRecord.setUserId(dto.getUserId());
        redRecord.setRedPacket(redId);
        redRecord.setTotal(dto.getTotal());
        redRecord.setAmount(BigDecimal.valueOf(dto.getAmount()));
        redRecordDao.save(redRecord);

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
     * @param userId
     * @param redId
     * @param amount
     * @throws Exception
     */
    @Async
    public void recordRobRedPacket(Long userId, String redId, BigDecimal amount) throws Exception {
        RedRobRecord redRobRecord = new RedRobRecord();
        redRobRecord.setUserId(userId);
        redRobRecord.setRedPacket(redId);
        redRobRecord.setAmount(amount);
        redRobRecord.setRobTime(new Date());
        redRobRecordDao.save(redRobRecord);
    }
}
