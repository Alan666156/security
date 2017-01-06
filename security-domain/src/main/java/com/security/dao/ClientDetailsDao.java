package com.security.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.security.domain.ClientDetails;

public interface ClientDetailsDao extends PagingAndSortingRepository<ClientDetails, Long>, JpaSpecificationExecutor<ClientDetails>{

	public ClientDetails findByAppIdAndAppSecret(String appId, String appSecret);

}
