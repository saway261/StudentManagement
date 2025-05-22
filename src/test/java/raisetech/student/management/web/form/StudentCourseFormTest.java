package raisetech.student.management.web.form;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.value.Id;
import raisetech.student.management.testutil.TestDataFactory;

class StudentCourseFormTest {

  @Test
  void 登録時_StudentCourseFormがStudentCourseに変換されcourseIdがnullでもエラーにならないこと() {
    // Arrange
    Id studentId = new Id(1);
    LocalDate now = LocalDate.now();
    StudentCourseForm form = TestDataFactory.makeCompletedStudentCourseForm(null);

    // Act
    StudentCourse domain = StudentCourseForm.toDomain(form, studentId);

    // Assert
    assertThat(domain.getCourseId()).isNull();
    assertThat(domain.getCourseName()).isEqualTo(form.getCourseName());
    assertThat(domain.getStudentId()).isEqualTo(studentId);
    assertThat(domain.getCourseStartAt()).isEqualTo(now);
    assertThat(domain.getCourseEndAt()).isEqualTo(now.plusMonths(6));

  }

  @Test
  void 更新時_StudentCourseFormがStudentCourseに変換されcourseIdがInteger型からIdに変換されること() {
    // Arrange
    Id studentId = new Id(1);
    Integer courseId = 2;
    StudentCourseForm form = TestDataFactory.makeCompletedStudentCourseForm(courseId);

    // Act
    StudentCourse domain = StudentCourseForm.toDomain(form, studentId);

    // Assert
    assertThat(domain.getCourseId()).isEqualTo(new Id(courseId));
    assertThat(domain.getCourseName()).isEqualTo(form.getCourseName());
    assertThat(domain.getStudentId()).isEqualTo(studentId);
    assertThat(domain.getCourseStartAt()).isNull();
    assertThat(domain.getCourseEndAt()).isEqualTo(form.getCourseEndAt());

  }

}