-- Insert sample Admin
INSERT INTO users (username, password, email, first_name, last_name, user_role) VALUES
('admin1', 'password123', 'admin1@example.com', 'Admin', 'One', 'ADMIN');

-- Insert sample Instructor
INSERT INTO users (username, password, email, first_name, last_name, user_role, admin_id) VALUES
('instructor1', 'password456', 'instructor1@example.com', 'Instructor', 'One', 'INSTRUCTOR', 1);

-- Insert sample Students
INSERT INTO users (username, password, email, first_name, last_name, user_role, admin_id) VALUES
('student1', 'password789', 'student1@example.com', 'Student', 'One', 'STUDENT', 1),
('student2', 'password101', 'student2@example.com', 'Student', 'Two', 'STUDENT', 1);

-- Insert sample Courses
INSERT INTO course (title, description, instructor_id) VALUES
('Course 1', 'Description of Course 1', 2),
('Course 2', 'Description of Course 2', 2);

-- Assign students to courses in student_courses table (Many-to-Many)
INSERT INTO student_courses (student_id, course_id) VALUES
(3, 1), -- Automatically uses the correct student ID once generated
(3, 2),
(4, 1);

-- Insert notifications
INSERT INTO notification (message, created_at, is_read) VALUES
('New class added to Course 1', '2023-11-01 10:00:00', false),
('Assignment deadline update for Course 2', '2023-11-02 12:00:00', true);

-- Assign notifications to students (Many-to-Many)
INSERT INTO student_notifications (student_id, notification_id) VALUES
(3, 1), -- Automatically uses the correct student and notification IDs
(4, 1),
(4, 2);

-- Insert Lessons
INSERT INTO lesson (title, otp_code, course_id) VALUES
('Lesson 1', 'ABC123', 1),
('Lesson 2', 'DEF456', 1),
('Lesson 1', 'GHI789', 2);

-- Insert sample Assessments (Inheritance: SINGLE_TABLE)
INSERT INTO assessment (title,total_questions, passing_score,grade, course_id, assessment_type) VALUES
('Quiz 1',10,50, 85, 1, 'QUIZ')
