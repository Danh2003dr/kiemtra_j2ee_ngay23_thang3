package com.example.lehoangdanh_2280605295_j2ee.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lehoangdanh_2280605295_j2ee.entity.Course;
import com.example.lehoangdanh_2280605295_j2ee.entity.Enrollment;
import com.example.lehoangdanh_2280605295_j2ee.entity.Student;
import com.example.lehoangdanh_2280605295_j2ee.repository.CourseRepository;
import com.example.lehoangdanh_2280605295_j2ee.repository.EnrollmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

	private final EnrollmentRepository enrollmentRepository;
	private final CourseRepository courseRepository;

	@Transactional
	public void enroll(Student student, Long courseId) {
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException("Không có học phần"));
		if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
			throw new IllegalStateException("Bạn đã đăng ký học phần này");
		}
		Enrollment enrollment = Enrollment.builder()
				.student(student)
				.course(course)
				.enrollDate(LocalDate.now())
				.build();
		enrollmentRepository.save(enrollment);
	}

	@Transactional(readOnly = true)
	public List<Enrollment> listForStudent(Student student) {
		return enrollmentRepository.findByStudentOrderByEnrollDateDesc(student);
	}

	@Transactional(readOnly = true)
	public Set<Long> enrolledCourseIds(Student student) {
		return new HashSet<>(enrollmentRepository.findCourseIdsByStudent(student));
	}
}
