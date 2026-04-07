INSERT INTO students(full_name, kana_name, nickname, email, area, telephone, age, sex, remark, is_deleted)
VALUES ('田中太郎','たなかたろう','タロー','tarotaro@gmail.com','茨城県かすみがうら市','080-1234-5678',32,'男','',false);

INSERT INTO students(full_name, kana_name, nickname, email, area, telephone, age,  sex, remark, is_deleted)
VALUES ('佐藤花子','さとうはなこ','ハナチャン','hanakko@gmail.com','奈良県大和郡山市','03-1234-5678',23,'女','',false);

INSERT INTO students(full_name, kana_name, nickname, email, area, telephone, age,  sex, remark, is_deleted)
VALUES ('井戸真由美','いどまゆみ','まゆみ','mayumi456@gmail.com','北海道登別市','070-1244-5678',27,'女','',false);

INSERT INTO students(full_name, kana_name, nickname, email, area, telephone, age,  sex, remark, is_deleted)
VALUES ('鈴木佑一','すずきゆういち','Yuu','yuyuyu@gmail.com','福島県会津若松市','080-1344-5678',21,'男','',false);

INSERT INTO students(full_name, kana_name, nickname, email, area, telephone, age,  sex, remark, is_deleted)
VALUES ('服部次郎','はっとりじろう','ハットリくん','ninja@gmail.com','滋賀県甲賀市','080-1347-0678',33,'その他','',true);

INSERT INTO course_master VALUES ('JA','Javaコース');
INSERT INTO course_master VALUES ('AW','AWSコース');
INSERT INTO course_master VALUES ('DE','デザインコース');
INSERT INTO course_master VALUES ('WM','Webマーケティングコース');
INSERT INTO course_master VALUES ('FR','フロントエンド開発コース');

INSERT INTO status_master VALUES (1,'仮申込',false);
INSERT INTO status_master VALUES (2,'本申込',false);
INSERT INTO status_master VALUES (3,'受講中',false);
INSERT INTO status_master VALUES (4,'受講終了',true);
INSERT INTO status_master VALUES (5,'キャンセル',true);

INSERT INTO status_transition_master VALUES (1,2);
INSERT INTO status_transition_master VALUES (1,5);
INSERT INTO status_transition_master VALUES (2,3);
INSERT INTO status_transition_master VALUES (2,5);
INSERT INTO status_transition_master VALUES (3,4);
INSERT INTO status_transition_master VALUES (3,5);

INSERT INTO student_courses(course_code, student_id, status_id, course_apply_at, course_start_at, course_planned_end_at, course_finished_at)
VALUES ('JA',1,3,'2024-07-10','2024-07-15','2025-04-15',null);

INSERT INTO student_courses(course_code, student_id, status_id, course_apply_at, course_start_at, course_planned_end_at, course_finished_at)
VALUES ('AW',2,2,'2024-07-15',null,null,null);

INSERT INTO student_courses(course_code, student_id, status_id, course_apply_at, course_start_at, course_planned_end_at, course_finished_at)
VALUES ('DE',3,4,'2024-08-07','2024-08-12','2025-02-12','2025-02-10');

INSERT INTO student_courses(course_code, student_id, status_id, course_apply_at, course_start_at, course_planned_end_at, course_finished_at)
VALUES ('WM',4,5,'2024-08-25',null,null,null);

INSERT INTO student_courses(course_code, student_id, status_id, course_apply_at, course_start_at, course_planned_end_at, course_finished_at)
VALUES ('FR',5,1,'2024-09-04',null,null,null);

INSERT INTO student_courses(course_code, student_id, status_id, course_apply_at, course_start_at, course_planned_end_at, course_finished_at)
VALUES ('JA',2,3,'2024-09-18','2024-09-23','2025-03-23',null);

INSERT INTO student_courses(course_code, student_id, status_id, course_apply_at, course_start_at, course_planned_end_at, course_finished_at)
VALUES ('DE',4,3,'2024-07-15','2024-07-20','2025-07-20',null);

