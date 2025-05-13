INSERT INTO students(fullname, kana_name, nickname, email, area, telephone, age, sex, remark, is_deleted)
VALUES ('田中太郎','たなかたろう','タロー','tarotaro@gmail.com','茨城県かすみがうら市','080-1234-5678',32,'男','',false);

INSERT INTO students(fullname, kana_name, nickname, email, area, telephone, age,  sex, remark, is_deleted)
VALUES ('佐藤花子','さとうはなこ','ハナチャン','hanakko@gmail.com','奈良県大和郡山市','03-1234-5678',23,'女','',false);

INSERT INTO students(fullname, kana_name, nickname, email, area, telephone, age,  sex, remark, is_deleted)
VALUES ('井戸真由美','いどまゆみ','まゆみ','mayumi456@gmail.com','北海道登別市','070-1244-5678',27,'女','',false);

INSERT INTO students(fullname, kana_name, nickname, email, area, telephone, age,  sex, remark, is_deleted)
VALUES ('鈴木佑一','すずきゆういち','Yuu','yuyuyu@gmail.com','福島県会津若松市','080-1344-5678',21,'男','',false);

INSERT INTO students(fullname, kana_name, nickname, email, area, telephone, age,  sex, remark, is_deleted)
VALUES ('服部次郎','はっとりじろう','ハットリくん','ninja@gmail.com','滋賀県甲賀市','080-1347-0678',33,'その他','',false);


INSERT INTO students_courses(course_name, student_id, course_start_at, course_end_at)
VALUES ('Javaコース',1,'2024-07-15','2025-04-15');

INSERT INTO students_courses(course_name, student_id, course_start_at, course_end_at)
VALUES ('AWSコース',2,'2024-07-20','2025-07-20');

INSERT INTO students_courses(course_name, student_id, course_start_at, course_end_at)
VALUES ('デザインコース',3,'2024-08-12','2025-02-12');

INSERT INTO students_courses(course_name, student_id, course_start_at, course_end_at)
VALUES ('Webマーケティングコース',4,'2024-08-30','2025-08-30');

INSERT INTO students_courses(course_name, student_id, course_start_at, course_end_at)
VALUES ('フロントエンドコース',5,'2024-09-09','2025-03-09');

INSERT INTO students_courses(course_name, student_id, course_start_at, course_end_at)
VALUES ('Javaコース',2,'2024-09-23','2025-03-23');

INSERT INTO students_courses(course_name, student_id, course_start_at, course_end_at)
VALUES ('デザインコース',4,'2024-07-20','2025-07-20');
