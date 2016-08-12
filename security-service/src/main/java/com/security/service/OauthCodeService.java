package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.dao.OauthCodeDao;
import com.security.domain.OauthCode;

@Service
public class OauthCodeService {
	
	@Autowired
	private OauthCodeDao oauthCodeDao;
	
	public OauthCode save(OauthCode oauthCode){
		return oauthCodeDao.save(oauthCode);
	}
	
	public OauthCode findById(Long id){
		return oauthCodeDao.findOne(id);
	}
}
