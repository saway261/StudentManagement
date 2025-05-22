package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudentCourse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.value.Id;
import raisetech.student.management.mybatis.IdTypeHandler;
import raisetech.student.management.testutil.TestDataFactory;


@MybatisTest
@Import(StudentRepositoryTest.MyBatisTestConfig.class)
class StudentRepositoryTest {

  @TestConfiguration
  static class MyBatisTestConfig implements ConfigurationCustomizer {

    @Override
    public void customize(org.apache.ibatis.session.Configuration configuration) {
      configuration.getTypeHandlerRegistry().register(
          Id.class,
          IdTypeHandler.class
      );
    }
  }

  @Autowired
  private StudentRepository sut;

  @Test
  void アクティブな受講生の全件検索が行えること() {
    List<Student> expected = MyBatisTestDataFactory.makeDummyStudentList();

    List<Student> actual = sut.searchActiveStudentList();

    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 受講生の単一検索が行えること() {
    Id studentId = new Id(1);
    Student actual = sut.searchStudent(studentId);

    assertThat(actual).isEqualTo(MyBatisTestDataFactory.makeStudentDetail1().getStudent());
  }

  @Test
  void 受講生IDに紐づく受講生コースの一覧を検索できること() {
    Id studentId = new Id(2);

    List<StudentCourse> actual = sut.searchCourses(studentId);
    List<StudentCourse> expected = MyBatisTestDataFactory.makeStudentDetail2()
        .getStudentCourseList();

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 受講生IDに紐づく受講生コースIDの一覧を検索できること() {
    Id studentId = new Id(4);
    List<Id> actual = sut.searchCourseIdListLinkedStudentId(studentId);
    List<Id> expected = MyBatisTestDataFactory.makeStudentDetail4()
        .getStudentCourseList()
        .stream().map(course -> course.getCourseId()).toList();

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

  }

  @Test
  void 受講生登録が行えること() {
    // Arrange
    Student beforResister = TestDataFactory.makeCompletedStudent(null);
    Student afterResister = TestDataFactory.makeCompletedStudent(new Id(6));

    List<Student> expected = MyBatisTestDataFactory.makeDummyStudentList();
    expected.add(afterResister);

    // Act
    sut.registerStudent(beforResister);

    // Assert
    List<Student> actual = sut.searchActiveStudentList();
    assertThat(actual.size()).isEqualTo(6);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

  }

  @Test
  void 受講生コース登録が行えること() {
    // Arrange
    Id studentId = new Id(1);
    StudentCourse beforRegister = makeCompletedStudentCourse(studentId, null);
    StudentCourse afterRegister = makeCompletedStudentCourse(studentId, new Id(8));

    List<StudentCourse> expected = new ArrayList<>();
    expected.add(MyBatisTestDataFactory.makeStudentDetail1().getStudentCourseList().get(0));
    expected.add(afterRegister);

    // Act
    sut.registerCourse(beforRegister);

    // Assert
    List<StudentCourse> actual = sut.searchCourses(studentId);
    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 受講生のプロフィール情報更新が行えること() {
    Id studentId = new Id(1);
    Id courseId = new Id(1);
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
    sut.updateStudent(expected);

    Student actual = sut.searchStudent(studentId);

    assertThat(actual).isEqualTo(expected);

  }

  @Test
  void 受講生コース名およびコース終了予定日の更新を行うことができ受講生IDとコース開始日の更新はできないこと() {
    Id studentId = new Id(1);
    Id courseId = new Id(1);
    StudentCourse original = new StudentCourse(
        courseId,
        "Javaコース",
        studentId,
        LocalDate.of(2024, 7, 15),
        LocalDate.of(2025, 4, 15)
    );

    StudentCourse forUpdate = new StudentCourse(
        courseId,
        "AWSコース",
        new Id(2),
        LocalDate.of(2023, 10, 15),
        LocalDate.of(2025, 12, 15)
    );

    sut.updateCourse(forUpdate);

    StudentCourse actual = sut.searchCourses(studentId).stream()
        .filter(sc -> sc.getCourseId().equals(courseId))
        .findFirst()
        .orElseThrow(() -> new AssertionError(
            "指定された courseId の StudentCourse が見つかりませんでした"));

    assertThat(actual.getCourseName()).isEqualTo(forUpdate.getCourseName());
    assertThat(actual.getStudentId()).isEqualTo(original.getStudentId());
    assertThat(actual.getCourseStartAt()).isEqualTo(original.getCourseStartAt());
    assertThat(actual.getCourseEndAt()).isEqualTo(forUpdate.getCourseEndAt());

  }


  @Test
  void 受講生の論理削除が行えること() {

    Id studentId = new Id(1);
    Student forLogicalDelete = new Student(
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
        true //もとはfalse
    );

    List<Student> expected = MyBatisTestDataFactory.makeDummyStudentList();
    expected.removeIf(student -> student.getStudentId().equals(new Id(1)));

    sut.updateStudent(forLogicalDelete);

    List<Student> actual = sut.searchActiveStudentList();

    assertThat(actual.size()).isEqualTo(4);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    assertThat(actual.contains(forLogicalDelete)).isFalse();
  }
  
}