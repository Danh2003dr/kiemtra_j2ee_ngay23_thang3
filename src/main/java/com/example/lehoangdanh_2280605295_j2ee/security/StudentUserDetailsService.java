package com.example.lehoangdanh_2280605295_j2ee.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lehoangdanh_2280605295_j2ee.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentUserDetailsService implements UserDetailsService {

	private final StudentRepository studentRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return studentRepository.findByUsername(username)
				.map(StudentUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + username));
	}
}
