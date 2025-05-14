package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudent;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudentCourse;

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
    int studentId = 1;
    Student actual = sut.searchStudent(studentId);

    assertThat(actual.getStudentId()).isEqualTo(studentId);
  }

  @Test
  void 受講生IDに紐づく受講生コースの一覧を検索できること() {
    int studentId = 2;
    List<StudentCourse> actual = sut.searchCourses(studentId);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual.stream()
        .allMatch(course -> course.getStudentId() == studentId)).isTrue();
  }

  @Test
  void 受講生IDに紐づく受講生コースIDの一覧を検索できること() {
    int studentId = 4;
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


}