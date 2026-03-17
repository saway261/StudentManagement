package raisetech.student.management.data.domain;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.testutil.TestDataFactory;
import raisetech.student.management.validation.CreateGroup;
import raisetech.student.management.validation.UpdateGroup;

class StudentDetailTest {

  private static Validator validator;

  @BeforeAll
  static void setUpValidator() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void 登録時_受講生がnullのときバリデーション違反が起きる() {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;
    StudentDetail invalidStudentDetail = new StudentDetail(
        null, List.of(TestDataFactory.makeCompletedStudentCourse(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(
        invalidStudentDetail, CreateGroup.class);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("student"))).isTrue();
    // studentCourseでviolationが発生しても、studentでのviolationが確認できれば良いのでanyMatch
  }

  @Test
  void 登録時_受講生コースが空のときバリデーション違反が起きる() {
    // Arrange
    Integer studentId = 1;
    StudentDetail invalidStudentDetail = new StudentDetail(
        TestDataFactory.makeCompletedStudent(studentId), new ArrayList<StudentCourse>()
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(
        invalidStudentDetail, CreateGroup.class);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("studentCourses"))).isTrue();
  }

  @Test
  void 登録時_各フィールドが妥当な値をもつときバリデーション違反が起きない() {
    // Arrange:
    Integer studentId = null;
    Integer courseId = null;
    StudentDetail validStudentDetail = new StudentDetail(
        TestDataFactory.makeCompletedStudent(studentId),
        List.of(TestDataFactory.makeCompletedStudentCourse(studentId,courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(
        validStudentDetail, CreateGroup.class);

    // Act & Assert
    assertThat(violations).isEmpty();
  }

  @Test
  void 更新時_受講生がnullのときバリデーション違反が起きる() {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;
    StudentDetail invalidStudentDetailForm = new StudentDetail(
        null, List.of(TestDataFactory.makeCompletedStudentCourse(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(
        invalidStudentDetailForm,
        UpdateGroup.class);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("student"))).isTrue();
    // studentCourseでviolationが発生しても、studentでのviolationが確認できれば良いのでanyMatch
  }

  @Test
  void 登録時および更新時_受講生コースが空のときバリデーション違反が起きる() {
    // Arrange
    Integer studentId = 1;
    StudentDetail invalidStudentDetail = new StudentDetail(
        TestDataFactory.makeCompletedStudent(studentId), new ArrayList<StudentCourse>()
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(
        invalidStudentDetail,
        UpdateGroup.class);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("studentCourses"))).isTrue();
  }


  @Test
  void 更新時_各フィールドが妥当な値をもつときバリデーション違反が起きない() {
    // Arrange:
    Integer studentId = 1;
    Integer courseId = 1;
    StudentDetail validStudentDetail = new StudentDetail(
        TestDataFactory.makeCompletedStudent(studentId),
        List.of(TestDataFactory.makeCompletedStudentCourse(studentId,courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(
        validStudentDetail,
        UpdateGroup.class);

    // Act & Assert
    assertThat(violations).isEmpty();
  }

}