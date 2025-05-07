package raisetech.student.management.data.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudent;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudentCourse;
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
import raisetech.student.management.data.Id;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.validation.OnRegister;
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
    Id studentId = null;
    Id courseId = null;
    StudentDetail validStudentDetail = new StudentDetail(
        makeCompletedStudent(studentId), List.of(
        makeEnoughStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(validStudentDetail,
        OnRegister.class);

    // Act & Assert
    assertThat(violations).isEmpty();
  }

  @Test
  void すべてのフィールドが更新時に期待する値をもつとき入力チェックにかからないこと() {
    // Arrange:
    Id studentId = new Id(1);
    Id courseId = new Id(1);
    StudentDetail validStudentDetail = new StudentDetail(
        makeCompletedStudent(studentId), List.of(makeCompletedStudentCourse(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(validStudentDetail,
        OnUpdate.class);

    // Act & Assert
    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @CsvSource({// trueはNotNull, falseはnull許容
      "studentId,false",
      "fullname,true",
      "kanaName,true",
      "nickname,false",
      "email,true",
      "area,false",
      "telephone,false",
      "age,false",
      "sex,false",
      "remark,false",
      "courseId,false",
      "courseName,true",
      "courseStartAt,false",
      "courseEndAt,false"
  })
  void 登録時のStudentDetailの各フィールドのnull許容性を検証する(String fieldName,
      boolean expectViolation) {
    // Arrange
    Student student = new Student(
        fieldName.equals("studentId") ? null : new Id(1),
        fieldName.equals("fullname") ? null : "山田太郎",
        fieldName.equals("kanaName") ? null : "やまだたろう",
        fieldName.equals("nickname") ? null : "タロー",
        fieldName.equals("email") ? null : "yamada@example.com",
        fieldName.equals("area") ? null : "東京都",
        fieldName.equals("telephone") ? null : "090-0000-0000",
        fieldName.equals("age") ? null : 20,
        fieldName.equals("sex") ? null : "男",
        fieldName.equals("remark") ? null : "特になし",
        false
    );
    Id studentId = student.getStudentId();
    LocalDate now = LocalDate.now();
    StudentCourse studentCourse = new StudentCourse(
        fieldName.equals("courseId") ? null : new Id(1),
        fieldName.equals("courseName") ? null : "Javaコース",
        studentId,
        fieldName.equals("courseStartAt") ? null : now,
        fieldName.equals("courseEndAt") ? null : now.plusMonths(6)
    );

    StudentDetail studentDetail = new StudentDetail(student, List.of(studentCourse));

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(studentDetail,
        OnRegister.class);

    // Act & Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().contains(fieldName))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @ParameterizedTest
  @CsvSource({// trueはNotNull, falseはnull許容
      "studentId,true",
      "fullname,true",
      "kanaName,true",
      "nickname,false",
      "email,true",
      "area,false",
      "telephone,false",
      "age,false",
      "sex,false",
      "remark,false",
      "courseId,true",
      "courseName,true",
      "courseStartAt,false",
      "courseEndAt,true"
  })
  void 更新時のStudentDetailの各フィールドのnull許容性を検証する(String fieldName,
      boolean expectViolation) {
    // Arrange
    Student student = new Student(
        fieldName.equals("studentId") ? null : new Id(1),
        fieldName.equals("fullname") ? null : "山田太郎",
        fieldName.equals("kanaName") ? null : "やまだたろう",
        fieldName.equals("nickname") ? null : "タロー",
        fieldName.equals("email") ? null : "yamada@example.com",
        fieldName.equals("area") ? null : "東京都",
        fieldName.equals("telephone") ? null : "090-0000-0000",
        fieldName.equals("age") ? null : 20,
        fieldName.equals("sex") ? null : "男",
        fieldName.equals("remark") ? null : "特になし",
        false
    );
    Id studentId = student.getStudentId();
    LocalDate now = LocalDate.now();
    StudentCourse studentCourse = new StudentCourse(
        fieldName.equals("courseId") ? null : new Id(1),
        fieldName.equals("courseName") ? null : "Javaコース",
        studentId,
        fieldName.equals("courseStartAt") ? null : now,
        fieldName.equals("courseEndAt") ? null : now.plusMonths(6)
    );

    StudentDetail studentDetail = new StudentDetail(student, List.of(studentCourse));

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(studentDetail,
        OnUpdate.class);

    // Act & Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().contains(fieldName))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }


  @ParameterizedTest
  @ValueSource(strings = {"OnRegister", "OnUpdate"})
  void 受講生がnullのとき入力チェックにかかること(String groupName) {
    // Arrange
    Id studentId = new Id(1);
    Id courseId = new Id(1);
    Class<?> group = groupName.equals("OnRegister") ? OnRegister.class : OnUpdate.class;
    StudentDetail invalidStudentDetail = new StudentDetail(
        null, List.of(makeCompletedStudentCourse(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail,
        group);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("student"))).isTrue();
    // studentCourseでviolationが発生しても、studentでのviolationが確認できれば良いのでanyMatch
  }

  @ParameterizedTest
  @ValueSource(strings = {"OnRegister", "OnUpdate"})
  void 受講生コースが空のとき入力チェックにかかること(String groupName) {
    // Arrange
    Id studentId = new Id(1);
    Class<?> group = groupName.equals("OnRegister") ? OnRegister.class : OnUpdate.class;
    StudentDetail invalidStudentDetail = new StudentDetail(
        makeCompletedStudent(studentId), new ArrayList<StudentCourse>()
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail,
        group);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("studentCourseList"))).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"fullname", "kanaName", "nickname", "email", "area", "remark"})
  void 登録時_文字列が51文字以上のとき入力チェックにかかること(String fieldName) throws Exception {
    // Arrange
    Id studentId = null;
    Id courseId = null;
    String over50char = "あ".repeat(51); // 51文字の文字列
    String over200char = "い".repeat(201); //201文字の文字列(備考用)
    String validButLongEmail = "a".repeat(41) + "@email.com"; // 全体で51文字（形式は正しい
    Student student = new Student(
        studentId,
        fieldName.equals("fullname") ? over50char : "山田太郎",
        fieldName.equals("kanaName") ? over50char : "やまだたろう",
        fieldName.equals("nickname") ? over50char : "タロー",
        fieldName.equals("email") ? validButLongEmail : "test@example.com",
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
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalid,
        OnRegister.class);
    violations.forEach(v -> System.out.println(v.getPropertyPath() + " : " + v.getMessage()));

    // Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student." + fieldName))).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"fullname", "kanaName", "nickname", "email", "area", "remark"})
  void 更新時_文字列が51文字以上のとき入力チェックにかかること(String fieldName) throws Exception {
    // Arrange
    Id studentId = new Id(1);
    Id courseId = new Id(1);
    String over50char = "あ".repeat(51); // 51文字の文字列
    String over200char = "い".repeat(201); //201文字の文字列(備考用)
    String validButLongEmail = "a".repeat(41) + "@email.com"; // 全体で51文字（形式は正しい）
    Student student = new Student(
        studentId,
        fieldName.equals("fullname") ? over50char : "山田太郎",
        fieldName.equals("kanaName") ? over50char : "やまだたろう",
        fieldName.equals("nickname") ? over50char : "タロー",
        fieldName.equals("email") ? validButLongEmail : "test@example.com",
        fieldName.equals("area") ? over50char : "東京都",
        "090-0000-0000",
        20,
        "男",
        fieldName.equals("remark") ? over200char : "特になし",
        false
    );
    StudentDetail invalid = new StudentDetail(student,
        List.of(makeCompletedStudentCourse(studentId, courseId)));

    // Act
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalid,
        OnUpdate.class);
    violations.forEach(v -> System.out.println(v.getPropertyPath() + " : " + v.getMessage()));

    // Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student." + fieldName))).isTrue();

  }


  @ParameterizedTest
  @ValueSource(strings = {
      "plainaddress",        // @なし
      "@missingusername.com",// ユーザー名なし
      "username@.com",       // ドメイン名先頭にドット
      "user@com.",           // ドメイン末尾にドット
      "user@-com.com",       // 不正なドメイン記号
      "user@com..com"        // ドット連続
  })
  void 入力チェック_emailの不正な形式はすべて入力チェックにかかるべき(String invalidEmail) {
    // Arrange
    Id studentId = null;
    Id courseId = null;
    Student student = new Student(
        studentId, "山田太郎", "やまだたろう", "タロー", invalidEmail,
        "東京都", "090-0000-0000", 20, "男", "", false
    );
    StudentDetail detail = new StudentDetail(student,
        List.of(makeEnoughStudentCourseOnRegister(studentId, courseId)));

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(detail);

    // Act & Assert
    assertThat(violations)
        .anyMatch(v -> v.getPropertyPath().toString().equals("student.email"));
  }


  @ParameterizedTest
  @ValueSource(strings = {
      "09000000000",     // ハイフンなし
      "090-0000-000",    // 下4桁が3桁
      "0900-000-00000",  // 中間と下桁の桁数異常
      "abcd-efgh-ijkl",  // 数字以外
      "-03-1234-5678",   // 先頭にハイフン
      "03--1234-5678",   // ハイフン重複
      "03-1234--5678"    // ハイフン重複（別位置）
  })
  void 電話番号の形式が不正のとき入力チェックにかかること(String invalidPhoneNumber) {
    // Arrange
    Id studentId = null;
    Id courseId = null;
    Student invalidStudent = new Student(
        studentId,
        "山田太郎",
        "やまだたろう",
        "タロー",
        "test@example.com",
        "東京都",
        invalidPhoneNumber,
        20,
        "男",
        "備考なし",
        false
    );

    StudentDetail detail = new StudentDetail(invalidStudent,
        List.of(makeEnoughStudentCourseOnRegister(studentId, courseId)));

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(detail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("student.telephone"))).isTrue();
  }


  @ParameterizedTest
  @ValueSource(ints = {14, 81})
  void 年齢が想定範囲外のとき入力チェックにかかること(int age)
      throws Exception {
    // Arrange
    Id studentId = null;
    Id courseId = null;
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

  @ParameterizedTest
  @ValueSource(strings = {"男性", "female", "man", "それ以外"})
//TODO:性別の想定された入力は通って、それ以外が通らないこと
  void 性別がパターン外のときエラーになる(String invalidSex) {
    // Arrange
    Id studentId = null;
    Id courseId = null;
    Student invalidStudent = new Student(
        studentId, "山田太郎", "やまだたろう", "タロー", "test@example.com",
        "東京都", "090-0000-0000", 20, invalidSex, "", false
    );
    StudentDetail detail = new StudentDetail(invalidStudent,
        List.of(makeEnoughStudentCourseOnRegister(studentId, courseId)));

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(detail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("student.sex"))).isTrue();
  }


  @ParameterizedTest
  @ValueSource(strings = {"", "Pythonコース"})
//TODO:コース名の想定された入力は通って、それ以外が通らないこと
  void 受講生コースのコース名がnullや不正のとき入力チェックにかかること(String invalidCourseName) {
    // Arrange
    Id studentId = null;
    Id courseId = null;

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

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString()
            .matches("studentCourseList\\[\\d+\\]\\.courseName"))).isTrue();
  }

}