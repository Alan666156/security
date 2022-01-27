package com.security.dao;

import com.security.domain.RedRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface RedRecordDao extends PagingAndSortingRepository<RedRecord, Long>, JpaSpecificationExecutor<RedRecord>{

}
