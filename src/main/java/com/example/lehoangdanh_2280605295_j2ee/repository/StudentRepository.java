package com.example.lehoangdanh_2280605295_j2ee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lehoangdanh_2280605295_j2ee.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	Optional<Student> findByUsername(String username);

	Optional<Student> findByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
}
