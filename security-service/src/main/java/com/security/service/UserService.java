package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.dao.UserDao;
import com.security.domain.User;

@Service
public class UserService {
    
	@Autowired
	private UserDao userDao;
	
	public User save(User user){
		return userDao.save(user);
	}
	
	public User findById(Long id){
		return userDao.findOne(id);
	}
}
