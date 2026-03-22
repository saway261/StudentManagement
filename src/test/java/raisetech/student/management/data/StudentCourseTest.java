package raisetech.student.management.data;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import raisetech.student.management.testutil.TestDataFactory;
import raisetech.student.management.validation.CreateGroup;
import raisetech.student.management.validation.UpdateGroup;

class StudentCourseTest {

  private static Validator validator;

  @BeforeAll
  static void setUpValidator() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @ParameterizedTest(name = "[{index}] 登録時_フィールド: {0} がnullのとき violation={1}")
  @CsvSource({// trueはNotNull, falseはnull許容
      "courseId,false",
      "studentId,false",
      "courseCode,true",
      "courseStartAt,false",
      "courseEndAt,false"
  })
  void 登録時_各フィールドのnull許容性のテスト(String fieldName,
      boolean expectViolation) {
    // Arrange
    LocalDate now = LocalDate.now();
    StudentCourse studentCourse = new StudentCourse(
        fieldName.equals("courseId") ? null : 1,
        fieldName.equals("studentId") ? null : 1,
        fieldName.equals("courseCode") ? null : "JA",
        fieldName.equals("courseStartAt") ? null : now,
        fieldName.equals("courseEndAt") ? null : now.plusMonths(6)
    );

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse,
        CreateGroup.class);

    // Act & Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @ParameterizedTest(name = "[{index}] 登録時 courseCodeが{0}文字のとき violation={1}")
  @CsvSource({
      "4,false",
      "5,false",
      "6,true"
  })
  void 登録時_コースコードの文字数の境界値テスト(int size, boolean expectViolation)throws Exception{
    // Arrange
    Integer studentId = null;
    Integer courseId = null;
    String over10char = "あ".repeat(size);
    LocalDate now = LocalDate.now();
    StudentCourse studentCourse = new StudentCourse(
        courseId,
        studentId,
        over10char,
        now,
        now.plusMonths(6)
    );

    // Act
    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse,
        CreateGroup.class);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("courseCode"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }

  }

  @Test
  void 登録時_StudentCourseの各のフィールドが妥当な値を持つときバリデーション違反が起きない(){
    Integer studentId = null;
    Integer courseId = null;
    StudentCourse validStudentCourse = TestDataFactory.makeCompletedStudentCourse(studentId,courseId);

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(validStudentCourse,
        CreateGroup.class);

    assertThat(violations).isEmpty();
  }

  @ParameterizedTest(name = "[{index}] 更新時_フィールド: {0} がnullのとき violation={1}")
  @CsvSource({// trueはNotNull, falseはnull許容
      "courseId,true",
      "studentId,false",
      "courseCode,true",
      "courseStartAt,false",
      "courseEndAt,false"
  })
  void 更新時_各フィールドのnull許容性のテスト(String fieldName,
      boolean expectViolation) {
    // Arrange
    LocalDate now = LocalDate.now();
    StudentCourse studentCourse = new StudentCourse(
        fieldName.equals("courseId") ? null : 1,
        fieldName.equals("studentId") ? null : 1,
        fieldName.equals("courseCode") ? null : "JA",
        fieldName.equals("courseStartAt") ? null : now,
        fieldName.equals("courseEndAt") ? null : now.plusMonths(6)
    );

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse,
        UpdateGroup.class);

    // Act & Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @Test
  void 更新時_courseIdが1未満の数値の時バリデーション違反が起きる() {
    Integer studentId = 1;
    Integer courseId = -3;
    StudentCourse invalidStudentCourse = TestDataFactory.makeCompletedStudentCourse(studentId,courseId);

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(invalidStudentCourse,
        UpdateGroup.class);
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("courseId"))).isTrue();
  }

  @ParameterizedTest(name = "[{index}] 更新時 courseCodeが{0}文字のとき violation={1}")
  @CsvSource({
      "4,false",
      "5,false",
      "6,true"
  })
  void 更新時_コースコードの文字数の境界値テスト(int size, boolean expectViolation)throws Exception{
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;
    String over10char = "あ".repeat(size);
    LocalDate now = LocalDate.now();
    StudentCourse studentCourse = new StudentCourse(
        courseId,
        studentId,
        over10char,
        now,
        now.plusMonths(6)
    );

    // Act
    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse,
        UpdateGroup.class);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("courseCode"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }

  }

  @Test
  void 更新時_StudentCourseの各のフィールドが妥当な値を持つときバリデーション違反が起きない(){
    Integer studentId = null;
    Integer courseId = 1;
    StudentCourse validStudentCourse = TestDataFactory.makeCompletedStudentCourse(studentId,courseId);

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(validStudentCourse,
        UpdateGroup.class);

    assertThat(violations).isEmpty();
  }

}