package raisetech.student.management.data;

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
import raisetech.student.management.testutil.TestDataFactory;
import raisetech.student.management.validation.CreateGroup;
import raisetech.student.management.validation.UpdateGroup;

class StudentTest {

  private static Validator validator;

  @BeforeAll
  static void setUpValidator() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @ParameterizedTest(name = "[{index}] 登録時_フィールド: {0} がnullのとき violation={1}")
  @CsvSource({// trueはNotNull, falseはnull許容
      "studentId,false",
      "fullName,true",
      "kanaName,true",
      "nickname,true",
      "email,true",
      "area,false",
      "telephone,false",
      "age,false",
      "sex,false",
      "remark,false"
  })
  void 登録時_各フィールドのnull許容性のテスト(String fieldName,
      boolean expectViolation) {
    // Arrange
    Student student = new Student(
        fieldName.equals("studentId") ? null : 1,
        fieldName.equals("fullName") ? null : "山田太郎",
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

    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(student,
        CreateGroup.class);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @ParameterizedTest(name = "[{index}] 登録時_フィールド：{0}が上限50文字に対して{1}文字のとき violation={2}")
  @CsvSource({
      "fullName,49,false",
      "fullName,50,false",
      "fullName,51,true",

      "kanaName,49,false",
      "kanaName,50,false",
      "kanaName,51,true",

      "nickname,49,false",
      "nickname,50,false",
      "nickname,51,true",

      "area,49,false",
      "area,50,false",
      "area,51,true",
  })
  void 登録時_50文字上限のフィールドの文字数の境界値テスト(String fieldName, int length, boolean expectViolation) throws Exception {
    // Arrange
    Integer studentId = null;
    String testValue = "あ".repeat(length);
    Student invalidStudent = new Student(
        studentId,
        fieldName.equals("fullName") ? testValue : "山田太郎",
        fieldName.equals("kanaName") ? testValue : "やまだたろう",
        fieldName.equals("nickname") ? testValue : "タロー",
        "test@example.com",
        fieldName.equals("area") ? testValue : "東京都",
        "090-0000-0000",
        20,
        "男",
        "特になし",
        false
    );

    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(invalidStudent,
        CreateGroup.class);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations)
          .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @ParameterizedTest(name = "[{index}] 登録時_フィールド：remark が上限200文字に対して{0}文字のとき violation={1}")
  @CsvSource({
      "199,false",
      "200,false",
      "201,true"
  })
  void 登録時_200文字上限のフィールドの文字数の境界値テスト(int length, boolean expectViolation) throws Exception {
    // Arrange
    Integer studentId = null;
    String testValue = "あ".repeat(length);
    Student invalidStudent = new Student(
        studentId,
        "山田太郎",
        "やまだたろう",
        "タロー",
        "test@example.com",
        "東京都",
        "090-0000-0000",
        20,
        "男",
        testValue,
        false
    );

    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(invalidStudent,
        CreateGroup.class);

    // Assert
    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations)
          .anyMatch(v -> v.getPropertyPath().toString().equals("remark"));
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @ParameterizedTest(name = "[{index}] 登録時_emailの形式が {0} のときバリデーション違反が起きる")
  @ValueSource(strings = {
      "plainaddress",        // @なし
      "@missingusername.com",// ユーザー名なし
      "username@.com",       // ドメイン名先頭にドット
      "user@com.",           // ドメイン末尾にドット
      "user@-com.com",       // 不正なドメイン記号
      "user@com..com"        // ドット連続
  })
  void 登録時_emailの形式が不正なときバリデーション違反が起きる(String invalidEmail) {
    // Arrange
    Integer studentId = null;
    Student invalidStudent = new Student(
        studentId, "山田太郎", "やまだたろう", "タロー", invalidEmail,
        "東京都", "090-0000-0000", 20, "男", "", false
    );

    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(invalidStudent,
        CreateGroup.class);

    // Assert
    assertThat(violations)
        .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
  }

  @ParameterizedTest(name = "[{index}] 登録時_電話番号の形式が {0} のときバリデーション違反が起きる")
  @ValueSource(strings = {
      "09000000000",     // ハイフンなし
      "090-0000-000",    // 下4桁が3桁
      "0900-000-00000",  // 中間と下桁の桁数異常
      "abcd-efgh-ijkl",  // 数字以外
      "-03-1234-5678",   // 先頭にハイフン
      "03--1234-5678",   // ハイフン重複
      "03-1234--5678"    // ハイフン重複（別位置）
  })
  void 登録時_電話番号の形式が不正のときバリデーション違反が起きる(String invalidPhoneNumber) {
    // Arrange
    Integer studentId = null;
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

    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(invalidStudent,
        CreateGroup.class);

    // Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("telephone"))).isTrue();
  }

  @ParameterizedTest(name = "[{index}] 登録時_年齢が {0} のとき violation={1}")
  @CsvSource({
      "14,true",
      "15,false",
      "80,false",
      "81,true"
  })
  void 登録時_年齢バリデーションの境界値テスト(int age,boolean expectViolation)
      throws Exception {
    // Arrange
    Integer studentId = null;
    Student invalidStudent = new Student(
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
    );

    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(
        invalidStudent,
        CreateGroup.class);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("age"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @ParameterizedTest(name = "[{index}] 登録時_性別が {0} のとき violation={1}")
  @CsvSource({
      "男,false",
      "女,false",
      "その他,false",
      "男性,true",
      "female,true",
      "man,true",
      "それ以外,true"
  })
  void 登録時_性別が想定されたパターンのときは通過しパターン外のときバリデーション違反になること(
      String sex, boolean expectViolation) {
    // Arrange
    Integer studentId = null;
    Student invalidStudent = new Student(
        studentId,
        "山田太郎",
        "やまだたろう",
        "タロー",
        "test@example.com",
        "東京都",
        "090-0000-0000",
        20,
        sex,
        "",
        false
    );

    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(invalidStudent,
        CreateGroup.class);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("sex"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @Test
  void 登録時_Studentの各のフィールドが妥当な値を持つときバリデーション違反が起きない(){
    Integer studentId = null;
    Student validStudent = TestDataFactory.makeCompletedStudent(studentId);

    Set<ConstraintViolation<Student>> violations = validator.validate(validStudent,
        CreateGroup.class);

    assertThat(violations).isEmpty();
  }

  @ParameterizedTest(name = "[{index}] 更新時_フィールド: {0} がnullのとき violation={1}")
  @CsvSource({// trueはNotNull, falseはnull許容
      "studentId,true",
      "fullName,true",
      "kanaName,true",
      "nickname,true",
      "email,true",
      "area,false",
      "telephone,false",
      "age,false",
      "sex,false",
      "remark,false"
  })
  void 更新時_各フィールドのnull許容性のテスト(String fieldName,
      boolean expectViolation) {
    // Arrange
    Student student = new Student(
        fieldName.equals("studentId") ? null : 1,
        fieldName.equals("fullName") ? null : "山田太郎",
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

    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(student,
        UpdateGroup.class);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @Test
  void 更新時_studentIdが1未満の数値の時バリデーション違反_true() {
    Integer studentId = -3;
    Student invalidStudent = TestDataFactory.makeCompletedStudent(studentId);

    Set<ConstraintViolation<Student>> violations = validator.validate(invalidStudent,
        UpdateGroup.class);
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("studentId"))).isTrue();
  }

  @ParameterizedTest(name = "[{index}] 登録時_フィールド：{0}が上限50文字に対して{1}文字のとき violation={2}")
  @CsvSource({
      "fullName,49,false",
      "fullName,50,false",
      "fullName,51,true",

      "kanaName,49,false",
      "kanaName,50,false",
      "kanaName,51,true",

      "nickname,49,false",
      "nickname,50,false",
      "nickname,51,true",

      "area,49,false",
      "area,50,false",
      "area,51,true",
  })
  void 更新時_50文字上限のフィールドの文字数の境界値テスト(String fieldName, int length, boolean expectViolation) throws Exception {
    // Arrange
    Integer studentId = 1;
    String testValue = "あ".repeat(length);
    Student invalidStudent = new Student(
        studentId,
        fieldName.equals("fullName") ? testValue : "山田太郎",
        fieldName.equals("kanaName") ? testValue : "やまだたろう",
        fieldName.equals("nickname") ? testValue : "タロー",
        "test@example.com",
        fieldName.equals("area") ? testValue : "東京都",
        "090-0000-0000",
        20,
        "男",
        "特になし",
        false
    );

    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(invalidStudent,
        UpdateGroup.class);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations)
          .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @ParameterizedTest(name = "[{index}] 登録時_フィールド：remark が上限200文字に対して{0}文字のとき violation={1}")
  @CsvSource({
      "199,false",
      "200,false",
      "201,true"
  })
  void 更新時_200文字上限のフィールドの文字数の境界値テスト(int length, boolean expectViolation) throws Exception {
    // Arrange
    Integer studentId = 1;
    String testValue = "あ".repeat(length);
    Student invalidStudent = new Student(
        studentId,
        "山田太郎",
        "やまだたろう",
        "タロー",
        "test@example.com",
        "東京都",
        "090-0000-0000",
        20,
        "男",
        testValue,
        false
    );

    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(invalidStudent,
        UpdateGroup.class);

    // Assert
    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations)
          .anyMatch(v -> v.getPropertyPath().toString().equals("remark"));
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @ParameterizedTest(name = "[{index}] 更新時_emailの形式が {0} のとき violation={1}")
  @ValueSource(strings = {
      "plainaddress",        // @なし
      "@missingusername.com",// ユーザー名なし
      "username@.com",       // ドメイン名先頭にドット
      "user@com.",           // ドメイン末尾にドット
      "user@-com.com",       // 不正なドメイン記号
      "user@com..com"        // ドット連続
  })
  void 更新時_emailの形式が不正な場合はバリデーション違反が起きる(String invalidEmail) {
    // Arrange
    Integer studentId = 1;
    Student invalidStudent = new Student(
        studentId, "山田太郎", "やまだたろう", "タロー", invalidEmail,
        "東京都", "090-0000-0000", 20, "男", "", false
    );

    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(invalidStudent,
        UpdateGroup.class);

    // Assert
    assertThat(violations)
        .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
  }

  @ParameterizedTest(name = "[{index}] 更新時_電話番号の形式が {0} のとき violation={1}")
  @ValueSource(strings = {
      "09000000000",     // ハイフンなし
      "090-0000-000",    // 下4桁が3桁
      "0900-000-00000",  // 中間と下桁の桁数異常
      "abcd-efgh-ijkl",  // 数字以外
      "-03-1234-5678",   // 先頭にハイフン
      "03--1234-5678",   // ハイフン重複
      "03-1234--5678"    // ハイフン重複（別位置）
  })
  void 更新時_電話番号の形式が不正のときバリデーション違反が起きる(String invalidPhoneNumber) {
    // Arrange
    Integer studentId = 1;
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

    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(invalidStudent,
        UpdateGroup.class);

    // Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("telephone"))).isTrue();
  }

  @ParameterizedTest(name = "[{index}] 更新時_年齢が {0} のとき violation={1}")
  @CsvSource({
      "14,true",
      "15,false",
      "80,false",
      "81,true"
  })
  void 更新時_年齢バリデーションの境界値テスト(int age, boolean expectViolation)
      throws Exception {
    // Arrange
    Integer studentId = 1;
    Student invalidStudent = new Student(
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
    );
    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(
        invalidStudent,
        UpdateGroup.class);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("age"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }


  @ParameterizedTest(name = "[{index}] 更新時_性別が ”{0}” のとき violation={1}")
  @CsvSource({
      "男,false",
      "女,false",
      "その他,false",
      "男性,true",
      "female,true",
      "man,true",
      "それ以外,true"
  })
  void 更新時_性別が想定されたパターンのとき通過しパターン外のときバリデーション違反が起きること(
      String sex, boolean expectViolation) {
    // Arrange
    Integer studentId = 1;
    Student invalidStudent = new Student(
        studentId, "山田太郎", "やまだたろう", "タロー", "test@example.com",
        "東京都", "090-0000-0000", 20, sex, "", false
    );

    // Act
    Set<ConstraintViolation<Student>> violations = validator.validate(invalidStudent,
        UpdateGroup.class);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("sex"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @Test
  void 更新時_Studentの各のフィールドが妥当な値を持つときバリデーション違反が起きない(){
    Integer studentId = 1;
    Student validStudent = TestDataFactory.makeCompletedStudent(studentId);

    Set<ConstraintViolation<Student>> violations = validator.validate(validStudent,
        UpdateGroup.class);

    assertThat(violations).isEmpty();
  }


}