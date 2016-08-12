package com.security.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.security.domain.OauthRefreshToken;

public interface OauthRefreshTokenDao extends PagingAndSortingRepository<OauthRefreshToken, Long>, JpaSpecificationExecutor<OauthRefreshToken>{

}
