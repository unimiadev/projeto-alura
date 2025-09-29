-- Insert sample users for testing
-- Password for all users is "123456" (MD5: e10adc3949ba59abbe56e057f20f883e)

INSERT INTO User (name, email, password, role, createdAt) VALUES
('João Silva', 'joao.silva@alura.com.br', 'e10adc3949ba59abbe56e057f20f883e', 'INSTRUCTOR', NOW()),
('Maria Santos', 'maria.santos@alura.com.br', 'e10adc3949ba59abbe56e057f20f883e', 'INSTRUCTOR', NOW()),
('Pedro Oliveira', 'pedro.oliveira@alura.com.br', 'e10adc3949ba59abbe56e057f20f883e', 'INSTRUCTOR', NOW()),
('Ana Costa', 'ana.costa@alura.com.br', 'e10adc3949ba59abbe56e057f20f883e', 'INSTRUCTOR', NOW()),
('Carlos Ferreira', 'carlos.ferreira@alura.com.br', 'e10adc3949ba59abbe56e057f20f883e', 'INSTRUCTOR', NOW()),
('Estudante Teste', 'estudante@alura.com.br', 'e10adc3949ba59abbe56e057f20f883e', 'STUDENT', NOW());
