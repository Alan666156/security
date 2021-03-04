package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.dao.OauthApprovalsDao;
import com.security.domain.OauthApprovals;

@Service
public class OauthApprovalsService {
	
	@Autowired
	private OauthApprovalsDao oauthApprovalsDao;
	
	public OauthApprovals save(OauthApprovals oauthApprovals){
		return oauthApprovalsDao.save(oauthApprovals);
	}
	
	public OauthApprovals findById(Long id){
		return oauthApprovalsDao.findById(id).get();
	}
	
}
