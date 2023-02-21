package com.security.dao;

import com.security.domain.RedRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 * 发红包记录dao
 *
 * @author fuhongxing
 */
public interface RedRecordDao extends PagingAndSortingRepository<RedRecord, Long>, JpaSpecificationExecutor<RedRecord> {

    /**
     * 根据红包Code查询
     *
     * @param redPacket 红包code标识
     * @return
     */
    RedRecord findByRedPacket(String redPacket);
}
