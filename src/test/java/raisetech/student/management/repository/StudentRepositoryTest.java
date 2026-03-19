package raisetech.student.management.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.testutil.TestDataFactory;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void アクティブな受講生IDの全件検索が行えること() {
    List<Integer> expected = List.of(1,2,3,4,5);

    List<Integer> actual = sut.searchActiveStudentIdList();

    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 受講生の単一検索が行えること() {
    int studentId = 1;
    Student actual = sut.searchStudent(studentId);

    assertThat(actual).isEqualTo(MyBatisTestDataFactory.makeDummyStudentDetail1().getStudent());
  }

  @Test
  void 存在しない受講生IDを指定したときnullが返ること() {
    int studentId = 999;
    Student actual = sut.searchStudent(studentId);

    assertThat(actual).isNull();
  }

  @Test
  void 受講生IDに紐づく受講生コースの一覧を検索できること() {
    int studentId = 2;

    List<StudentCourse> actual = sut.searchStudentCourses(studentId);
    List<StudentCourse> expected = MyBatisTestDataFactory.makeDummyStudentDetail2()
        .getStudentCourses();

    assertThat(actual.size()).isEqualTo(expected.size());
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 存在しない受講生IDを指定したとき空のリストが返ること() {
    int studentId = 999;
    List<StudentCourse> actual = sut.searchStudentCourses(studentId);

    assertThat(actual).isNotNull();
    assertThat(actual).isEmpty();
  }

  @Test
  void 受講生登録が行えること() {
    // Arrange
    List<Student> existing = MyBatisTestDataFactory.makeDummyStudentList();
    int existingSize = existing.size();
    Student beforeResister = TestDataFactory.makeCompletedStudent(null);
    Student afterResister = TestDataFactory.makeCompletedStudent(existingSize + 1);

    List<Student> expected = new ArrayList<>(existing);
    expected.add(afterResister);

    // Act
    sut.registerStudent(beforeResister);

    // Assert
    List<Student> actual = sut.searchActiveStudentIdList().stream()
        .map(sut::searchStudent)
        .toList();
    assertThat(actual.size()).isEqualTo(existingSize + 1);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

  }

  @Test
  void 受講生コース登録が行えること() {
    // Arrange
    Integer studentId = 1;
    int existingCoursesSize =
        MyBatisTestDataFactory.makeDummyStudentDetailList().stream()
            .mapToInt(sd -> sd.getStudentCourses().size())
            .sum();

    StudentCourse beforeRegister = TestDataFactory.makeCompletedStudentCourse(studentId, null);
    StudentCourse afterRegister = TestDataFactory.makeCompletedStudentCourse(studentId, existingCoursesSize + 1);
    List<StudentCourse> expected = new ArrayList<>(
        MyBatisTestDataFactory.makeDummyStudentDetail1().getStudentCourses()
    );
    int originalSize = expected.size();
    expected.add(afterRegister);

    // Act
    sut.registerStudentCourse(beforeRegister);

    // Assert
    List<StudentCourse> actual = sut.searchStudentCourses(studentId);
    assertThat(actual.size()).isEqualTo(originalSize + 1);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 受講生の更新が行えること() {
    // Arrange
    Integer studentId = 1;
    Student expected = new Student(
        studentId,
        "高橋太郎",// もとは「田中太郎」
        "たかはしたろう",// もとは「たなかたろう」
        "タロウ",// 「タロー」
        "tarotarotaro@gmail.com",// もとは「tarotaro@gmail.com」
        "茨城県東茨城郡城里町",// もとは「茨城県かすみがうら市」
        "080-9876-5432",// もとは「080-1234-5678」
        35,// もとは「32」
        "その他",// もとは「男」
        "転職活動中",// もとは空文字
        true // もとはfalse
    );

    // Act
    Integer updated = sut.updateStudent(expected);
    Student actual = sut.searchStudent(studentId);

    // Assert
    assertThat(actual).isEqualTo(expected);
    assertThat(updated).isEqualTo(1);
  }

  @Test
  void 存在しない受講生IDを更新しようとすると更新件数が0件であること() {
    Integer studentId = 999;
    Student student = TestDataFactory.makeCompletedStudent(studentId);

    int actual = sut.updateStudent(student);

    assertThat(actual).isZero();
  }

  @Test
  void 受講生コース名の更新を行うことができ受講生IDとコース開始日とコース終了予定日の更新はできないこと() {
    Integer studentId = 1;
    Integer courseId = 1;
    StudentCourse original = new StudentCourse(
        courseId,
        studentId,
        "Javaコース",
        LocalDate.of(2024, 7, 15),
        LocalDate.of(2025, 4, 15)
    );

    StudentCourse forUpdate = new StudentCourse(
        courseId,
        2,
        "AWSコース",
        LocalDate.of(2023, 10, 15),
        LocalDate.of(2025, 12, 15)
    );

    // Act
    Integer updated = sut.updateStudentCourse(forUpdate);

    StudentCourse actual = sut.searchStudentCourses(studentId).stream()
        .filter(sc -> sc.getCourseId().equals(courseId))
        .findFirst()
        .orElseThrow(() -> new AssertionError(
            "指定された courseId の StudentCourse が見つかりませんでした"));

    // Assert
    assertThat(actual.getStudentId()).isEqualTo(original.getStudentId());
    assertThat(actual.getCourseName()).isEqualTo(forUpdate.getCourseName());
    assertThat(actual.getCourseStartAt()).isEqualTo(original.getCourseStartAt());
    assertThat(actual.getCourseEndAt()).isEqualTo(original.getCourseEndAt());
    assertThat(updated).isEqualTo(1);

  }

  @Test
  void 存在しない受講生コースIDを更新しようとすると更新件数が0件であること() {
    int courseId = 999;
    StudentCourse course = TestDataFactory.makeCompletedStudentCourse(1, courseId);

    int actual = sut.updateStudentCourse(course);

    assertThat(actual).isZero();
  }

}