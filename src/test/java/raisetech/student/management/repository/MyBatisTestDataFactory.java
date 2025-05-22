package raisetech.student.management.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.value.Id;

public class MyBatisTestDataFactory {

  static StudentDetail makeDummyStudentDetail1() {

    Id studentId = new Id(1);
    Id courseId = new Id(1);

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
            "Javaコース",
            new Id(1),
            LocalDate.of(2024, 7, 15),
            LocalDate.of(2025, 4, 15)
        ))
    );
  }

  static StudentDetail makeDummyStudentDetail2() {

    Id studentId = new Id(2);
    Id courseId1 = new Id(2);
    Id courseId2 = new Id(6);

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
                "AWSコース",
                new Id(2),
                LocalDate.of(2024, 7, 20),
                LocalDate.of(2025, 7, 20)
            ),
            new StudentCourse(
                courseId2,
                "Javaコース",
                new Id(2),
                LocalDate.of(2024, 9, 23),
                LocalDate.of(2025, 3, 23)
            )
        )
    );
  }

  static StudentDetail makeDummyStudentDetail3() {

    Id studentId = new Id(3);
    Id courseId = new Id(3);

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
            "デザインコース",
            new Id(3),
            LocalDate.of(2024, 8, 12),
            LocalDate.of(2025, 2, 12)
        ))
    );
  }

  static StudentDetail makeDummyStudentDetail4() {

    Id studentId = new Id(4);
    Id courseId1 = new Id(4);
    Id courseId2 = new Id(7);

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
                "Webマーケティングコース",
                new Id(4),
                LocalDate.of(2024, 8, 30),
                LocalDate.of(2025, 8, 30)
            ),
            new StudentCourse(
                courseId2,
                "デザインコース",
                new Id(4),
                LocalDate.of(2024, 7, 20),
                LocalDate.of(2025, 7, 20)
            )
        )
    );
  }

  static StudentDetail makeDummyStudentDetail5() {

    Id studentId = new Id(5);
    Id courseId = new Id(5);

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
            "フロントエンドコース",
            new Id(5),
            LocalDate.of(2024, 9, 9),
            LocalDate.of(2025, 3, 9)
        ))
    );
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
