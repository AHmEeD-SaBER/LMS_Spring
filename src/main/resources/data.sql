-- Insert a new instructor
INSERT INTO instructor (username, password, email, first_name, last_name, user_role, user_type)
VALUES ('instructor1', 'password123', 'instructor1@example.com', 'Instructor', 'One', 'INSTRUCTOR', 'INSTRUCTOR');

-- Insert a student
INSERT INTO student (username, password, email, first_name, last_name, user_role, user_type)
VALUES ('student1', 'password123', 'student1@example.com', 'Student', 'One', 'STUDENT', 'STUDENT');

-- Insert courses (link to instructor via username or email)
INSERT INTO course (title, description, instructor_id)
SELECT 'Introduction to Java', 'Learn the basics of Java programming.', id
FROM users WHERE username = 'instructor1';

INSERT INTO course (title, description, instructor_id)
SELECT 'Spring Framework Basics', 'Master the basics of the Spring Framework.', id
FROM users WHERE username = 'instructor1';

-- Enroll a student into courses (dynamic ID resolution)
INSERT INTO student_courses (student_id, course_id)
SELECT s.id, c.id
FROM users s, course c
WHERE s.username = 'student1' AND c.title = 'Introduction to Java';

INSERT INTO student_courses (student_id, course_id)
SELECT s.id, c.id
FROM users s, course c
WHERE s.username = 'student1' AND c.title = 'Spring Framework Basics';