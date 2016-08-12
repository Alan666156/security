package com.security.userdetails;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UserDetailsImpl extends User implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7442692309456432678L;
	
	private Map<String ,Object> transitData = new HashMap<String, Object>();
	private Collection<? extends GrantedAuthority> authorities;

	private List<String> roles;

	private String userid;
	
	public String getUserid() {
		return userid;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public UserDetailsImpl() {
		
	}
	
	public UserDetailsImpl(String userid, Collection<? extends GrantedAuthority> authorities) {
		super();
		this.userid = userid;
		this.authorities = authorities;
		this.roles = new ArrayList<String>();
		for (GrantedAuthority authority : authorities) {
			this.roles.add(authority.getAuthority());
		}
	}
	public UserDetailsImpl(Long id) {
		super.setId(id);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getUsername() {
		return userid;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Map<String, Object> getTransitData() {
		return transitData;
	}

	public void setTransitData(Map<String, Object> transitData) {
		this.transitData = transitData;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toString() {
		return "UserDetails [userid=" + userid + ", authorities=" + roles.toString() + ", isAccountNonExpired()="
				+ isAccountNonExpired() + ", isAccountNonLocked()=" + isAccountNonLocked()
				+ ", isCredentialsNonExpired()=" + isCredentialsNonExpired() + ", isEnabled()=" + isEnabled() + "]";
	}
 }