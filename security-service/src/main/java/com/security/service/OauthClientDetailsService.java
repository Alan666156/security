package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.dao.OauthClientDetailsDao;
import com.security.domain.OauthClientDetails;

@Service
public class OauthClientDetailsService {
	
	@Autowired
	private OauthClientDetailsDao oauthClientDetailsDao;
	
	public OauthClientDetails save(OauthClientDetails oauthClientDetails){
		return oauthClientDetailsDao.save(oauthClientDetails);
	}
	
	public OauthClientDetails findById(Long id){
		return oauthClientDetailsDao.findOne(id);
	}
}
