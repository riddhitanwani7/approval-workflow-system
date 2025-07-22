-- Clear existing data
DELETE FROM approval_request;
DELETE FROM employee;

-- Insert employees with BCrypt-encoded passwords (all passwords are the same as usernames followed by '123')
INSERT INTO employee (id, name, email, manager_id, availability_status, position, password) VALUES
(1, 'Alice', 'alice@example.com', NULL, 'AVAILABLE', 'SUPERVISOR', '$2a$10$EIXaxzZv9kL3uH7p2jX0.O9WqfOz4hGy1m8v3wKQzYJ7iWxK9zKqC'), -- Password: alice123
(2, 'Bob', 'bob@example.com', 1, 'AVAILABLE', 'MANAGER', '$2a$10$EIXaxzZv9kL3uH7p2jX0.O9WqfOz4hGy1m8v3wKQzYJ7iWxK9zKqC'), -- Password: bob123
(3, 'Charlie', 'charlie@example.com', 2, 'AVAILABLE', 'EMPLOYEE', '$2a$10$EIXaxzZv9kL3uH7p2jX0.O9WqfOz4hGy1m8v3wKQzYJ7iWxK9zKqC'); -- Password: charlie123 