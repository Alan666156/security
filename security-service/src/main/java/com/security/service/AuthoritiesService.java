package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.dao.AuthoritiesDao;
import com.security.domain.Authorities;

@Service
public class AuthoritiesService {
	
	@Autowired
	private AuthoritiesDao authoritiesDao;
	
	public Authorities save(Authorities authorities){
		return authoritiesDao.save(authorities);
	}
	
	public Authorities findById(Long id){
		return authoritiesDao.findOne(id);
	}
	
}
