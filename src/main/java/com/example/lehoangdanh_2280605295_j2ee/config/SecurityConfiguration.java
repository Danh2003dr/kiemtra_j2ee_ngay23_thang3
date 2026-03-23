package com.example.lehoangdanh_2280605295_j2ee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.lehoangdanh_2280605295_j2ee.security.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/css/**", "/images/**", "/error").permitAll()
						// Đăng nhập / đăng ký / OAuth2
						.requestMatchers("/register", "/login").permitAll()
						.requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
						// /admin/** — chỉ ADMIN
						.requestMatchers("/admin/**").hasRole("ADMIN")
						// /enroll/** — chỉ sinh viên thuần (có STUDENT và không có ADMIN)
						.requestMatchers("/enroll/**").access((authentication, context) -> enrollDecision(authentication.get()))
						// /courses (và /, /home) — mọi người (kể cả chưa đăng nhập)
						.requestMatchers("/", "/home", "/courses", "/courses/**").permitAll()
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/home", true)
						.permitAll())
				.oauth2Login(oauth2 -> oauth2
						.loginPage("/login")
						.defaultSuccessUrl("/home", true)
						.userInfoEndpoint(u -> u.userService(customOAuth2UserService)))
				.logout(logout -> logout
						.logoutSuccessUrl("/courses")
						.permitAll())
				.headers(h -> h.frameOptions(fo -> fo.sameOrigin()));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private static AuthorizationDecision enrollDecision(Authentication auth) {
		if (auth == null || !auth.isAuthenticated()) {
			return new AuthorizationDecision(false);
		}
		boolean admin = auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
		boolean student = auth.getAuthorities().stream().anyMatch(a -> "ROLE_STUDENT".equals(a.getAuthority()));
		return new AuthorizationDecision(student && !admin);
	}
}
