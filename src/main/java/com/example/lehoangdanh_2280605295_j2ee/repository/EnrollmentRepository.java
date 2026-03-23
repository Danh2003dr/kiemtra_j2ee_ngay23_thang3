package com.example.lehoangdanh_2280605295_j2ee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.lehoangdanh_2280605295_j2ee.entity.Course;
import com.example.lehoangdanh_2280605295_j2ee.entity.Enrollment;
import com.example.lehoangdanh_2280605295_j2ee.entity.Student;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

	List<Enrollment> findByStudentOrderByEnrollDateDesc(Student student);

	boolean existsByStudentAndCourse(Student student, Course course);

	void deleteByCourse_Id(Long courseId);

	@Query("select e.course.id from Enrollment e where e.student = :student")
	List<Long> findCourseIdsByStudent(@Param("student") Student student);
}
