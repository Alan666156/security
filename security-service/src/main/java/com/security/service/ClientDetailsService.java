package com.security.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.security.dao.ClientDetailsDao;
import com.security.domain.ClientDetails;
import com.security.vo.PageFormVo;

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
	
	/**
	 * 查询所有用户信息
	 * @param oauthClientDetails
	 * @param form
	 * @return
	 */
	public Page<ClientDetails> findAll(final ClientDetails oauthClientDetails, PageFormVo form){
		
		return clientDetailsDao.findAll(new Specification<ClientDetails>() {
			@Override
			public Predicate toPredicate(Root<ClientDetails> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();

				if (oauthClientDetails != null) {
					// 按照appId模糊查询
					if (!StringUtils.isEmpty(oauthClientDetails.getAppId())) {
						list.add(cb.like(root.<String> get("clientId"), '%' + oauthClientDetails.getAppId() + '%'));
					}

					// 按照用户状态模糊查询
//					if (!StringUtils.isEmpty(customer.getStatus())) {
//						list.add(cb.equal(root.<String> get("status"), customer.getStatus()));
//					} else {
//						Predicate p = cb.or(cb.equal(root.<String> get("status"), Customer.Status.注册.name()), cb.equal(root.<String> get("status"), Customer.Status.认证.name()));
//						list.add(p);
//					}
				}
				Predicate[] predicate = new Predicate[list.size()];
				return cb.and(list.toArray(predicate));
			}
		}, form);
	}
}
