package com.security.dao;

import com.security.domain.RedDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface RedDetailDao extends PagingAndSortingRepository<RedDetail, Long>, JpaSpecificationExecutor<RedDetail>{

}
