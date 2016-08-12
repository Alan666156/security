package com.security.userdetails;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.security.cas.AuthoritiesConstants;


/**
 * 使用security 存储用户信息
 * @author fuhongxing
 * @date 2016年8月2日
 * @version 1.0.0
 */
//@Transactional
//@Service
public class CustomUserDetailsService implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);
	private Set<String> admins = new HashSet<>();
	
	
	public CustomUserDetailsService() {
		super();
	}

	public CustomUserDetailsService(Set<String> admins) {
		super();
		this.admins = admins;
	}

	@Override
	public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
		LOGGER.info("spring security cas authentication push userDetail");
		String login = token.getPrincipal().toString();
		String lowercaseLogin = login.toLowerCase();
		LOGGER.info("Authenticating '{}'", lowercaseLogin);
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		if (admins != null && admins.contains(lowercaseLogin)) {
			grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN));
		} else {
			grantedAuthorities.add(new GrantedAuthority() {
				private static final long serialVersionUID = 1L;
				@Override
				public String getAuthority() {
					return AuthoritiesConstants.USER;
				}
			});
		}
		//写入当前用户信息到security
		UserDetailsImpl userDetail = new UserDetailsImpl(lowercaseLogin, grantedAuthorities);
//		userDetail.setName(token.getName().split("\\\\")[1]);
		return userDetail;
	}

	
}
