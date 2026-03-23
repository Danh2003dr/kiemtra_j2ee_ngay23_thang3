package com.example.lehoangdanh_2280605295_j2ee.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.lehoangdanh_2280605295_j2ee.entity.Category;
import com.example.lehoangdanh_2280605295_j2ee.entity.Course;
import com.example.lehoangdanh_2280605295_j2ee.entity.Role;
import com.example.lehoangdanh_2280605295_j2ee.entity.Student;
import com.example.lehoangdanh_2280605295_j2ee.repository.CategoryRepository;
import com.example.lehoangdanh_2280605295_j2ee.repository.CourseRepository;
import com.example.lehoangdanh_2280605295_j2ee.repository.RoleRepository;
import com.example.lehoangdanh_2280605295_j2ee.repository.StudentRepository;

@Configuration
public class DataInitializer {

	@Bean
	@Order(1)
	CommandLineRunner seedRoles(RoleRepository roleRepository) {
		return args -> {
			ensureRole(roleRepository, Role.ADMIN);
			ensureRole(roleRepository, Role.STUDENT);
		};
	}

	private static void ensureRole(RoleRepository roleRepository, String roleName) {
		if (roleRepository.findByName(roleName).isEmpty()) {
			roleRepository.save(Role.builder().name(roleName).build());
		}
	}

	@Bean
	@Order(2)
	CommandLineRunner seedAdminAccount(
			RoleRepository roleRepository,
			StudentRepository studentRepository,
			PasswordEncoder passwordEncoder,
			@Value("${app.bootstrap.admin.username:admin}") String adminUsername,
			@Value("${app.bootstrap.admin.password:admin}") String adminPassword,
			@Value("${app.bootstrap.admin.email:admin@example.com}") String adminEmail,
			@Value("${app.bootstrap.admin.include-student-role:false}") boolean includeStudentRole) {
		return args -> {
			Role adminRole = roleRepository.findByName(Role.ADMIN).orElseThrow();
			Role studentRole = roleRepository.findByName(Role.STUDENT).orElseThrow();

			studentRepository.findByUsername(adminUsername).ifPresentOrElse(
					student -> syncAdminRoles(student, studentRepository, adminRole, studentRole, includeStudentRole),
					() -> {
						Student admin = Student.builder()
								.username(adminUsername)
								.password(passwordEncoder.encode(adminPassword))
								.email(adminEmail)
								.build();
						admin.getRoles().add(adminRole);
						if (includeStudentRole) {
							admin.getRoles().add(studentRole);
						}
						studentRepository.save(admin);
					});
		};
	}

	private static void syncAdminRoles(
			Student student,
			StudentRepository studentRepository,
			Role adminRole,
			Role studentRole,
			boolean includeStudentRole) {
		boolean dirty = false;
		if (student.getRoles().stream().noneMatch(r -> Role.ADMIN.equals(r.getName()))) {
			student.getRoles().add(adminRole);
			dirty = true;
		}
		if (includeStudentRole) {
			if (student.getRoles().stream().noneMatch(r -> Role.STUDENT.equals(r.getName()))) {
				student.getRoles().add(studentRole);
				dirty = true;
			}
		} else if (student.getRoles().removeIf(r -> Role.STUDENT.equals(r.getName()))) {
			dirty = true;
		}
		if (dirty) {
			studentRepository.save(student);
		}
	}

	@Bean
	@Order(3)
	CommandLineRunner seedDemoCourses(CategoryRepository categoryRepository, CourseRepository courseRepository) {
		return args -> {
			if (categoryRepository.count() > 0) {
				return;
			}

			Category cntt = categoryRepository.save(Category.builder().name("Công nghệ thông tin").build());
			Category kinhTe = categoryRepository.save(Category.builder().name("Kinh tế").build());
			Category ngoaiNgu = categoryRepository.save(Category.builder().name("Ngoại ngữ").build());

			List<Course> samples = List.of(
					course("Lập trình Web với Spring Boot", "https://picsum.photos/seed/c1/640/360", 3, "TS. Nguyễn Văn A", cntt),
					course("Cơ sở dữ liệu", "https://picsum.photos/seed/c2/640/360", 4, "PGS.TS. Trần Thị B", cntt),
					course("Cấu trúc dữ liệu & Giải thuật", "https://picsum.photos/seed/c3/640/360", 4, "TS. Lê Văn C", cntt),
					course("Mạng máy tính", "https://picsum.photos/seed/c4/640/360", 3, "ThS. Phạm D", cntt),
					course("Hệ điều hành", "https://picsum.photos/seed/c5/640/360", 3, "TS. Hoàng E", cntt),
					course("Kinh tế vi mô", "https://picsum.photos/seed/c6/640/360", 3, "PGS.TS. Võ F", kinhTe),
					course("Kinh tế vĩ mô", "https://picsum.photos/seed/c7/640/360", 3, "TS. Đinh G", kinhTe),
					course("Tiếng Anh chuyên ngành", "https://picsum.photos/seed/c8/640/360", 2, "ThS. Jane H", ngoaiNgu),
					course("Phân tích thiết kế hệ thống", "https://picsum.photos/seed/c9/640/360", 3, "TS. Bùi I", cntt),
					course("Trí tuệ nhân tạo cơ bản", "https://picsum.photos/seed/c10/640/360", 3, "PGS.TS. Ngô K", cntt),
					course("Bảo mật thông tin", "https://picsum.photos/seed/c11/640/360", 3, "TS. Lý L", cntt),
					course("Đồ án tốt nghiệp", "https://picsum.photos/seed/c12/640/360", 10, "Hội đồng M", cntt));
			courseRepository.saveAll(samples);
		};
	}

	private static Course course(String name, String image, int credits, String lecturer, Category category) {
		return Course.builder()
				.name(name)
				.image(image)
				.credits(credits)
				.lecturer(lecturer)
				.category(category)
				.build();
	}
}
