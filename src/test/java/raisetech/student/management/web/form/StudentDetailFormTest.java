package raisetech.student.management.web.form;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;
import raisetech.student.management.data.value.Id;
import raisetech.student.management.testutil.TestDataFactory;

class StudentDetailFormTest {

  private static Validator validator;

  @BeforeAll
  static void setUpValidator() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Nested
  @DisplayName("バリデーションのテスト")
  class validation {

    @Test
    @DisplayName("登録時_受講生がnullのときバリデーションエラー : true")
    void test_001() {
      // Arrange
      Integer courseId = 1;
      StudentDetailForm invalidStudentDetailForm = new StudentDetailForm(
          null, List.of(TestDataFactory.makeCompletedStudentCourseForm(courseId))
      );
      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
          invalidStudentDetailForm,
          OnRegister.class);

      // Act & Assert
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("student"))).isTrue();
      // studentCourseでviolationが発生しても、studentでのviolationが確認できれば良いのでanyMatch
    }

    @Test
    @DisplayName("登録時_受講生コースが空のとき入力チェックにかかること")
    void test_002() {
      // Arrange
      Integer studentId = 1;
      StudentDetailForm invalidStudentDetail = new StudentDetailForm(
          TestDataFactory.makeCompletedStudentForm(studentId), new ArrayList<StudentCourseForm>()
      );
      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
          invalidStudentDetail,
          OnRegister.class);

      // Act & Assert
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("studentCourseList"))).isTrue();
    }

    @ParameterizedTest(name = "[{index}] 登録時_フィールド: {0} がnullでバリデーションエラー: {1}")
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

    @ParameterizedTest(name = "[{index}] 登録時_{0}が文字数超過のときバリデーションエラー : true")
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
          List.of(TestDataFactory.makeEnoughStudentCourseFormOnRegister()));

      // Act
      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(invalid,
          OnRegister.class);

      // Assert
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .allMatch(v -> v.getPropertyPath().toString().equals("student." + fieldName))).isTrue();
    }

    @ParameterizedTest(name = "[{index}] 登録時_emailの形式が {0} のときバリデーションエラー : true")
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
          List.of(TestDataFactory.makeEnoughStudentCourseFormOnRegister()));

      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
          OnRegister.class);

      // Act & Assert
      assertThat(violations)
          .anyMatch(v -> v.getPropertyPath().toString().equals("student.email"));
    }

    @ParameterizedTest(name = "[{index}] 登録時_電話番号の形式が {0} のときバリデーションエラー : true")
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
          List.of(TestDataFactory.makeEnoughStudentCourseFormOnRegister()));

      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
          OnRegister.class);

      // Act & Assert
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("student.telephone"))).isTrue();
    }

    @ParameterizedTest(name = "[{index}] 登録時_年齢が {0} のときバリデーションエラー : true")
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
          List.of(TestDataFactory.makeEnoughStudentCourseFormOnRegister())
      );
      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
          invalidStudentDetail,
          OnRegister.class);

      // Act & Assert
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .allMatch(v -> v.getPropertyPath().toString().equals("student.age"))).isTrue();
    }

    @ParameterizedTest(name = "登録時_性別が {0} のときバリデーションエラー：{1}")
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
          List.of(TestDataFactory.makeEnoughStudentCourseFormOnRegister()));

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

    @ParameterizedTest(name = "登録時_コース名が {0} のときバリデーションエラー：{1}")
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
          TestDataFactory.makeCompletedStudentForm(studentId),
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
    @DisplayName("登録時_すべてのフィールドが登録時に期待する値をもつときバリデーションエラー : false")
    void test_010() {
      // Arrange:
      Integer studentId = null;
      Integer courseId = null;
      StudentDetailForm validStudentDetail = new StudentDetailForm(
          TestDataFactory.makeCompletedStudentForm(studentId),
          List.of(TestDataFactory.makeEnoughStudentCourseFormOnRegister())
      );
      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
          validStudentDetail,
          OnRegister.class);

      // Act & Assert
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("更新時_受講生がnullのときバリデーションエラー : true")
    void test_011() {
      // Arrange
      Integer courseId = 1;
      StudentDetailForm invalidStudentDetailForm = new StudentDetailForm(
          null, List.of(TestDataFactory.makeCompletedStudentCourseForm(courseId))
      );
      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
          invalidStudentDetailForm,
          OnUpdate.class);

      // Act & Assert
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("student"))).isTrue();
      // studentCourseでviolationが発生しても、studentでのviolationが確認できれば良いのでanyMatch
    }

    @Test
    @DisplayName("登録時および更新時_受講生コースが空のときバリデーションエラー : true")
    void test_012() {
      // Arrange
      Integer studentId = 1;
      StudentDetailForm invalidStudentDetail = new StudentDetailForm(
          TestDataFactory.makeCompletedStudentForm(studentId), new ArrayList<StudentCourseForm>()
      );
      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
          invalidStudentDetail,
          OnUpdate.class);

      // Act & Assert
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("studentCourseList"))).isTrue();
    }

    @ParameterizedTest(name = "[{index}] 更新時_フィールド: {0} がnullでバリデーションエラー: {1}")
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
    void test_013(String fieldName, boolean expectViolation) {
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
    @DisplayName("更新時_studentIdが1未満の数値の時バリデーションエラー : true")
    void test_014() {
      Integer studentId = -3;
      Integer courseId = 1;
      StudentDetailForm studentDetail = new StudentDetailForm(
          TestDataFactory.makeCompletedStudentForm(studentId),
          List.of(TestDataFactory.makeCompletedStudentCourseForm(courseId)));

      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
          OnUpdate.class);
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().contains("studentId"))).isTrue();
    }

    @ParameterizedTest(name = "[{index}] 更新時_{0}が文字数超過のときバリデーションエラー : true")
    @ValueSource(strings = {"fullname", "kanaName", "nickname", "email", "area", "remark"})
    @DisplayName("更新時_文字列超過のとき入力チェックにかかること")
    void test_015(String fieldName) throws Exception {
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
          List.of(TestDataFactory.makeCompletedStudentCourseForm(courseId)));

      // Act
      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(invalid,
          OnUpdate.class);

      // Assert
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .allMatch(v -> v.getPropertyPath().toString().equals("student." + fieldName))).isTrue();

    }

    @ParameterizedTest(name = "[{index}] 更新時_emailの形式が {0} のときバリデーションエラー : true")
    @ValueSource(strings = {
        "plainaddress",        // @なし
        "@missingusername.com",// ユーザー名なし
        "username@.com",       // ドメイン名先頭にドット
        "user@com.",           // ドメイン末尾にドット
        "user@-com.com",       // 不正なドメイン記号
        "user@com..com"        // ドット連続
    })
    @DisplayName("更新時_emailの形式が不正な場合は入力チェックにかかること")
    void test_016(String invalidEmail) {
      // Arrange
      Integer studentId = 1;
      Integer courseId = 1;
      StudentForm student = new StudentForm(
          studentId, "山田太郎", "やまだたろう", "タロー", invalidEmail,
          "東京都", "090-0000-0000", 20, "男", "", false
      );
      StudentDetailForm studentDetail = new StudentDetailForm(student,
          List.of(TestDataFactory.makeCompletedStudentCourseForm(courseId)));

      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
          OnUpdate.class);

      // Act & Assert
      assertThat(violations)
          .anyMatch(v -> v.getPropertyPath().toString().equals("student.email"));
    }

    @ParameterizedTest(name = "[{index}] 更新時_電話番号の形式が {0} のときバリデーションエラー : true")
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
    void test_017(String invalidPhoneNumber) {
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
          List.of(TestDataFactory.makeCompletedStudentCourseForm(courseId)));

      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
          OnUpdate.class);

      // Act & Assert
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("student.telephone"))).isTrue();
    }

    @ParameterizedTest(name = "[{index}] 更新時_年齢が {0} のときバリデーションエラー : true")
    @ValueSource(ints = {14, 81})
    @DisplayName("更新時_年齢が15未満または80より大きいとき入力チェックにかかること")
    void test_018(int age)
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
          List.of(TestDataFactory.makeCompletedStudentCourseForm(courseId))
      );
      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
          invalidStudentDetail,
          OnUpdate.class);

      // Act & Assert
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .allMatch(v -> v.getPropertyPath().toString().equals("student.age"))).isTrue();
    }


    @ParameterizedTest(name = "更新時_性別が {0} のときバリデーションエラー：{1}")
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
    void test_019(String sex, boolean expectViolation) {
      // Arrange
      Integer studentId = 1;
      Integer courseId = 1;
      StudentForm invalidStudent = new StudentForm(
          studentId, "山田太郎", "やまだたろう", "タロー", "test@example.com",
          "東京都", "090-0000-0000", 20, sex, "", false
      );
      StudentDetailForm StudentDetail = new StudentDetailForm(invalidStudent,
          List.of(TestDataFactory.makeCompletedStudentCourseForm(courseId)));

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
    @DisplayName("更新時_courseIdが1未満の数値の時バリデーションエラー : true")
    void test_020() {
      Integer studentId = 1;
      Integer courseId = -3;
      StudentDetailForm studentDetail = new StudentDetailForm(
          TestDataFactory.makeCompletedStudentForm(studentId),
          List.of(TestDataFactory.makeCompletedStudentCourseForm(courseId)));

      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(studentDetail,
          OnUpdate.class);
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().contains("courseId"))).isTrue();
    }

    @ParameterizedTest(name = "更新時_コース名が {0} のときバリデーションエラー：{1}")
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
    void test_021(String courseName, boolean expectViolation) {
      // Arrange
      Integer studentId = 1;
      Integer courseId = 1;
      LocalDate now = LocalDate.now();

      StudentDetailForm invalidStudentDetail = new StudentDetailForm(
          TestDataFactory.makeCompletedStudentForm(studentId),
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
    @DisplayName("更新時_すべてのフィールドが登録時に期待する値をもつときバリデーションエラー : false")
    void test_022() {
      // Arrange:
      Integer studentId = 1;
      Integer courseId = 1;
      StudentDetailForm validStudentDetail = new StudentDetailForm(
          TestDataFactory.makeCompletedStudentForm(studentId),
          List.of(TestDataFactory.makeCompletedStudentCourseForm(courseId))
      );
      Set<ConstraintViolation<StudentDetailForm>> violations = validator.validate(
          validStudentDetail,
          OnUpdate.class);

      // Act & Assert
      assertThat(violations).isEmpty();
    }

  }

  @Nested
  @DisplayName("ドメインオブジェクトへの変換処理のテスト")
  class converter {

    @Test
    void 登録時_StudentDetailFormがStudentDetailに変換されstudentIdとcourseIdがId型に変換されていること() {
      // Arrange
      StudentDetailForm detailForm = TestDataFactory.makeDummyStudentDetailFormOnRegister();
      StudentForm studentForm = detailForm.getStudent();
      List<StudentCourseForm> coursesForm = detailForm.getStudentCourseList();

      // Act
      StudentDetail domain = StudentDetailForm.toDomain(detailForm);

      // Assert
      Student studentDomain = domain.getStudent();
      List<StudentCourse> coursesDomain = domain.getStudentCourseList();

      assertThat(studentDomain.getStudentId()).isNull();
      assertThat(studentDomain.getFullname()).isEqualTo(studentForm.getFullname());
      assertThat(studentDomain.getKanaName()).isEqualTo(studentForm.getKanaName());
      assertThat(studentDomain.getNickname()).isEqualTo(studentForm.getNickname());
      assertThat(studentDomain.getEmail()).isEqualTo(studentForm.getEmail());
      assertThat(studentDomain.getArea()).isEqualTo(studentForm.getArea());
      assertThat(studentDomain.getTelephone()).isEqualTo(studentForm.getTelephone());
      assertThat(studentDomain.getSex()).isEqualTo(studentForm.getSex());
      assertThat(studentDomain.getRemark()).isEqualTo(studentForm.getRemark());
      assertThat(studentDomain.isDeleted()).isEqualTo(studentForm.isDeleted());

      assertThat(coursesDomain.get(0).getCourseId()).isNull();
      assertThat(coursesDomain.get(0).getCourseName()).isEqualTo(
          coursesForm.get(0).getCourseName());
      assertThat(coursesDomain.get(0).getStudentId()).isNull();
      assertThat(coursesDomain.get(0).getCourseStartAt()).isNull();
      assertThat(coursesDomain.get(0).getCourseEndAt()).isEqualTo(
          coursesForm.get(0).getCourseEndAt());
    }

    @Test
    void 更新時_StudentDetailFormがStudentDetailに変換されstudentIdとcourseIdがId型に変換されていること() {
      // Arrange
      Integer studentId = 1;
      Integer courseId = 1;
      StudentDetailForm detailForm = TestDataFactory.makeDummyStudentDetailFormOnUpdate(studentId,
          courseId);
      StudentForm studentForm = detailForm.getStudent();
      List<StudentCourseForm> coursesForm = detailForm.getStudentCourseList();

      // Act
      StudentDetail domain = StudentDetailForm.toDomain(detailForm);

      // Assert
      Student studentDomain = domain.getStudent();
      List<StudentCourse> coursesDomain = domain.getStudentCourseList();

      assertThat(studentDomain.getStudentId()).isEqualTo(new Id(studentForm.getStudentId()));
      assertThat(studentDomain.getFullname()).isEqualTo(studentForm.getFullname());
      assertThat(studentDomain.getKanaName()).isEqualTo(studentForm.getKanaName());
      assertThat(studentDomain.getNickname()).isEqualTo(studentForm.getNickname());
      assertThat(studentDomain.getEmail()).isEqualTo(studentForm.getEmail());
      assertThat(studentDomain.getArea()).isEqualTo(studentForm.getArea());
      assertThat(studentDomain.getTelephone()).isEqualTo(studentForm.getTelephone());
      assertThat(studentDomain.getSex()).isEqualTo(studentForm.getSex());
      assertThat(studentDomain.getRemark()).isEqualTo(studentForm.getRemark());
      assertThat(studentDomain.isDeleted()).isEqualTo(studentForm.isDeleted());

      assertThat(coursesDomain.get(0).getCourseId()).isEqualTo(
          new Id(coursesForm.get(0).getCourseId()));
      assertThat(coursesDomain.get(0).getCourseName()).isEqualTo(
          coursesForm.get(0).getCourseName());
      assertThat(coursesDomain.get(0).getStudentId()).isNull();
      assertThat(coursesDomain.get(0).getCourseStartAt()).isNull();
      assertThat(coursesDomain.get(0).getCourseEndAt()).isEqualTo(
          coursesForm.get(0).getCourseEndAt());
    }

  }


}