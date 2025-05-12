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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;
import raisetech.student.management.data.value.Id;

class StudentDetailTest {

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
  @DisplayName("登録時および更新時_受講生コースが空のとき入力チェックにかかること")
  void test_002(String groupName) {
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
      "courseStartAt,false", //TC-15
      "courseEndAt,false" //TC-16
  })
  @DisplayName("登録時_StudentDetailの各フィールドのnull許容性を検証する")
  void test_003(String fieldName, boolean expectViolation) {
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
  @ValueSource(strings = {"fullname", "kanaName", "nickname", "email", "area", "remark"})
  @DisplayName("登録時_文字列超過のとき入力チェックにかかること")
  void test_004(String fieldName) throws Exception {
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
    Id studentId = null;
    Id courseId = null;
    Student student = new Student(
        studentId, "山田太郎", "やまだたろう", "タロー", invalidEmail,
        "東京都", "090-0000-0000", 20, "男", "", false
    );
    StudentDetail studentDetail = new StudentDetail(student,
        List.of(makeEnoughStudentCourseOnRegister(studentId, courseId)));

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(studentDetail,
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

    StudentDetail studentDetail = new StudentDetail(invalidStudent,
        List.of(makeEnoughStudentCourseOnRegister(studentId, courseId)));

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(studentDetail,
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
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail,
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
    Id studentId = null;
    Id courseId = null;
    Student invalidStudent = new Student(
        studentId, "山田太郎", "やまだたろう", "タロー", "test@example.com",
        "東京都", "090-0000-0000", 20, sex, "", false
    );
    StudentDetail StudentDetail = new StudentDetail(invalidStudent,
        List.of(makeEnoughStudentCourseOnRegister(studentId, courseId)));

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(StudentDetail,
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
    Id studentId = null;
    Id courseId = null;

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

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail,
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

  @ParameterizedTest
  @CsvSource({// trueはNotNull, falseはnull許容
      "studentId,true", //TC-32
      "fullname,true", //TC-33
      "kanaName,true", //TC-34
      "nickname,false", //TC-35
      "email,true", //TC-36
      "area,false", //TC-37
      "telephone,false", //TC-38
      "age,false", //TC-39
      "sex,false", //TC-40
      "remark,false", //TC-41
      "courseId,true", //TC-42
      "courseName,true", //TC-43
      "courseStartAt,false", //TC-44
      "courseEndAt,true" //TC-45
  })
  @DisplayName("更新時_StudentDetailの各フィールドのnull許容性を検証する")
  void test_011(String fieldName, boolean expectViolation) {
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

//  @Test
//  @DisplayName("更新時_studentIdが1未満の数値の時入力チェックにかかること")
//  void test_012() {
//    Id studentId = new Id(-3);
//    Id courseId = new Id(1);
//
//    StudentDetail studentDetail = makeCompletedStudentDetail(studentId, courseId);
//
//    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(studentDetail,
//        OnUpdate.class);
//
//    assertThat(violations).isNotEmpty();
//    assertThat(violations.stream()
//        .anyMatch(v -> v.getPropertyPath().toString().contains("studentId"))).isTrue();
//
//  }

  @ParameterizedTest
  @ValueSource(strings = {"fullname", "kanaName", "nickname", "email", "area", "remark"})
  @DisplayName("更新時_文字列超過のとき入力チェックにかかること")
  void test_013(String fieldName) throws Exception {
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
  @DisplayName("更新時_emailの形式が不正な場合は入力チェックにかかること")
  void test_014(String invalidEmail) {
    // Arrange
    Id studentId = new Id(1);
    Id courseId = new Id(1);
    Student student = new Student(
        studentId, "山田太郎", "やまだたろう", "タロー", invalidEmail,
        "東京都", "090-0000-0000", 20, "男", "", false
    );
    StudentDetail studentDetail = new StudentDetail(student,
        List.of(makeCompletedStudentCourse(studentId, courseId)));

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(studentDetail,
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
    Id studentId = new Id(1);
    Id courseId = new Id(1);
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

    StudentDetail studentDetail = new StudentDetail(invalidStudent,
        List.of(makeCompletedStudentCourse(studentId, courseId)));

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(studentDetail,
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
    Id studentId = new Id(1);
    Id courseId = new Id(1);
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
        List.of(makeCompletedStudentCourse(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail,
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
    Id studentId = new Id(1);
    Id courseId = new Id(1);
    Student invalidStudent = new Student(
        studentId, "山田太郎", "やまだたろう", "タロー", "test@example.com",
        "東京都", "090-0000-0000", 20, sex, "", false
    );
    StudentDetail StudentDetail = new StudentDetail(invalidStudent,
        List.of(makeCompletedStudentCourse(studentId, courseId)));

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(StudentDetail,
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

//  @Test
//  @DisplayName("更新時_courseIdが1未満の数値の時入力チェックにかかること")
//  void test_018() {
//
//  }

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
  void test_019(String invalidCourseName, boolean expectViolation) {
    // Arrange
    Id studentId = new Id(1);
    Id courseId = new Id(1);
    LocalDate now = LocalDate.now();

    StudentDetail invalidStudentDetail = new StudentDetail(
        makeCompletedStudent(studentId),
        List.of(new StudentCourse(
            courseId,
            invalidCourseName,
            studentId,
            now,
            now.plusMonths(6)
        ))
    );

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail,
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

}