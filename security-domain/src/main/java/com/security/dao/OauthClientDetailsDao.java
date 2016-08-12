package com.security.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.security.domain.OauthClientDetails;

public interface OauthClientDetailsDao extends PagingAndSortingRepository<OauthClientDetails, Long>, JpaSpecificationExecutor<OauthClientDetails>{

}
