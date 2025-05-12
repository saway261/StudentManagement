package raisetech.student.management.web.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudentDetail;
import static raisetech.student.management.testutil.TestDataFactory.makeDummyStudentDetailFormOnUpdate;

import org.junit.jupiter.api.Test;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.value.Id;
import raisetech.student.management.web.form.StudentDetailForm;
import raisetech.student.management.web.response.StudentCourseResponse;
import raisetech.student.management.web.response.StudentDetailResponse;
import raisetech.student.management.web.response.StudentResponse;

class StudentMapperTest {

  private final StudentMapper mapper = new StudentMapper();

  @Test
  void toDomain_正常系_studentIdとcourseIdがId型に変換されていること() {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 2;
    StudentDetailForm form = makeDummyStudentDetailFormOnUpdate(studentId, courseId);

    // Act
    StudentDetail domain = mapper.toDomain(form);

    // Assert
    assertThat(domain.getStudent().getStudentId()).isEqualTo(new Id(studentId));
    assertThat(domain.getStudentCourseList()).hasSize(
        form.getStudentCourseList().size());
    assertThat(domain.getStudentCourseList().get(0).getCourseId()).isEqualTo(new Id(courseId));
  }

  @Test
  void toDomain_異常系_studentIdが1未満のときId生成時に例外が発生すること() {
    // Arrange
    Integer invalidStudentId = 0; // ← 無効
    Integer validCourseId = 1;
    StudentDetailForm form = makeDummyStudentDetailFormOnUpdate(invalidStudentId, validCourseId);

    // Act & Assert
    assertThatThrownBy(() -> mapper.toDomain(form))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("IDはnullまたは1以上でなければなりません");
  }

  @Test
  void toDomain_異常系_courseIdが1未満のときId生成時に例外が発生すること() {
    // Arrange
    Integer validStudentId = 1;
    Integer invalidCourseId = 0; // ← 無効
    StudentDetailForm form = makeDummyStudentDetailFormOnUpdate(validStudentId, invalidCourseId);

    // Act & Assert
    assertThatThrownBy(() -> mapper.toDomain(form))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("IDはnullまたは1以上でなければなりません");
  }

  @Test
  void fromDomain_正常系_ドメインモデルがレスポンスDTOに正しく変換されること() {
    // Arrange
    Id studentId = new Id(1);
    Id courseId = new Id(2);
    StudentDetail domain = makeCompletedStudentDetail(studentId, courseId);

    // Act
    StudentDetailResponse response = mapper.fromDomain(domain);

    // Assert: student部分
    StudentResponse student = response.getStudent();
    assertThat(student.getStudentId()).isEqualTo(studentId.getValue());
    assertThat(student.getFullname()).isEqualTo(domain.getStudent().getFullname());
    assertThat(student.getEmail()).isEqualTo(domain.getStudent().getEmail());

    // Assert: courseList部分
    assertThat(response.getStudentCourseList()).hasSize(domain.getStudentCourseList().size());
    StudentCourseResponse course = response.getStudentCourseList().get(0);
    assertThat(course.getCourseId()).isEqualTo(courseId.getValue());
    assertThat(course.getCourseName()).isEqualTo(
        domain.getStudentCourseList().get(0).getCourseName());
    assertThat(course.getCourseEndAt()).isEqualTo(
        domain.getStudentCourseList().get(0).getCourseEndAt());
  }

}
