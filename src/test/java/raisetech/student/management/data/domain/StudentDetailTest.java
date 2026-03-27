package raisetech.student.management.data.domain;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.repository.CourseRepository;
import raisetech.student.management.testutil.TestDataFactory;
import raisetech.student.management.testutil.ValidatorTestFactory;
import raisetech.student.management.validation.CreateGroup;

@ExtendWith(MockitoExtension.class) // Mockitoを使用
class StudentDetailTest {

  private Validator validator;

  @Mock
  private CourseRepository courseRepository;

  @BeforeEach
  void setUp() {
    validator = ValidatorTestFactory.createValidator(courseRepository);
  }

  @Test
  void 登録時_受講生がnullのときバリデーション違反が起きる() {
    // Arrange
    Integer studentId = 1;
    Integer scId = 1;
    StudentDetail invalidStudentDetail = new StudentDetail(
        null, List.of(TestDataFactory.makeCompletedStudentCourse(studentId, scId))
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
    Integer scId = null;
    StudentDetail validStudentDetail = new StudentDetail(
        TestDataFactory.makeCompletedStudent(studentId),
        List.of(TestDataFactory.makeCompletedStudentCourse(studentId,scId))
    );
    Mockito.when(courseRepository.existsByCourseCode(Mockito.anyString())).thenReturn(true);
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(
        validStudentDetail, CreateGroup.class);

    // Act & Assert
    assertThat(violations).isEmpty();
  }

}