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
  void 受講生リクエストボディ検証_すべてのフィールドが登録時に期待する値をもつとき入力チェックにかからないこと() {
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
  void 受講生リクエストボディ検証_すべてのフィールドが更新時に期待する値をもつとき入力チェックにかからないこと() {
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

  @Test
  void 受講生詳細リクエストボディ検証_文字数が超過するとき入力チェックにかかること()
      throws Exception {
    // Arrange: かなが51文字
    StringBuilder tooLongKanaName = new StringBuilder();
    tooLongKanaName.append("テスト");
    tooLongKanaName.setLength(51);

    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        new Student(
            studentId,
            "山田太郎",
            tooLongKanaName.toString(),
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
        .allMatch(v -> v.getPropertyPath().toString().equals("student.kanaName"))).isTrue();
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

  @Test
  void 受講生詳細リクエストボディ検証_年齢が15未満のとき入力チェックにかかること()
      throws Exception {
    // Arrange: ageが15未満
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
            14,
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
  void 受講生詳細リクエストボディ検証_年齢が80より大きいとき入力チェックにかかること()
      throws Exception {
    // Arrange: ageが81
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
            81,
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

  @Test
  void 受講生詳細リクエストボディ検証_受講生コースの必須情報がnullのとき入力チェックにかかること()
      throws Exception {
    // Arrange: コース名がnull
    int studentId = 0;
    int courseId = 0;
    LocalDate now = LocalDate.now();
    StudentDetail invalidStudentDetail = new StudentDetail(
        makeCompletedStudent(studentId),
        List.of(new StudentCourse(
            courseId,
            null,// コース名がnull
            studentId,
            null,
            null)
        ));
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString()
            .matches("studentCourseList\\[\\d+\\]\\.courseName"))).isTrue();
  }

  @Test
  void 受講生詳細リクエストボディ検証_受講生コースのコース名が不正のとき入力チェックにかかること()
      throws Exception {
    // Arrange: コース名が不正
    int studentId = 0;
    int courseId = 0;
    LocalDate now = LocalDate.now();
    StudentDetail invalidStudentDetail = new StudentDetail(
        makeCompletedStudent(studentId),
        List.of(new StudentCourse(
            courseId,
            "Pythonコース",// 想定されないコース名
            studentId,
            null,
            null)
        ));
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString()
            .matches("studentCourseList\\[\\d+\\]\\.courseName"))).isTrue();
  }
  //TODO:どのくらいの粒度で網羅したらいいのか？

  @Test
  void 受講生詳細リクエストボディ検証_受講生がnullのとき入力チェックにかかること() {
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
  void 受講生詳細リクエストボディ検証_受講生コースが空のとき入力チェックにかかること() {
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

  @Test
  void 受講生詳細リクエストボディ検証_更新時_受講生IDと受講生コースIDが1未満のとき入力チェックにかかること() {
    // Arrange
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = makeCompletedStudentDetail(studentId, courseId);
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail,
        OnUpdate.class);

    // Act & Assert
    assertThat(violations.size()).isEqualTo(2);
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