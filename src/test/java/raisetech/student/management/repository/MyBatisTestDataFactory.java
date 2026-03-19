package raisetech.student.management.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;

public class MyBatisTestDataFactory {

  static StudentDetail makeDummyStudentDetail1() {

    Integer studentId = 1;
    Integer courseId = 1;

    return new StudentDetail(
        new Student(
            studentId,
            "田中太郎",
            "たなかたろう",
            "タロー",
            "tarotaro@gmail.com",
            "茨城県かすみがうら市",
            "080-1234-5678",
            32,
            "男",
            "",
            false
        ),
        List.of(new StudentCourse(
            courseId,
            1,
            "Javaコース",
            LocalDate.of(2024, 7, 15),
            LocalDate.of(2025, 4, 15)
        ))
    );
  }

  static StudentDetail makeDummyStudentDetail2() {

    Integer studentId = 2;
    Integer courseId1 = 2;
    Integer courseId2 = 6;

    return new StudentDetail(
        new Student(
            studentId,
            "佐藤花子",
            "さとうはなこ",
            "ハナチャン",
            "hanakko@gmail.com",
            "奈良県大和郡山市",
            "03-1234-5678",
            23,
            "女",
            "",
            false
        ),
        List.of(
            new StudentCourse(
                courseId1,
                2,
                "AWSコース",
                LocalDate.of(2024, 7, 20),
                LocalDate.of(2025, 7, 20)
            ),
            new StudentCourse(
                courseId2,
                2,
                "Javaコース",
                LocalDate.of(2024, 9, 23),
                LocalDate.of(2025, 3, 23)
            )
        )
    );
  }

  static StudentDetail makeDummyStudentDetail3() {

    Integer studentId = 3;
    Integer courseId = 3;

    return new StudentDetail(
        new Student(
            studentId,
            "井戸真由美",
            "いどまゆみ",
            "まゆみ",
            "mayumi456@gmail.com",
            "北海道登別市",
            "070-1244-5678",
            27,
            "女",
            "",
            false
        ),
        List.of(new StudentCourse(
            courseId,
            3,
            "デザインコース",
            LocalDate.of(2024, 8, 12),
            LocalDate.of(2025, 2, 12)
        ))
    );
  }

  static StudentDetail makeDummyStudentDetail4() {

    Integer studentId = 4;
    Integer courseId1 = 4;
    Integer courseId2 = 7;

    return new StudentDetail(
        new Student(
            studentId,
            "鈴木佑一",
            "すずきゆういち",
            "Yuu",
            "yuyuyu@gmail.com",
            "福島県会津若松市",
            "080-1344-5678",
            21,
            "男",
            "",
            false
        ),
        List.of(
            new StudentCourse(
                courseId1,
                4,
                "Webマーケティングコース",
                LocalDate.of(2024, 8, 30),
                LocalDate.of(2025, 8, 30)
            ),
            new StudentCourse(
                courseId2,
                4,
                "デザインコース",
                LocalDate.of(2024, 7, 20),
                LocalDate.of(2025, 7, 20)
            )
        )
    );
  }

  static StudentDetail makeDummyStudentDetail5() {

    Integer studentId = 5;
    Integer courseId = 5;

    return new StudentDetail(
        new Student(
            studentId,
            "服部次郎",
            "はっとりじろう",
            "ハットリくん",
            "ninja@gmail.com",
            "滋賀県甲賀市",
            "080-1347-0678",
            33,
            "その他",
            "",
            false),
        List.of(new StudentCourse(
            courseId,
            5,
            "フロントエンドコース",
            LocalDate.of(2024, 9, 9),
            LocalDate.of(2025, 3, 9)
        ))
    );
  }

  static List<StudentDetail> makeDummyStudentDetailList() {
    List<StudentDetail> dummyStudentDetailList = new ArrayList<>();
    dummyStudentDetailList.add(makeDummyStudentDetail1());
    dummyStudentDetailList.add(makeDummyStudentDetail2());
    dummyStudentDetailList.add(makeDummyStudentDetail3());
    dummyStudentDetailList.add(makeDummyStudentDetail4());
    dummyStudentDetailList.add(makeDummyStudentDetail5());

    return dummyStudentDetailList;
  }

  static List<Student> makeDummyStudentList() {
    List<Student> dummyStudents = new ArrayList<>();
    dummyStudents.add(makeDummyStudentDetail1().getStudent());
    dummyStudents.add(makeDummyStudentDetail2().getStudent());
    dummyStudents.add(makeDummyStudentDetail3().getStudent());
    dummyStudents.add(makeDummyStudentDetail4().getStudent());
    dummyStudents.add(makeDummyStudentDetail5().getStudent());

    return dummyStudents;
  }

}