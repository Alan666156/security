package com.security.service;

import org.springframework.stereotype.Service;

import com.security.dao.OauthRefreshTokenDao;
import com.security.domain.OauthRefreshToken;

@Service
public class OauthRefreshTokenService {
	
	public OauthRefreshTokenDao oauthRefreshTokenDao;
	
	public OauthRefreshToken save(OauthRefreshToken oauthRefreshToken){
		return oauthRefreshTokenDao.save(oauthRefreshToken);
	}
	
	public OauthRefreshToken findById(Long id){
		return oauthRefreshTokenDao.findById(id).get();
	}
}
