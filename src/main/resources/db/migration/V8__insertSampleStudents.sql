-- Insert sample students for testing enrollment API
INSERT INTO User (name, email, role, password, createdAt) VALUES
('João Silva', 'joao.silva@student.com', 'STUDENT', MD5('123456'), NOW()),
('Maria Santos', 'maria.santos@student.com', 'STUDENT', MD5('123456'), NOW()),
('Pedro Oliveira', 'pedro.oliveira@student.com', 'STUDENT', MD5('123456'), NOW()),
('Ana Costa', 'ana.costa@student.com', 'STUDENT', MD5('123456'), NOW());

-- Update existing user to be a student if exists
UPDATE User SET role = 'STUDENT' WHERE email = 'teste@email.com' AND role != 'INSTRUCTOR';
