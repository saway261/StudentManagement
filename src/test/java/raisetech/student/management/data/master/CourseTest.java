package raisetech.student.management.data.master;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class CourseTest {

  private static Validator validator;

  @BeforeAll
  static void setUpValidator() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @ParameterizedTest(name = "[{index}] フィールド: {0} がnullのときバリデーション違反が起きること")
  @ValueSource(strings = {"courseCode","courseName"})
  void 各フィールドがnullのときバリデーション違反が起きること(String fieldName) {
    // Arrange
    Course course = new Course(
        fieldName.equals("courseCode")? null : "JA",
        fieldName.equals("courseName")? null : "Javaコース"
    );

    Set<ConstraintViolation<Course>> violations = validator.validate(course);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName))).isTrue();
  }

  @ParameterizedTest(name = "[{index}] フィールド: {0} が空文字のときバリデーション違反が起きること")
  @ValueSource(strings = {"courseCode","courseName"})
  void 各フィールドが空文字のときバリデーション違反が起きること(String fieldName) {
    // Arrange
    Course course = new Course(
        fieldName.equals("courseCode")? "" : "JA",
        fieldName.equals("courseName")? "" : "Javaコース"
    );

    Set<ConstraintViolation<Course>> violations = validator.validate(course);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName))).isTrue();
  }

  @ParameterizedTest(name = "[{index}] フィールド：courseCodeが{0}文字の時violation={1}")
  @CsvSource({
      "1,true",
      "2,false",
      "5,false",
      "6,true"
  })
  void コースコード文字数の境界地テスト(int length,boolean expectViolation)
      throws Exception {
    // Arrange
    String testValue = "A".repeat(length);
    Course course = new Course(
        testValue,"Javaコース"
    );

    // Act
    Set<ConstraintViolation<Course>> violations = validator.validate(course);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("courseCode"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @ParameterizedTest(name = "[{index}] フィールド：courseNameが{0}文字の時violation={1}")
  @CsvSource({
      "19,false",
      "20,false",
      "21,true"
  })
  void コース名文字数の境界値テスト(int length,boolean expectViolation)
      throws Exception {
    // Arrange
    String testValue = "A".repeat(length);
    Course course = new Course(
        "JA",testValue
    );

    // Act
    Set<ConstraintViolation<Course>> violations = validator.validate(course);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("courseName"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @ParameterizedTest(name = "[{index}] 登録時_電話番号の形式が {0} のときバリデーション違反が起きる")
  @ValueSource(strings = {
      "123",     // 数字
      "あいう",    // 日本語
      "#&-",  // 記号
      "ui",  // 英字小文字
      "0z#あ"   // 混合
  })
  void コースコードの形式が不正のときバリデーション違反が起きる(String invalidCourseCode) {
    // Arrange
    Course course = new Course(
        invalidCourseCode,"Javaコース"
    );

    // Act
    Set<ConstraintViolation<Course>> violations = validator.validate(course);

    // Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("courseCode"))).isTrue();
  }

  @Test
  void 各フィールドが妥当な値を持つときバリデーション違反が起きない(){
    // Arrange
    Course course = new Course("JA","Javaコース");

    // Act
    Set<ConstraintViolation<Course>> violations = validator.validate(course);

    // Assert
    assertThat(violations).isEmpty();

  }

}