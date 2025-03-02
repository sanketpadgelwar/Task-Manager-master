CREATE DATABASE tmanager;
USE tmanager;
SHOW TABLES;
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'PROJECT_MANAGER', 'TASK_ASSIGNEE') NOT NULL
);

CREATE TABLE projects (
    project_id INT PRIMARY KEY AUTO_INCREMENT,
    project_name VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATETIME,
    end_date DATETIME,
    manager_id INT,
    FOREIGN KEY (manager_id) REFERENCES users(user_id),
	CHECK (end_date >= start_date)
);
CREATE TABLE tasks (
    task_id INT PRIMARY KEY AUTO_INCREMENT,
    task_name VARCHAR(100) NOT NULL,
    description TEXT,
    status ENUM('BACKLOG', 'ANALYSIS_WIP', 'DEVELOPMENT_WIP','TESTING_WIP','PRODUCTION_ACCEPTANCE','DEFFECT_RAISED') NOT NULL,
    priority ENUM('LOW', 'MEDIUM', 'HIGH') NOT NULL,
    deadline DATETIME,
    project_id INT,
    assigned_user_id INT,
    FOREIGN KEY (project_id) REFERENCES projects(project_id),
    FOREIGN KEY (assigned_user_id) REFERENCES users(user_id)
);

DELIMITER $$
CREATE TRIGGER trg_before_insert_tasks
BEFORE INSERT ON tasks
FOR EACH ROW
BEGIN
    IF NEW.deadline < NOW() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Deadline must be in the future';
    END IF;
END$$
DELIMITER ;

CREATE TABLE roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);


CREATE INDEX idx_project_id ON tasks (project_id);
CREATE INDEX idx_assigned_user_id ON tasks (assigned_user_id);

ALTER TABLE users
MODIFY COLUMN role VARCHAR(20);

INSERT INTO users (username, email, password, role)
VALUES
('jane_smith', 'jane@example.com', 'password_123', 'ADMIN'),
('bob_johnson', 'bob@example.com', 'password_123', 'TASK_ASSIGNEE');


SELECT * FROM tasks;
SELECT * FROM projects;
SELECT * FROM users;

DESCRIBE users;
ALTER TABLE tasks MODIFY status ENUM('BACKLOG', 'ANALYSIS_WIP', 'DEVELOPMENT_WIP', 'TESTING_WIP', 'PRODUCTION_ACCEPTANCE','DEFFECT_RAISED');

INSERT INTO users (user_id, username, email, password, role) VALUES
(1, 'john.doe', 'john.doe@example.com', 'password123', 'ADMIN'),
(2, 'jane.smith', 'jane.smith@example.com', 'password123', 'PROJECT_MANAGER'),
(3, 'alice.johnson', 'alice.johnson@example.com', 'password123', 'TASK_ASSIGNEE'),
(4, 'bob.williams', 'bob.williams@example.com', 'password123', 'PROJECT_MANAGER'),
(5, 'carol.taylor', 'carol.taylor@example.com', 'password123', 'TASK_ASSIGNEE'),
(6, 'david.brown', 'david.brown@example.com', 'password123', 'ADMIN'),
(7, 'eve.davis', 'eve.davis@example.com', 'password123', 'TASK_ASSIGNEE'),
(8, 'frank.miller', 'frank.miller@example.com', 'password123', 'PROJECT_MANAGER'),
(9, 'grace.wilson', 'grace.wilson@example.com', 'password123', 'ADMIN'),
(10, 'henry.moore', 'henry.moore@example.com', 'password123', 'TASK_ASSIGNEE');


