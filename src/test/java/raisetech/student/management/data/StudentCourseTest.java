package raisetech.student.management.data;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.repository.CourseRepository;
import raisetech.student.management.testutil.TestDataFactory;
import raisetech.student.management.testutil.ValidatorTestFactory;
import raisetech.student.management.validation.CreateGroup;
import raisetech.student.management.validation.UpdateGroup;

@ExtendWith(MockitoExtension.class)
class StudentCourseTest {

  private Validator validator;

  @Mock
  private CourseRepository courseRepository;

  @BeforeEach
  void setUp() {
    validator = ValidatorTestFactory.createValidator(courseRepository);
  }

  private void stubCourseCodeExistsIfNeeded(String courseCode) {
    if (courseCode != null) {
      Mockito.when(courseRepository.existsByCourseCode(courseCode)).thenReturn(true);
    }
  }

  @ParameterizedTest(name = "[{index}] 登録時_フィールド: {0} がnullのとき violation={1}")
  @CsvSource({// trueはNotNull, falseはnull許容
      "studentCourseId,false",
      "studentId,false",
      "courseCode,true",
      "statusId,false",
      "courseApplyAt,false",
      "courseStartAt,false",
      "courseEndAt,false"
  })
  void 登録時_各フィールドのnull許容性のテスト(String fieldName,
      boolean expectViolation) {
    // Arrange
    LocalDate now = LocalDate.now();
    String courseCode = fieldName.equals("courseCode") ? null : "JA";

    StudentCourse studentCourse = new StudentCourse(
        fieldName.equals("studentCourseId") ? null : 1,
        fieldName.equals("studentId") ? null : 1,
        courseCode,
        fieldName.equals("statusId") ? null : 1,
        fieldName.equals("courseApplyAt") ? null : now.minusDays(1),
        fieldName.equals("courseStartAt") ? null : now,
        fieldName.equals("courseEndAt") ? null : now.plusMonths(6)
    );

    stubCourseCodeExistsIfNeeded(courseCode);
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

  @Test
  void 登録時_courseCodeがコースマスタに登録のない値を受け取ったときバリデーション違反が起きる(){
    // Arrange
    Integer studentId = null;
    Integer scId = null;
    StudentCourse invalidStudentCourse = new StudentCourse(
        scId, studentId, "存在しないコース",null,null, null, null
    );
    Mockito.when(courseRepository.existsByCourseCode("存在しないコース")).thenReturn(false);
    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(invalidStudentCourse,
        CreateGroup.class);

    // Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("courseCode"))).isTrue();

  }

  @Test
  void 登録時_StudentCourseの各のフィールドが妥当な値を持つときバリデーション違反が起きない(){
    Integer studentId = null;
    Integer scId = null;
    StudentCourse validStudentCourse = TestDataFactory.makeCompletedStudentCourse(studentId,scId);

    Mockito.when(courseRepository.existsByCourseCode(Mockito.anyString())).thenReturn(true);
    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(validStudentCourse,
        CreateGroup.class);

    assertThat(violations).isEmpty();
  }

  @ParameterizedTest(name = "[{index}] 更新時_フィールド: {0} がnullのとき violation={1}")
  @CsvSource({// trueはNotNull, falseはnull許容
      "studentCourseId,true",
      "studentId,false",
      "courseCode,true",
      "statusId,false",
      "courseApplyAt,false",
      "courseStartAt,false",
      "courseEndAt,false"
  })
  void 更新時_各フィールドのnull許容性のテスト(String fieldName,
      boolean expectViolation) {
    // Arrange
    LocalDate now = LocalDate.now();
    String courseCode = fieldName.equals("courseCode") ? null : "JA";

    StudentCourse studentCourse = new StudentCourse(
        fieldName.equals("studentCourseId") ? null : 1,
        fieldName.equals("studentId") ? null : 1,
        courseCode,
        fieldName.equals("statusId") ? null : 1,
        fieldName.equals("courseApplyAt") ? null : now.minusDays(1),
        fieldName.equals("courseStartAt") ? null : now,
        fieldName.equals("courseEndAt") ? null : now.plusMonths(6)
    );

    stubCourseCodeExistsIfNeeded(courseCode);
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
  void 更新時_studentCourseIdが1未満の数値の時バリデーション違反が起きる() {
    // Arrange
    Integer studentId = 1;
    Integer scId = -3;
    StudentCourse invalidStudentCourse = TestDataFactory.makeCompletedStudentCourse(studentId,scId);

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(invalidStudentCourse,
        UpdateGroup.class);

    // Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("studentCourseId"))).isTrue();
  }

  @Test
  void 更新時_courseCodeがコースマスタに登録のない値を受け取ったときバリデーション違反が起きる(){
    // Arrange
    Integer studentId = 1;
    Integer scId = 1;
    StudentCourse invalidStudentCourse = new StudentCourse(
        scId, studentId, "存在しないコース",null,null, null, null
    );
    Mockito.when(courseRepository.existsByCourseCode("存在しないコース")).thenReturn(false);
    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(invalidStudentCourse,
        UpdateGroup.class);

    // Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("courseCode"))).isTrue();

  }

  @Test
  void 更新時_StudentCourseの各のフィールドが妥当な値を持つときバリデーション違反が起きない(){
    Integer studentId = null;
    Integer scId = 1;
    StudentCourse validStudentCourse = TestDataFactory.makeCompletedStudentCourse(studentId,scId);

    Mockito.when(courseRepository.existsByCourseCode(Mockito.anyString())).thenReturn(true);
    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(validStudentCourse,
        UpdateGroup.class);

    assertThat(violations).isEmpty();
  }

}