package com.example.lehoangdanh_2280605295_j2ee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationForm {

	@NotBlank(message = "Nhập tên đăng nhập")
	@Size(min = 3, max = 100, message = "Tên đăng nhập từ 3 đến 100 ký tự")
	private String username;

	@NotBlank(message = "Nhập mật khẩu")
	@Size(min = 6, max = 100, message = "Mật khẩu ít nhất 6 ký tự")
	private String password;

	@NotBlank(message = "Nhập email")
	@Email(message = "Email không hợp lệ")
	private String email;
}
