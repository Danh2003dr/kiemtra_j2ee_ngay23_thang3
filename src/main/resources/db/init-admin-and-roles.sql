-- =============================================================================
-- TUỲ CHỌN: import thủ công khi không dùng auto-seed trong DataInitializer.
-- Mặc định ứng dụng đã tạo admin theo application.properties (app.bootstrap.admin.*).
-- =============================================================================
-- Đăng nhập mẫu: username = admin , mật khẩu = admin
-- (BCrypt sinh bởi Spring BCryptPasswordEncoder, strength mặc định 10)
-- =============================================================================

INSERT IGNORE INTO `role` (name) VALUES ('ADMIN');
INSERT IGNORE INTO `role` (name) VALUES ('STUDENT');

INSERT IGNORE INTO `student` (username, password, email)
VALUES (
	'admin',
	'$2a$10$H3KD1AaFe1w6SwFsfrjUG.0qAceorhth1GTfpnhyuNRx/QdD6Ha32',
	'admin@example.com'
);

-- Gán ADMIN + STUDENT cho user admin (chạy lại an toàn nhờ NOT EXISTS)
INSERT INTO `student_role` (student_id, role_id)
SELECT s.student_id, r.role_id
FROM `student` s
CROSS JOIN `role` r
WHERE s.username = 'admin'
  AND r.name IN ('ADMIN', 'STUDENT')
  AND NOT EXISTS (
	SELECT 1 FROM `student_role` x
	WHERE x.student_id = s.student_id AND x.role_id = r.role_id
  );
