package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.security.dao.OauthCodeDao;
import com.security.domain.OauthCode;
import com.security.util.Generate;

@Transactional
@Service
public class OauthCodeService {
	
	@Autowired
	private OauthCodeDao oauthCodeDao;
	
	public OauthCode save(OauthCode oauthCode){
		return oauthCodeDao.save(oauthCode);
	}
	
	public OauthCode findById(Long id){
		return oauthCodeDao.findById(id).get();
	}
	
	/**
	 * 生成授权码
	 * @return
	 */
	public OauthCode generateCode(){
		OauthCode oauthCode = new OauthCode();
		oauthCode.setCode(Generate.generateUUID());
		oauthCode.setAuthentication("");
		return oauthCodeDao.save(oauthCode);
	}
}
