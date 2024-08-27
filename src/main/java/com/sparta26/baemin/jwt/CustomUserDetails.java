package com.sparta26.baemin.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
public class CustomUserDetails implements UserDetails{

	private static final long serialVersionUID = 1L;
	private final Long id;
	private ForContext context;
	public CustomUserDetails(ForContext forContext) {
		this.context = forContext;
		this.id = context.getId();
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Set<GrantedAuthority> authorities = new HashSet<>();

		authorities.add(new SimpleGrantedAuthority(context.getRole()));

		return authorities;

	}
	public ForContext getForContext() {
		return context;
	}
	public Long getId() {
		return id;
	}
	@Override
	public String getPassword() {
		return "";
	}
	@Override
	public String getUsername() {
		return context.getEmail();
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
	public String getEmail() {
		return context.getEmail();
	}
	public  String getRole() { return context.getRole();}
}
