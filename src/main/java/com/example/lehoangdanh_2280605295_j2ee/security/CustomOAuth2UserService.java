package com.example.lehoangdanh_2280605295_j2ee.security;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lehoangdanh_2280605295_j2ee.entity.Role;
import com.example.lehoangdanh_2280605295_j2ee.entity.Student;
import com.example.lehoangdanh_2280605295_j2ee.repository.RoleRepository;
import com.example.lehoangdanh_2280605295_j2ee.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final StudentRepository studentRepository;
	private final RoleRepository roleRepository;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(userRequest);
		Map<String, Object> attributes = oauth2User.getAttributes();
		String email = (String) attributes.get("email");
		if (email == null || email.isBlank()) {
			throw new OAuth2AuthenticationException(
					new OAuth2Error("invalid_request", "Email không có trong tài khoản Google", null));
		}

		Student student = studentRepository.findByEmail(email)
				.orElseGet(() -> registerGoogleUser(email, attributes));

		Set<SimpleGrantedAuthority> authorities = student.getRoles().stream()
				.map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
				.collect(Collectors.toCollection(HashSet::new));

		String nameAttributeKey = userRequest.getClientRegistration()
				.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		if (nameAttributeKey == null || nameAttributeKey.isBlank()) {
			nameAttributeKey = "sub";
		}

		return new DefaultOAuth2User(authorities, attributes, nameAttributeKey);
	}

	private Student registerGoogleUser(String email, Map<String, Object> attributes) {
		Role studentRole = roleRepository.findByName(Role.STUDENT)
				.orElseThrow(() -> new IllegalStateException("Chưa khởi tạo role STUDENT"));

		Object sub = attributes.get("sub");
		String username = email;
		if (studentRepository.existsByUsername(username)) {
			username = "google_" + (sub != null ? sub.toString() : String.valueOf(email.hashCode()));
		}

		Student student = Student.builder()
				.username(username)
				.password(null)
				.email(email)
				.build();
		student.getRoles().add(studentRole);
		return studentRepository.save(student);
	}
}
