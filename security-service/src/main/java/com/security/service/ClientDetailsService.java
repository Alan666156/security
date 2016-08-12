package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.dao.ClientDetailsDao;
import com.security.domain.ClientDetails;

@Service
public class ClientDetailsService {
	
	@Autowired
	private ClientDetailsDao clientDetailsDao;
	
	public ClientDetails save(ClientDetails clientDetails){
		return clientDetailsDao.save(clientDetails);
	}
	
	public ClientDetails findById(Long id){
		return clientDetailsDao.findOne(id);
	}
}
