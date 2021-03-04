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

import com.security.dao.OauthClientDetailsDao;
import com.security.domain.OauthClientDetails;
import com.security.vo.PageFormVo;

@Service
public class OauthClientDetailsService {
	
	@Autowired
	private OauthClientDetailsDao oauthClientDetailsDao;
	
	public OauthClientDetails save(OauthClientDetails oauthClientDetails){
		return oauthClientDetailsDao.save(oauthClientDetails);
	}
	
	public OauthClientDetails findById(Long id){
		return oauthClientDetailsDao.findById(id).get();
	}
	
	/**
	 * 根据appid和secret查询
	 * @param clientId
	 * @param clientSecret
	 * @return
	 */
	public OauthClientDetails findByClientIdAndClientSecret(String clientId, String clientSecret){
		return oauthClientDetailsDao.findByClientIdAndClientSecret(clientId, clientSecret);
	}
	
	public OauthClientDetails findByClientId(String clientId){
		return oauthClientDetailsDao.findByClientId(clientId);
	}
	/**
	 * 查询所有用户信息
	 * @param oauthClientDetails
	 * @param form
	 * @return
	 */
	public Page<OauthClientDetails> findAll(final OauthClientDetails oauthClientDetails, PageFormVo form){
		
		return oauthClientDetailsDao.findAll(new Specification<OauthClientDetails>() {
			@Override
			public Predicate toPredicate(Root<OauthClientDetails> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();

				if (oauthClientDetails != null) {
					// 按照姓名模糊查询
					if (!StringUtils.isEmpty(oauthClientDetails.getClientId())) {
						list.add(cb.like(root.<String> get("clientId"), '%' + oauthClientDetails.getClientId() + '%'));
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
