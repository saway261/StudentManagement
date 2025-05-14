package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudent;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudentCourse;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void アクティブな受講生の全件検索が行えること() {
    List<Student> actual = sut.searchActiveStudentList();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生の単一検索が行えること() {
    Integer studentId = 1;
    Student actual = sut.searchStudent(studentId);

    assertThat(actual.getStudentId()).isEqualTo(studentId);
  }

  @Test
  void 受講生IDに紐づく受講生コースの一覧を検索できること() {
    Integer studentId = 2;
    List<StudentCourse> actual = sut.searchCourses(studentId);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual.stream()
        .allMatch(course -> course.getStudentId() == studentId)).isTrue();
  }

  @Test
  void 受講生IDに紐づく受講生コースIDの一覧を検索できること() {
    Integer studentId = 4;
    List<Integer> actual = sut.searchCourseIdListLinkedStudentId(studentId);

    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void 受講生登録が行えること() {
    sut.registerStudent(makeCompletedStudent(null));

    List<Student> actual = sut.searchActiveStudentList();
    assertThat(actual.size()).isEqualTo(6);

  }

  @Test
  void 受講生コース登録が行えること() {
    int studentId = 1;
    sut.registerCourse(makeCompletedStudentCourse(studentId, null));

    List<StudentCourse> actual = sut.searchCourses(studentId);
    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual.stream()
        .allMatch(course -> course.getStudentId() == studentId)).isTrue();
  }

  @Test
  void 受講生のプロフィール情報更新が行えること() {
    Integer studentId = 1;
    Integer courseId = 1;
    Student expected = new Student(
        studentId,
        "田中太郎",
        "たなかたろう",
        "タロー",
        "tarotarotaro@gmail.com",// もとは「tarotaro@gmail.com」
        "茨城県東茨城郡城里町",// もとは「茨城県かすみがうら市」
        "080-1234-5678",
        32,
        "男",
        "転職活動中",// もとは空文字
        false
    );
    sut.updateStudent(expected);

    Student actual = sut.searchStudent(studentId);

    assertThat(actual.getFullname()).isEqualTo(expected.getFullname());
    assertThat(actual.getKanaName()).isEqualTo(expected.getKanaName());
    assertThat(actual.getNickname()).isEqualTo(expected.getNickname());
    assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    assertThat(actual.getArea()).isEqualTo(expected.getArea());
    assertThat(actual.getTelephone()).isEqualTo(expected.getTelephone());
    assertThat(actual.getAge()).isEqualTo(expected.getAge());
    assertThat(actual.getSex()).isEqualTo(expected.getSex());
    assertThat(actual.getRemark()).isEqualTo(expected.getRemark());
    assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted());

  }

  @Test
  void 受講生コース情報の更新が行えること() {
    Integer courseId = 1;
    StudentCourse expected = new StudentCourse(
        courseId,
        "Javaコース",
        1,
        null,
        LocalDate.of(2025, 4, 15)
    );


  }

  @Test
  void 受講生の論理削除が行えること() {

  }


}