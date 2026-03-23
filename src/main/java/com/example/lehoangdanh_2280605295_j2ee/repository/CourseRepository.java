package com.example.lehoangdanh_2280605295_j2ee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lehoangdanh_2280605295_j2ee.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

	Page<Course> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
