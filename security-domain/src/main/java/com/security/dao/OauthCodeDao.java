package com.security.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.security.domain.OauthCode;

public interface OauthCodeDao extends PagingAndSortingRepository<OauthCode, Long>, JpaSpecificationExecutor<OauthCode>{

}
