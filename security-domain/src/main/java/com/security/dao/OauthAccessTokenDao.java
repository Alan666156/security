package com.security.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.security.domain.OauthAccessToken;


public interface OauthAccessTokenDao extends PagingAndSortingRepository<OauthAccessToken, Long>, JpaSpecificationExecutor<OauthAccessToken>{

}
