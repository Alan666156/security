package com.security.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.security.domain.Authorities;

public interface AuthoritiesDao extends PagingAndSortingRepository<Authorities, Long>, JpaSpecificationExecutor<Authorities>{

}
