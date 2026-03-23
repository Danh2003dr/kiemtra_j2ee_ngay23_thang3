package com.example.lehoangdanh_2280605295_j2ee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.lehoangdanh_2280605295_j2ee.entity.Course;
import com.example.lehoangdanh_2280605295_j2ee.repository.CategoryRepository;
import com.example.lehoangdanh_2280605295_j2ee.service.CourseAdminService;
import com.example.lehoangdanh_2280605295_j2ee.service.CourseService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

	private final CourseService courseService;
	private final CourseAdminService courseAdminService;
	private final CategoryRepository categoryRepository;

	@GetMapping
	public String list(@RequestParam(defaultValue = "0") int page, Model model) {
		model.addAttribute("keyword", "");
		model.addAttribute("page", courseService.listAllForAdmin(page));
		return "admin/courses/list";
	}

	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("keyword", "");
		model.addAttribute("course", Course.builder()
				.name("")
				.image("")
				.credits(3)
				.lecturer("")
				.build());
		model.addAttribute("categories", categoryRepository.findAll());
		return "admin/courses/form";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {
		model.addAttribute("keyword", "");
		model.addAttribute("course", courseService.getById(id));
		model.addAttribute("categories", categoryRepository.findAll());
		return "admin/courses/form";
	}

	@PostMapping
	public String save(
			@RequestParam(required = false) Long id,
			@RequestParam String name,
			@RequestParam(required = false) String image,
			@RequestParam Integer credits,
			@RequestParam String lecturer,
			@RequestParam Long categoryId,
			RedirectAttributes redirectAttributes) {
		if (id == null) {
			courseAdminService.create(name, image, credits, lecturer, categoryId);
			redirectAttributes.addFlashAttribute("success", "Đã thêm học phần.");
		}
		else {
			courseAdminService.update(id, name, image, credits, lecturer, categoryId);
			redirectAttributes.addFlashAttribute("success", "Đã cập nhật học phần.");
		}
		return "redirect:/admin/courses";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		courseAdminService.delete(id);
		redirectAttributes.addFlashAttribute("success", "Đã xóa học phần.");
		return "redirect:/admin/courses";
	}
}
