package com.example.lehoangdanh_2280605295_j2ee.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lehoangdanh_2280605295_j2ee.service.CourseService;
import com.example.lehoangdanh_2280605295_j2ee.service.CurrentStudentService;
import com.example.lehoangdanh_2280605295_j2ee.service.EnrollmentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CoursePublicController {

	private final CourseService courseService;
	private final CurrentStudentService currentStudentService;
	private final EnrollmentService enrollmentService;

	@GetMapping("/")
	public String root() {
		return "redirect:/courses";
	}

	@GetMapping({ "/courses", "/home" })
	public String listCourses(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String q,
			Authentication authentication,
			Model model) {
		model.addAttribute("page", courseService.listCourses(page, q));
		model.addAttribute("keyword", q != null ? q : "");
		currentStudentService.resolve(authentication).ifPresentOrElse(
				student -> model.addAttribute("enrolledIds", enrollmentService.enrolledCourseIds(student)),
				() -> model.addAttribute("enrolledIds", java.util.Set.of()));
		return "courses/list";
	}
}
