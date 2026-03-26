package raisetech.student.management.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.master.Course;

public class MyBatisTestDataFactory {

  /**
   * インスタンス化を防ぐprivateコンストラクタ
   */
  private MyBatisTestDataFactory() {
  }

  static StudentDetail makeDummyStudentDetail1() {

    Integer studentId = 1;
    Integer scId = 1;

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
            scId,
            1,
            "JA",
            LocalDate.of(2024, 7, 15),
            LocalDate.of(2025, 4, 15)
        ))
    );
  }

  static StudentDetail makeDummyStudentDetail2() {

    Integer studentId = 2;
    Integer scId1 = 2;
    Integer scId2 = 6;

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
                scId1,
                2,
                "AW",
                LocalDate.of(2024, 7, 20),
                LocalDate.of(2025, 7, 20)
            ),
            new StudentCourse(
                scId2,
                2,
                "JA",
                LocalDate.of(2024, 9, 23),
                LocalDate.of(2025, 3, 23)
            )
        )
    );
  }

  static StudentDetail makeDummyStudentDetail3() {

    Integer studentId = 3;
    Integer scId = 3;

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
            scId,
            3,
            "DE",
            LocalDate.of(2024, 8, 12),
            LocalDate.of(2025, 2, 12)
        ))
    );
  }

  static StudentDetail makeDummyStudentDetail4() {

    Integer studentId = 4;
    Integer scId1 = 4;
    Integer scId2 = 7;

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
                scId1,
                4,
                "WM",
                LocalDate.of(2024, 8, 30),
                LocalDate.of(2025, 8, 30)
            ),
            new StudentCourse(
                scId2,
                4,
                "DE",
                LocalDate.of(2024, 7, 20),
                LocalDate.of(2025, 7, 20)
            )
        )
    );
  }

  static StudentDetail makeDummyStudentDetail5() {

    Integer studentId = 5;
    Integer scId = 5;

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
            scId,
            5,
            "FR",
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

  static Course makeDummyCourse1(){
    return new Course("JA","Javaコース");
  }

  static Course makeDummyCourse2(){
    return new Course("AW","AWSコース");
  }

  static Course makeDummyCourse3(){
    return new Course("DE","デザインコース");
  }

  static Course makeDummyCourse4(){
    return new Course("WM","Webマーケティングコース");
  }

  static Course makeDummyCourse5(){
    return new Course("FR","フロントエンド開発コース");
  }

  static List<Course> makeDummyCourseList(){
    List<Course> dummyCourseList = new ArrayList<>();
    dummyCourseList.add(makeDummyCourse1());
    dummyCourseList.add(makeDummyCourse2());
    dummyCourseList.add(makeDummyCourse3());
    dummyCourseList.add(makeDummyCourse4());
    dummyCourseList.add(makeDummyCourse5());

    return dummyCourseList;
  }

}