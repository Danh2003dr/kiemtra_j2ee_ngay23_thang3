package com.example.lehoangdanh_2280605295_j2ee.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lehoangdanh_2280605295_j2ee.entity.Category;
import com.example.lehoangdanh_2280605295_j2ee.entity.Course;
import com.example.lehoangdanh_2280605295_j2ee.repository.CategoryRepository;
import com.example.lehoangdanh_2280605295_j2ee.repository.CourseRepository;
import com.example.lehoangdanh_2280605295_j2ee.repository.EnrollmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseAdminService {

	private final CourseRepository courseRepository;
	private final CategoryRepository categoryRepository;
	private final EnrollmentRepository enrollmentRepository;

	@Transactional
	public Course create(String name, String image, Integer credits, String lecturer, Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("Danh mục không hợp lệ"));
		Course course = Course.builder()
				.name(name)
				.image(image)
				.credits(credits)
				.lecturer(lecturer)
				.category(category)
				.build();
		return courseRepository.save(course);
	}

	@Transactional
	public Course update(Long id, String name, String image, Integer credits, String lecturer, Long categoryId) {
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Không có học phần"));
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("Danh mục không hợp lệ"));
		course.setName(name);
		course.setImage(image);
		course.setCredits(credits);
		course.setLecturer(lecturer);
		course.setCategory(category);
		return courseRepository.save(course);
	}

	@Transactional
	public void delete(Long id) {
		enrollmentRepository.deleteByCourse_Id(id);
		courseRepository.deleteById(id);
	}
}
