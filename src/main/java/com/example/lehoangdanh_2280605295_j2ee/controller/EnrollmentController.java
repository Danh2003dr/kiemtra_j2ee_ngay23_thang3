package com.example.lehoangdanh_2280605295_j2ee.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.lehoangdanh_2280605295_j2ee.service.CurrentStudentService;
import com.example.lehoangdanh_2280605295_j2ee.service.EnrollmentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/enroll")
@PreAuthorize("hasRole('STUDENT') and !hasRole('ADMIN')")
@RequiredArgsConstructor
public class EnrollmentController {

	private final CurrentStudentService currentStudentService;
	private final EnrollmentService enrollmentService;

	@PostMapping("/{courseId}")
	public String enroll(
			@PathVariable Long courseId,
			Authentication authentication,
			RedirectAttributes redirectAttributes) {
		var student = currentStudentService.resolve(authentication)
				.orElseThrow(() -> new IllegalStateException("Chưa đăng nhập"));
		try {
			enrollmentService.enroll(student, courseId);
			redirectAttributes.addFlashAttribute("success", "Đăng ký học phần thành công.");
		}
		catch (IllegalArgumentException | IllegalStateException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/courses";
	}

	@GetMapping("/my-courses")
	public String myCourses(Authentication authentication, Model model) {
		model.addAttribute("keyword", "");
		var student = currentStudentService.resolve(authentication)
				.orElseThrow(() -> new IllegalStateException("Chưa đăng nhập"));
		model.addAttribute("enrollments", enrollmentService.listForStudent(student));
		return "enroll/my-courses";
	}
}
