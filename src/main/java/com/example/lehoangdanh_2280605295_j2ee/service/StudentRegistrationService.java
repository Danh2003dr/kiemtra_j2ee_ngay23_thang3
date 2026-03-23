package com.example.lehoangdanh_2280605295_j2ee.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lehoangdanh_2280605295_j2ee.entity.Role;
import com.example.lehoangdanh_2280605295_j2ee.entity.Student;
import com.example.lehoangdanh_2280605295_j2ee.repository.RoleRepository;
import com.example.lehoangdanh_2280605295_j2ee.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentRegistrationService {

	private final StudentRepository studentRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void register(String username, String rawPassword, String email) {
		if (studentRepository.existsByUsername(username)) {
			throw new IllegalArgumentException("Tên đăng nhập đã được sử dụng");
		}
		if (studentRepository.existsByEmail(email)) {
			throw new IllegalArgumentException("Email đã được sử dụng");
		}
		Role studentRole = roleRepository.findByName(Role.STUDENT)
				.orElseThrow(() -> new IllegalStateException("Chưa khởi tạo role STUDENT"));

		Student student = Student.builder()
				.username(username)
				.password(passwordEncoder.encode(rawPassword))
				.email(email)
				.build();
		student.getRoles().add(studentRole);
		studentRepository.save(student);
	}
}
