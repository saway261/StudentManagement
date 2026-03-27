CREATE TABLE IF NOT EXISTS students (
student_id INT AUTO_INCREMENT PRIMARY KEY,
full_name VARCHAR(50) NOT NULL,
kana_name VARCHAR(50) NOT NULL,
nickname VARCHAR(50),
email VARCHAR(50) NOT NULL UNIQUE,
area VARCHAR(50) ,
telephone VARCHAR(14) UNIQUE,
age INT,
sex VARCHAR(10),
remark VARCHAR(200),
is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE IF NOT EXISTS course_master (
course_code VARCHAR(5) PRIMARY KEY ,
course_name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS status_master (
status_id INT PRIMARY KEY,
status_name VARCHAR(10) NOT NULL UNIQUE,
is_terminal BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS student_courses (
student_course_id INT AUTO_INCREMENT PRIMARY KEY ,
student_id INT NOT NULL,
course_code VARCHAR(5) NOT NULL,
status_id INT NOT NULL DEFAULT 1,
course_apply_at TIMESTAMP NOT NULL,
course_start_at TIMESTAMP,
course_end_at TIMESTAMP,
FOREIGN KEY (student_id) REFERENCES students(student_id),
FOREIGN KEY (course_code) REFERENCES course_master(course_code),
FOREIGN KEY (status_id) REFERENCES status_master(status_id)
);