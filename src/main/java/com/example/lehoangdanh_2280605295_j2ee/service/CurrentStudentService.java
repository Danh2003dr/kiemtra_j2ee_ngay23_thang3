package com.example.lehoangdanh_2280605295_j2ee.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.lehoangdanh_2280605295_j2ee.entity.Student;
import com.example.lehoangdanh_2280605295_j2ee.repository.StudentRepository;
import com.example.lehoangdanh_2280605295_j2ee.security.StudentUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrentStudentService {

	private final StudentRepository studentRepository;

	public Optional<Student> resolve(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof StudentUserDetails sud) {
			return Optional.of(sud.getStudent());
		}
		if (principal instanceof OAuth2User ou) {
			String email = ou.getAttribute("email");
			if (email != null) {
				return studentRepository.findByEmail(email);
			}
		}
		return Optional.empty();
	}
}
