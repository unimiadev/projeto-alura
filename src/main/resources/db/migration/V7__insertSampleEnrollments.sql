-- Insert sample enrollments for testing
-- Note: This assumes we have students and courses from previous migrations

-- Get student and course IDs for sample enrollments
INSERT INTO Enrollment (student_id, course_id, status, enrolled_at) 
SELECT 
    (SELECT id FROM User WHERE email = 'teste@email.com' AND role = 'STUDENT' LIMIT 1) as student_id,
    (SELECT id FROM Course WHERE code = 'spring-boot' LIMIT 1) as course_id,
    'ACTIVE' as status,
    NOW() - INTERVAL 7 DAY as enrolled_at
WHERE EXISTS (SELECT 1 FROM User WHERE email = 'teste@email.com' AND role = 'STUDENT')
  AND EXISTS (SELECT 1 FROM Course WHERE code = 'spring-boot');

-- Add more sample enrollments if we have more students
INSERT INTO Enrollment (student_id, course_id, status, enrolled_at, completed_at) 
SELECT 
    (SELECT id FROM User WHERE email = 'joao.silva@alura.com.br' AND role = 'STUDENT' LIMIT 1) as student_id,
    (SELECT id FROM Course WHERE code = 'java-oo' LIMIT 1) as course_id,
    'COMPLETED' as status,
    NOW() - INTERVAL 30 DAY as enrolled_at,
    NOW() - INTERVAL 5 DAY as completed_at
WHERE EXISTS (SELECT 1 FROM User WHERE email = 'joao.silva@alura.com.br' AND role = 'STUDENT')
  AND EXISTS (SELECT 1 FROM Course WHERE code = 'java-oo');

-- Add a cancelled enrollment example
INSERT INTO Enrollment (student_id, course_id, status, enrolled_at) 
SELECT 
    (SELECT id FROM User WHERE email = 'maria.santos@alura.com.br' AND role = 'STUDENT' LIMIT 1) as student_id,
    (SELECT id FROM Course WHERE code = 'react-hooks' LIMIT 1) as course_id,
    'CANCELLED' as status,
    NOW() - INTERVAL 15 DAY as enrolled_at
WHERE EXISTS (SELECT 1 FROM User WHERE email = 'maria.santos@alura.com.br' AND role = 'STUDENT')
  AND EXISTS (SELECT 1 FROM Course WHERE code = 'react-hooks');
