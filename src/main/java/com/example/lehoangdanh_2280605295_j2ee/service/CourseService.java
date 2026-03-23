package com.example.lehoangdanh_2280605295_j2ee.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lehoangdanh_2280605295_j2ee.entity.Course;
import com.example.lehoangdanh_2280605295_j2ee.repository.CourseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

	public static final int PAGE_SIZE = 5;

	private final CourseRepository courseRepository;

	@Transactional(readOnly = true)
	public Page<Course> listCourses(int page, String keyword) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		if (keyword != null && !keyword.isBlank()) {
			return courseRepository.findByNameContainingIgnoreCase(keyword.strip(), pageable);
		}
		return courseRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Page<Course> listAllForAdmin(int page) {
		return courseRepository.findAll(PageRequest.of(page, 20));
	}

	@Transactional(readOnly = true)
	public Course getById(Long id) {
		return courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không có học phần"));
	}
}
