package com.example.lehoangdanh_2280605295_j2ee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.lehoangdanh_2280605295_j2ee.dto.RegistrationForm;
import com.example.lehoangdanh_2280605295_j2ee.service.StudentRegistrationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

	private final StudentRegistrationService studentRegistrationService;

	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("keyword", "");
		return "auth/login";
	}

	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("keyword", "");
		model.addAttribute("registrationForm", new RegistrationForm());
		return "auth/register";
	}

	@PostMapping("/register")
	public String register(
			@Valid @ModelAttribute("registrationForm") RegistrationForm form,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("keyword", "");
			return "auth/register";
		}
		try {
			studentRegistrationService.register(form.getUsername(), form.getPassword(), form.getEmail());
			redirectAttributes.addFlashAttribute("success", "Đăng ký thành công. Vui lòng đăng nhập.");
			return "redirect:/login";
		}
		catch (IllegalArgumentException ex) {
			model.addAttribute("keyword", "");
			bindingResult.reject("register.error", ex.getMessage());
			return "auth/register";
		}
	}
}
