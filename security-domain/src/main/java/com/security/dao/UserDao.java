package com.security.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.security.domain.User;


public interface UserDao extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User>{

}
