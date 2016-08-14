package com.security.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.security.domain.OauthApprovals;

public interface OauthApprovalsDao extends PagingAndSortingRepository<OauthApprovals, Long>, JpaSpecificationExecutor<OauthApprovals>{

}