DESCRIBE projects;
-- Insert dummy data into projects table
INSERT INTO projects (project_id, manager_id, project_name, description, start_date, end_date) VALUES
(1, 4, 'Project Alpha', 'Description for Project Alpha', '2024-01-01', '2024-06-30'),
(2, 2, 'Project Beta', 'Description for Project Beta', '2024-02-01', '2024-07-31'),
(3, 8, 'Project Gamma', 'Description for Project Gamma', '2024-03-01', '2024-08-31'),
(4, 4, 'Project Delta', 'Description for Project Delta', '2024-04-01', '2024-09-30'),
(5, 2, 'Project Epsilon', 'Description for Project Epsilon', '2024-05-01', '2024-10-31');
DELETE FROM projects WHERE project_id IN (6,7,8,9,10);
INSERT INTO projects (project_id, manager_id, project_name, description, start_date, end_date) VALUES
(6, 3, 'Cloud Migration', 'Migration of on-premises systems to the cloud', '2024-06-01', '2024-12-31'),
(7, 5, 'Data Analytics Platform', 'Development of a new data analytics platform', '2024-07-01', '2025-01-31'),
(8, 6, 'Cybersecurity Enhancement', 'Improving cybersecurity measures across the organization', '2024-08-01', '2025-02-28'),
(9, 7, 'AI-Powered Chatbot', 'Creation of an AI-powered customer service chatbot', '2024-09-01', '2025-03-31'),
(10, 8, 'Virtual Collaboration Tool', 'Development of a new tool for virtual team collaboration', '2024-10-01', '2025-04-30');



UPDATE projects SET project_name = 'Website Redesign' WHERE project_id = 1;
UPDATE projects SET project_name = 'Mobile App Development' WHERE project_id = 2;
UPDATE projects SET project_name = 'CRM System Overhaul' WHERE project_id = 3;
UPDATE projects SET project_name = 'Enterprise Resource Planning' WHERE project_id = 4;
UPDATE projects SET project_name = 'Customer Feedback Platform' WHERE project_id = 5;





