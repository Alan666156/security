package com.security.dao;

import com.security.domain.RedRobRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface RedRobRecordDao extends PagingAndSortingRepository<RedRobRecord, Long>, JpaSpecificationExecutor<RedRobRecord>{

}
