package raisetech.student.management.data.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudent;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudentCourse;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudentDetail;
import static raisetech.student.management.testutil.TestDataFactory.makeEnoughStudentCourseOnRegister;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.validation.OnUpdate;

class StudentDetailTest {

  private static Validator validator;

  @BeforeAll
  static void setUpValidator() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void すべてのフィールドが登録時に期待する値をもつとき入力チェックにかからないこと() {
    // Arrange:
    int studentId = 0;
    int courseId = 0;
    StudentDetail validStudentDetail = new StudentDetail(
        makeCompletedStudent(studentId), List.of(
        makeEnoughStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(validStudentDetail);

    // Act & Assert
    assertThat(violations).isEmpty();
  }

  @Test
  void すべてのフィールドが更新時に期待する値をもつとき入力チェックにかからないこと() {
    // Arrange:
    int studentId = 1;
    int courseId = 1;
    StudentDetail validStudentDetail = new StudentDetail(
        makeCompletedStudent(studentId), List.of(makeCompletedStudentCourse(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(validStudentDetail,
        OnUpdate.class);

    // Act & Assert
    assertThat(violations).isEmpty();
  }

  @Test
  void 受講生詳細リクエストボディ検証_受講生の必須項目がnullのとき入力チェックにかかること()
      throws Exception {
    // Arrange: fullnameがnull
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        new Student(
            studentId,
            null, // fullnameがnull
            "やまだたろう",
            "タロー",
            "taro@email.com",
            "東京都練馬区",
            "090-0000-0000",
            20,
            "男",
            "特になし",
            false
        ),
        List.of(makeEnoughStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student.fullname"))).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"fullname", "kanaName", "nickname", "email", "area", "remark"})
  void 文字列が51文字以上のとき入力チェックにかかること(String fieldName) throws Exception {
    // Arrange
    int studentId = 0;
    int courseId = 0;
    String over50char = "あ".repeat(51); // 51文字の文字列
    String over200char = "い".repeat(201); //201文字の文字列(備考用)
    Student student = new Student(
        studentId,
        fieldName.equals("fullname") ? over50char : "山田太郎",
        fieldName.equals("kanaName") ? over50char : "やまだたろう",
        fieldName.equals("nickname") ? over50char : "タロー",
        fieldName.equals("email") ? over50char : "test@example.com",
        fieldName.equals("area") ? over50char : "東京都",
        "090-0000-0000",
        20,
        "男",
        fieldName.equals("remark") ? over200char : "特になし",
        false
    );
    StudentDetail invalid = new StudentDetail(student,
        List.of(makeEnoughStudentCourseOnRegister(studentId, courseId)));

    // Act
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalid);

    // Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student." + fieldName))).isTrue();
  }


  @Test
  void 受講生詳細リクエストボディ検証_emailの形式が不正のとき入力チェックにかかること()
      throws Exception {
    // Arrange: emailの形式が不正
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        new Student(
            studentId,
            "山田太郎",
            "やまだたろう",
            "タロー",
            "taroemailcom",// @がなく形式が不正
            "東京都練馬区",
            "090-0000-0000",
            20,
            "男",
            "特になし",
            false
        ),
        List.of(makeEnoughStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student.email"))).isTrue();
  }

  @Test
  void 受講生詳細リクエストボディ検証_電話番号の形式が不正のとき入力チェックにかかること()
      throws Exception {
    // Arrange: 電話番号の形式が不正
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        new Student(
            studentId,
            "山田太郎",
            "やまだたろう",
            "タロー",
            "taro@email.com",
            "東京都練馬区",
            "09000000000",// -がなく形式が不正
            20,
            "男",
            "特になし",
            false
        ),
        List.of(makeEnoughStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student.telephone"))).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {14, 81})
  void 年齢が想定範囲外のとき入力チェックにかかること(int age)
      throws Exception {
    // Arrange
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        new Student(
            studentId,
            "山田太郎",
            "やまだたろう",
            "タロー",
            "taro@email.com",
            "東京都練馬区",
            "090-0000-0000",
            age,
            "男",
            "特になし",
            false
        ),
        List.of(makeEnoughStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student.age"))).isTrue();
  }

  @Test
  void 受講生詳細リクエストボディ検証_性別がパターンにマッチしないとき入力チェックにかかること()
      throws Exception {
    // Arrange: 性別が”男性”
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        new Student(
            studentId,
            "山田太郎",
            "やまだたろう",
            "タロー",
            "taro@email.com",
            "東京都練馬区",
            "090-0000-0000",
            20,
            "男性",
            "特になし",
            false
        ),
        List.of(makeEnoughStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student.sex"))).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "Pythonコース"})
  void 受講生コースのコース名がnullや不正のとき入力チェックにかかること(String invalidCourseName) {
    int studentId = 0;
    int courseId = 0;

    String courseName = invalidCourseName.isEmpty() ? null : invalidCourseName;//空文字の時はnullに置き換える

    StudentDetail invalidStudentDetail = new StudentDetail(
        makeCompletedStudent(studentId),
        List.of(new StudentCourse(
            courseId,
            courseName,
            studentId,
            null,
            null
        ))
    );

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString()
            .matches("studentCourseList\\[\\d+\\]\\.courseName"))).isTrue();
  }


  @Test
  void 受講生がnullのとき入力チェックにかかること() {
    // Arrange
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        null, List.of(makeEnoughStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student"))).isTrue();
  }

  @Test
  void 受講生コースが空のとき入力チェックにかかること() {
    // Arrange
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        makeCompletedStudent(studentId), new ArrayList<StudentCourse>()
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("studentCourseList"))).isTrue();
  }

  @ParameterizedTest
  @CsvSource({
      "0,1",
      "1,0",
      "0,0"
  })
  void 更新時_studentIdまたはcourseIdあるいは両方が1未満のとき入力チェックにかかること(
      int studentId, int courseId) {

    StudentDetail invalidStudentDetail = makeCompletedStudentDetail(studentId, courseId);

    Set<ConstraintViolation<StudentDetail>> violations =
        validator.validate(invalidStudentDetail, OnUpdate.class);

    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().contains("Id"))).isTrue();
  }


  @Test
  void 受講生詳細リクエストボディ検証_更新時_受講終了予定日がnullのとき入力チェックにかかること() {
    // Arrange
    int studentId = 1;
    int courseId = 1;
    StudentDetail invalidStudentDetail = new StudentDetail(
        makeCompletedStudent(studentId),
        List.of(new StudentCourse(
            courseId,
            "Javaコース",
            studentId,
            LocalDate.now(),
            null // 受講終了予定日
        )));
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail,
        OnUpdate.class);
    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().
            matches("studentCourseList\\[\\d+\\]\\.courseEndAt"))).isTrue();
  }

}