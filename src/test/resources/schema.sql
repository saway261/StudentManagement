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
is_deleted BOOLEAN DEFAULT FALSE NOT NULL);

CREATE TABLE IF NOT EXISTS student_courses (
course_id INT AUTO_INCREMENT PRIMARY KEY ,
student_id INT NOT NULL,
course_name VARCHAR(20) NOT NULL,
course_start_at TIMESTAMP NOT NULL,
course_end_at TIMESTAMP,
FOREIGN KEY (student_id) REFERENCES students(student_id));