DESCRIBE tasks;
INSERT INTO tasks (task_id, task_name, description, status, priority, project_id, assigned_user_id, deadline) VALUES
(41, 'New UI Mockups', 'Create new UI mockups based on feedback', 'BACKLOG', 'MEDIUM', 1, 5, '2024-03-05'),
(42, 'Database Indexing', 'Optimize database queries with indexing', 'ANALYSIS_WIP', 'HIGH', 1, 3, '2024-03-15'),
(43, 'Middleware Development', 'Develop middleware for communication', 'DEVELOPMENT_WIP', 'LOW', 2, 7, '2024-04-05'),
(44, 'Functional Testing', 'Test all functionalities of the system', 'TESTING_WIP', 'HIGH', 2, 5, '2024-04-15'),
(45, 'Load Testing', 'Perform load testing to ensure stability', 'PRODUCTION_ACCEPTANCE', 'MEDIUM', 3, 10, '2024-05-05'),
(46, 'Critical Bug Fix', 'Fix critical bugs found during testing', 'DEFFECT_RAISED', 'HIGH', 3, 5, '2024-02-20'),
(47, 'Initial Requirements Gathering', 'Gather initial requirements from stakeholders', 'BACKLOG', 'LOW', 4, 5, '2024-01-20'),
(48, 'Refactor Codebase', 'Refactor code for better performance', 'ANALYSIS_WIP', 'MEDIUM', 4, 3, '2024-02-25'),
(49, 'Build API Endpoints', 'Develop API endpoints for mobile app', 'DEVELOPMENT_WIP', 'HIGH', 5, 7, '2024-03-10'),
(50, 'End-to-End Testing', 'Perform end-to-end testing', 'TESTING_WIP', 'LOW', 5, 6, '2024-04-05'),
(51, 'Feedback Analysis', 'Analyze feedback from beta users', 'BACKLOG', 'MEDIUM', 1, 5, '2024-03-25'),
(52, 'Data Migration', 'Migrate data to new database structure', 'ANALYSIS_WIP', 'HIGH', 1, 3, '2024-04-05'),
(53, 'Service Layer Implementation', 'Implement the service layer', 'DEVELOPMENT_WIP', 'MEDIUM', 2, 7, '2024-04-20'),
(54, 'System Validation', 'Validate system against requirements', 'TESTING_WIP', 'HIGH', 2, 5, '2024-04-25'),
(55, 'Release Management', 'Manage the release process', 'PRODUCTION_ACCEPTANCE', 'LOW', 3, 10, '2024-05-15'),
(56, 'Security Patch', 'Apply security patches to the system', 'DEFFECT_RAISED', 'MEDIUM', 3, 5, '2024-02-28'),
(57, 'Client Demo Preparation', 'Prepare for client demonstration', 'BACKLOG', 'HIGH', 4, 5, '2024-03-15'),
(58, 'Architecture Review', 'Review system architecture', 'ANALYSIS_WIP', 'LOW', 4, 3, '2024-03-30'),
(59, 'Module Integration', 'Integrate modules developed by different teams', 'DEVELOPMENT_WIP', 'HIGH', 5, 7, '2024-04-30'),
(60, 'Beta Testing', 'Conduct beta testing with select users', 'TESTING_WIP', 'MEDIUM', 5, 6, '2024-05-15'),
(61, 'User Experience Survey', 'Conduct surveys to understand user experience', 'BACKLOG', 'LOW', 1, 5, '2024-03-20'),
(62, 'Performance Tuning', 'Tune performance of key components', 'ANALYSIS_WIP', 'HIGH', 1, 3, '2024-04-10'),
(63, 'Feature Implementation', 'Implement new features as per requirements', 'DEVELOPMENT_WIP', 'MEDIUM', 2, 7, '2024-04-22'),
(64, 'Regression Testing', 'Perform regression testing', 'TESTING_WIP', 'HIGH', 2, 5, '2024-05-01'),
(65, 'Deployment Plan', 'Create a detailed deployment plan', 'PRODUCTION_ACCEPTANCE', 'LOW', 3, 10, '2024-05-20'),
(66, 'Critical Defect Fixes', 'Address critical defects reported', 'DEFFECT_RAISED', 'HIGH', 3, 5, '2024-03-10'),
(67, 'Sprint Planning', 'Plan tasks for upcoming sprint', 'BACKLOG', 'MEDIUM', 4, 5, '2024-03-25'),
(68, 'Technical Debt Reduction', 'Reduce technical debt in the codebase', 'ANALYSIS_WIP', 'LOW', 4, 3, '2024-04-05'),
(69, 'System Upgrade', 'Upgrade system to latest version', 'DEVELOPMENT_WIP', 'HIGH', 5, 7, '2024-05-10'),
(70, 'Compatibility Testing', 'Test compatibility with other systems', 'TESTING_WIP', 'MEDIUM', 5, 6, '2024-05-20'),
(71, 'Product Launch Preparation', 'Prepare for product launch', 'BACKLOG', 'HIGH', 1, 5, '2024-04-15'),
(72, 'Data Analysis', 'Analyze data for insights', 'ANALYSIS_WIP', 'MEDIUM', 1, 3, '2024-04-25'),
(73, 'Interface Development', 'Develop user interface components', 'DEVELOPMENT_WIP', 'LOW', 2, 7, '2024-05-05'),
(74, 'Acceptance Testing', 'Perform acceptance testing with stakeholders', 'TESTING_WIP', 'HIGH', 2, 5, '2024-05-15'),
(75, 'Production Rollout', 'Rollout new version to production', 'PRODUCTION_ACCEPTANCE', 'LOW', 3, 10, '2024-06-01'),
(76, 'Emergency Bug Fix', 'Fix emergency bugs found in production', 'DEFFECT_RAISED', 'MEDIUM', 3, 5, '2024-03-15'),
(77, 'Backlog Refinement', 'Refine the product backlog', 'BACKLOG', 'HIGH', 4, 5, '2024-04-05'),
(78, 'Code Cleanup', 'Clean up code and remove redundancies', 'ANALYSIS_WIP', 'LOW', 4, 3, '2024-04-15'),
(79, 'Module Testing', 'Test individual modules', 'DEVELOPMENT_WIP', 'HIGH', 5, 7, '2024-05-25'),
(80, 'User Feedback Review', 'Review feedback from end users', 'TESTING_WIP', 'MEDIUM', 5, 6, '2024-06-05');


use tmanager;
ALTER TABLE projects ADD COLUMN last_updated_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tasks ADD COLUMN last_updated_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

	


