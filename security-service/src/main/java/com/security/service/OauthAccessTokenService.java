package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.dao.OauthAccessTokenDao;
import com.security.domain.OauthAccessToken;

@Service
public class OauthAccessTokenService {
	
	@Autowired
	private OauthAccessTokenDao oauthAccessTokenDao;
	
	public OauthAccessToken save(OauthAccessToken oauthAccessToken){
		return oauthAccessTokenDao.save(oauthAccessToken);
	}
	
	
	public OauthAccessToken findById(Long id){
		return oauthAccessTokenDao.findOne(id);
	}
}
