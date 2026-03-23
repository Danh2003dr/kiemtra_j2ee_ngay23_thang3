package com.example.lehoangdanh_2280605295_j2ee.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.lehoangdanh_2280605295_j2ee.entity.Student;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StudentUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final Student student;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return student.getRoles().stream()
				.map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
				.collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		return student.getPassword();
	}

	@Override
	public String getUsername() {
		return student.getUsername();
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
}
