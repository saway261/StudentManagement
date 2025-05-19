package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudent;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudentCourse;

import java.time.LocalDate;
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
    List<Student> actual = sut.searchActiveStudentList();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生の単一検索が行えること() {
    Id studentId = new Id(1);
    Student actual = sut.searchStudent(studentId);

    assertThat(actual.getStudentId()).isEqualTo(studentId);
  }

  @Test
  void 受講生IDに紐づく受講生コースの一覧を検索できること() {
    Id studentId = new Id(2);
    List<StudentCourse> actual = sut.searchCourses(studentId);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual.stream()
        .allMatch(course -> course.getStudentId().equals(studentId))).isTrue();
  }

  @Test
  void 受講生IDに紐づく受講生コースIDの一覧を検索できること() {
    Id studentId = new Id(4);
    List<Id> actual = sut.searchCourseIdListLinkedStudentId(studentId);

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
    Id studentId = new Id(1);
    sut.registerCourse(makeCompletedStudentCourse(studentId, null));

    List<StudentCourse> actual = sut.searchCourses(studentId);
    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual.stream()
        .allMatch(course -> course.getStudentId().equals(studentId))).isTrue();
  }

  @Test
  void 受講生のプロフィール情報更新が行えること() {
    Id studentId = new Id(1);
    Id courseId = new Id(1);
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

    sut.updateStudent(forLogicalDelete);

    List<Student> actual = sut.searchActiveStudentList();

    assertThat(actual.size()).isEqualTo(4);
    assertThat(
        actual.stream().allMatch(student -> student.getStudentId().equals(studentId))).isFalse();

  }


}