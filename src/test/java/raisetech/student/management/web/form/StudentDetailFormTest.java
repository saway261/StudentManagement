package raisetech.student.management.web.form;

import static org.assertj.core.api.Assertions.assertThat;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudentCourseForm;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudentForm;
import static raisetech.student.management.testutil.TestDataFactory.makeEnoughStudentCourseFormOnRegister;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;

class StudentDetailFormTest {

  private static Validator validator;

  @BeforeAll
  static void setUpValidator() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @ParameterizedTest
  @ValueSource(strings = {"OnRegister", "OnUpdate"})
  @DisplayName("登録時および更新時_受講生がnullのとき入力チェックにかかること")
  void test_001(String groupName) {
    // Arrange
    Integer courseId = 1;
    Class<?> group = groupName.equals("OnRegister") ? OnRegister.class : OnUpdate.class;
    StudentDetailForm invalidStudentDetailForm = new StudentDetailForm(
        null, List.of(makeCompletedStudentCourseForm(courseId))
    );
    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
        invalidStudentDetailForm,
        group);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("student"))).isTrue();
    // studentCourseでviolationが発生しても、studentでのviolationが確認できれば良いのでanyMatch
  }

  @ParameterizedTest
  @ValueSource(strings = {"OnRegister", "OnUpdate"})
  @DisplayName("登録時および更新時_受講生コースが空のとき入力チェックにかかること")
  void test_002(String groupName) {
    // Arrange
    Integer studentId = 1;
    Class<?> group = groupName.equals("OnRegister") ? OnRegister.class : OnUpdate.class;
    StudentDetailForm invalidStudentDetail = new StudentDetailForm(
        makeCompletedStudentForm(studentId), new ArrayList<StudentCourseForm>()
    );
    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
        invalidStudentDetail,
        group);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("studentCourseList"))).isTrue();
  }

  @ParameterizedTest
  @CsvSource({// trueはNotNull, falseはnull許容
      "studentId,false", //TC-3
      "fullname,true", //TC-4
      "kanaName,true", //TC-5
      "nickname,false", //TC-6
      "email,true", //TC-7
      "area,false", //TC-8
      "telephone,false", //TC-9
      "age,false", //TC-10
      "sex,false", //TC-11
      "remark,false", //TC-12
      "courseId,false", //TC-13
      "courseName,true", //TC-14
      "courseEndAt,false" //TC-15
  })
  @DisplayName("登録時_StudentDetailの各フィールドのnull許容性を検証する")
  void test_003(String fieldName, boolean expectViolation) {
    // Arrange
    StudentForm student = new StudentForm(
        fieldName.equals("studentId") ? null : 1,
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
    LocalDate now = LocalDate.now();
    StudentCourseForm studentCourse = new StudentCourseForm(
        fieldName.equals("courseId") ? null : 1,
        fieldName.equals("courseName") ? null : "Javaコース",
        fieldName.equals("courseEndAt") ? null : now.plusMonths(6)
    );

    StudentDetailForm studentDetail = new StudentDetailForm(student, List.of(studentCourse));

    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
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
  @ValueSource(strings = {"fullname", "kanaName", "nickname", "email", "area", "remark"})
  @DisplayName("登録時_文字列超過のとき入力チェックにかかること")
  void test_004(String fieldName) throws Exception {
    // Arrange
    Integer studentId = null;
    Integer courseId = null;
    String over50char = "あ".repeat(51); // 51文字の文字列
    String over200char = "い".repeat(201); //201文字の文字列(備考用)
    String validButLongEmail = "a".repeat(41) + "@email.com"; // 全体で51文字（形式は正しい
    StudentForm student = new StudentForm(
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
    StudentDetailForm invalid = new StudentDetailForm(student,
        List.of(makeEnoughStudentCourseFormOnRegister()));

    // Act
    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(invalid,
        OnRegister.class);

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
  @DisplayName("登録時_emailの形式が不正な場合は入力チェックにかかること")
  void test_005(String invalidEmail) {
    // Arrange
    Integer studentId = null;
    Integer courseId = null;
    StudentForm student = new StudentForm(
        studentId, "山田太郎", "やまだたろう", "タロー", invalidEmail,
        "東京都", "090-0000-0000", 20, "男", "", false
    );
    StudentDetailForm studentDetail = new StudentDetailForm(student,
        List.of(makeEnoughStudentCourseFormOnRegister()));

    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
        OnRegister.class);

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
  @DisplayName("登録時_電話番号の形式が不正のとき入力チェックにかかること")
  void test_006(String invalidPhoneNumber) {
    // Arrange
    Integer studentId = null;
    Integer courseId = null;
    StudentForm invalidStudent = new StudentForm(
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

    StudentDetailForm studentDetail = new StudentDetailForm(invalidStudent,
        List.of(makeEnoughStudentCourseFormOnRegister()));

    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
        OnRegister.class);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("student.telephone"))).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {14, 81})
  @DisplayName("登録時_年齢が15未満または80より大きいとき入力チェックにかかること")
  void test_007(int age)
      throws Exception {
    // Arrange
    Integer studentId = null;
    Integer courseId = null;
    StudentDetailForm invalidStudentDetail = new StudentDetailForm(
        new StudentForm(
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
        List.of(makeEnoughStudentCourseFormOnRegister())
    );
    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
        invalidStudentDetail,
        OnRegister.class);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student.age"))).isTrue();
  }

  @ParameterizedTest
  @CsvSource({
      "男,false",
      "女,false",
      "その他,false",
      "男性,true",
      "female,true",
      "man,true",
      "それ以外,true"
  })
  @DisplayName("登録時_性別がパターン外のとき入力チェックにかかること")
  void test_008(String sex, boolean expectViolation) {
    // Arrange
    Integer studentId = null;
    Integer courseId = null;
    StudentForm invalidStudent = new StudentForm(
        studentId, "山田太郎", "やまだたろう", "タロー", "test@example.com",
        "東京都", "090-0000-0000", 20, sex, "", false
    );
    StudentDetailForm StudentDetail = new StudentDetailForm(invalidStudent,
        List.of(makeEnoughStudentCourseFormOnRegister()));

    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(StudentDetail,
        OnRegister.class);

    // Act & Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("student.sex"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @ParameterizedTest
  @CsvSource({
      "Javaコース,false",
      "AWSコース,false",
      "デザインコース,false",
      "Webマーケティングコース,false",
      "フロントエンドコース,false",
      "Java,true", //「コース」なし
      "Pythonコース,true", //存在しないコース
  })
  @DisplayName("登録時_コース名が正しい時は通過し、不正のときは入力チェックにかかること")
  void test_009(String courseName, boolean expectViolation) {
    // Arrange
    Integer studentId = null;
    Integer courseId = null;

    StudentDetailForm invalidStudentDetail = new StudentDetailForm(
        makeCompletedStudentForm(studentId),
        List.of(new StudentCourseForm(
            courseId,
            courseName,
            null
        ))
    );

    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
        invalidStudentDetail,
        OnRegister.class);

    // Act & Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .allMatch(v -> v.getPropertyPath().toString()
              .matches("studentCourseList\\[\\d+\\]\\.courseName"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @Test
  @DisplayName("登録時_すべてのフィールドが登録時に期待する値をもつとき入力チェックにかからないこと")
  void test_010() {
    // Arrange:
    Integer studentId = null;
    Integer courseId = null;
    StudentDetailForm validStudentDetail = new StudentDetailForm(
        makeCompletedStudentForm(studentId),
        List.of(makeEnoughStudentCourseFormOnRegister())
    );
    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(validStudentDetail,
        OnRegister.class);

    // Act & Assert
    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @CsvSource({// trueはNotNull, falseはnull許容
      "studentId,true", //TC-31
      "fullname,true", //TC-32
      "kanaName,true", //TC-33
      "nickname,false", //TC-34
      "email,true", //TC-35
      "area,false", //TC-36
      "telephone,false", //TC-37
      "age,false", //TC-38
      "sex,false", //TC-39
      "remark,false", //TC-40
      "courseId,true", //TC-41
      "courseName,true", //TC-42
      "courseEndAt,true" //TC-43
  })
  @DisplayName("更新時_StudentDetailの各フィールドのnull許容性を検証する")
  void test_011(String fieldName, boolean expectViolation) {
    // Arrange
    StudentForm student = new StudentForm(
        fieldName.equals("studentId") ? null : 1,
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
    LocalDate now = LocalDate.now();
    StudentCourseForm studentCourse = new StudentCourseForm(
        fieldName.equals("courseId") ? null : 1,
        fieldName.equals("courseName") ? null : "Javaコース",
        fieldName.equals("courseEndAt") ? null : now.plusMonths(6)
    );

    StudentDetailForm studentDetail = new StudentDetailForm(student, List.of(studentCourse));

    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
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

  @Test
  @DisplayName("更新時_studentIdが1未満の数値の時入力チェックにかかること")
  void test_012() {
    Integer studentId = -3;
    Integer courseId = 1;
    StudentDetailForm studentDetail = new StudentDetailForm(
        makeCompletedStudentForm(studentId),
        List.of(makeCompletedStudentCourseForm(courseId)));

    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
        OnUpdate.class);
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().contains("studentId"))).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"fullname", "kanaName", "nickname", "email", "area", "remark"})
  @DisplayName("更新時_文字列超過のとき入力チェックにかかること")
  void test_013(String fieldName) throws Exception {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;
    String over50char = "あ".repeat(51); // 51文字の文字列
    String over200char = "い".repeat(201); //201文字の文字列(備考用)
    String validButLongEmail = "a".repeat(41) + "@email.com"; // 全体で51文字（形式は正しい）
    StudentForm student = new StudentForm(
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
    StudentDetailForm invalid = new StudentDetailForm(student,
        List.of(makeCompletedStudentCourseForm(courseId)));

    // Act
    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(invalid,
        OnUpdate.class);

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
  @DisplayName("更新時_emailの形式が不正な場合は入力チェックにかかること")
  void test_014(String invalidEmail) {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;
    StudentForm student = new StudentForm(
        studentId, "山田太郎", "やまだたろう", "タロー", invalidEmail,
        "東京都", "090-0000-0000", 20, "男", "", false
    );
    StudentDetailForm studentDetail = new StudentDetailForm(student,
        List.of(makeCompletedStudentCourseForm(courseId)));

    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
        OnUpdate.class);

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
  @DisplayName("更新時_電話番号の形式が不正のとき入力チェックにかかること")
  void test_015(String invalidPhoneNumber) {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;
    StudentForm invalidStudent = new StudentForm(
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

    StudentDetailForm studentDetail = new StudentDetailForm(invalidStudent,
        List.of(makeCompletedStudentCourseForm(courseId)));

    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
        OnUpdate.class);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("student.telephone"))).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {14, 81})
  @DisplayName("更新時_年齢が15未満または80より大きいとき入力チェックにかかること")
  void test_016(int age)
      throws Exception {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;
    StudentDetailForm invalidStudentDetail = new StudentDetailForm(
        new StudentForm(
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
        List.of(makeCompletedStudentCourseForm(courseId))
    );
    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
        invalidStudentDetail,
        OnUpdate.class);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student.age"))).isTrue();
  }


  @ParameterizedTest
  @CsvSource({
      "男,false",
      "女,false",
      "その他,false",
      "男性,true",
      "female,true",
      "man,true",
      "それ以外,true"
  })
  @DisplayName("更新時_性別がパターン外のとき入力チェックにかかること")
  void test_017(String sex, boolean expectViolation) {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;
    StudentForm invalidStudent = new StudentForm(
        studentId, "山田太郎", "やまだたろう", "タロー", "test@example.com",
        "東京都", "090-0000-0000", 20, sex, "", false
    );
    StudentDetailForm StudentDetail = new StudentDetailForm(invalidStudent,
        List.of(makeCompletedStudentCourseForm(courseId)));

    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(StudentDetail,
        OnUpdate.class);

    // Act & Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("student.sex"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @Test
  @DisplayName("更新時_courseIdが1未満の数値の時入力チェックにかかること")
  void test_018() {
    Integer studentId = 1;
    Integer courseId = -3;
    StudentDetailForm studentDetail = new StudentDetailForm(
        makeCompletedStudentForm(studentId),
        List.of(makeCompletedStudentCourseForm(courseId)));

    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
        OnUpdate.class);
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().contains("courseId"))).isTrue();
  }

  @ParameterizedTest
  @CsvSource({
      "Javaコース,false",
      "AWSコース,false",
      "デザインコース,false",
      "Webマーケティングコース,false",
      "フロントエンドコース,false",
      "Java,true", //「コース」なし
      "Pythonコース,true", //存在しないコース
  })
  @DisplayName("更新時_コース名が正しい時は通過し、不正のときは入力チェックにかかること")
  void test_019(String courseName, boolean expectViolation) {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;
    LocalDate now = LocalDate.now();

    StudentDetailForm invalidStudentDetail = new StudentDetailForm(
        makeCompletedStudentForm(studentId),
        List.of(new StudentCourseForm(
            courseId,
            courseName,
            now.plusMonths(6)
        ))
    );

    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
        invalidStudentDetail,
        OnUpdate.class);

    // Act & Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .allMatch(v -> v.getPropertyPath().toString()
              .matches("studentCourseList\\[\\d+\\]\\.courseName"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @Test
  @DisplayName("更新時_すべてのフィールドが登録時に期待する値をもつとき入力チェックにかからないこと")
  void test_020() {
    // Arrange:
    Integer studentId = 1;
    Integer courseId = 1;
    StudentDetailForm validStudentDetail = new StudentDetailForm(
        makeCompletedStudentForm(studentId),
        List.of(makeCompletedStudentCourseForm(courseId))
    );
    Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(validStudentDetail,
        OnUpdate.class);

    // Act & Assert
    assertThat(violations).isEmpty();
  }


}