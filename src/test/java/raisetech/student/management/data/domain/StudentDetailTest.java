package raisetech.student.management.data.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudent;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudentCourse;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudentDetail;
import static raisetech.student.management.testutil.TestDataFactory.makeEnoughStudentCourseOnRegister;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
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

  @ParameterizedTest
  @CsvSource({// trueはNotNull, falseはnull許容
      "fullname,true",
      "kanaName,true",
      "email,true",
      "nickname,false",
      "area,false",
      "telephone,false",
      "remark,false"
  })
  void Studentフィールドのnull許容性を検証する(String fieldName,
      boolean expectViolation) {
    // Arrange
    int studentId = 0;
    int courseId = 0;
    Student student = new Student(
        studentId,
        fieldName.equals("fullname") ? null : "山田太郎",
        fieldName.equals("kanaName") ? null : "やまだたろう",
        fieldName.equals("nickname") ? null : "タロー",
        fieldName.equals("email") ? null : "yamada@example.com",
        fieldName.equals("area") ? null : "東京都",
        fieldName.equals("telephone") ? null : "090-0000-0000",
        20,
        "男",
        fieldName.equals("remark") ? null : "特になし",
        false
    );

    StudentDetail detail = new StudentDetail(student,
        List.of(makeEnoughStudentCourseOnRegister(studentId, courseId)));

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(detail);

    // Act & Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("student." + fieldName))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
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
    int studentId = 0;
    int courseId = 0;
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
    int studentId = 0;
    int courseId = 0;
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

  @ParameterizedTest
  @ValueSource(strings = {"男性", "female", "man", "それ以外"})
  void 性別がパターン外のときエラーになる(String invalidSex) {
    // Arrange
    int studentId = 0;
    int courseId = 0;
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
  void 受講生コースのコース名がnullや不正のとき入力チェックにかかること(String invalidCourseName) {
    // Arrange
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

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString()
            .matches("studentCourseList\\[\\d+\\]\\.courseName"))).isTrue();
  }

  @ParameterizedTest
  @CsvSource({
      "0,0,Default,false",//登録時は1未満でも良いが、
      "0,1,Default,false",
      "1,0,Default,false",
      "0,0,OnUpdate,true",//更新時は1未満のとき入力チェックにかかる
      "0,1,OnUpdate,true",
      "1,0,OnUpdate,true"
  })
  void studentIdまたはcourseIdが1未満のとき_バリデーショングループによって挙動が異なる(
      int studentId, int courseId, String groupName, boolean expectViolation) {
    // Arrange
    Class<?> group = groupName.equals("OnUpdate") ? OnUpdate.class : Default.class;

    StudentDetail detail = makeCompletedStudentDetail(studentId, courseId);

    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(detail, group);

    // Act & Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .allMatch(v -> v.getPropertyPath().toString().contains("Id"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }


  @ParameterizedTest
  @CsvSource({
      "Default,false",
      "OnUpdate,true"
  })
  void courseEndAtがnullのとき_登録時は通過し更新時はエラーになる(
      String groupName, boolean expectViolation) {
    // Arrange
    int studentId = 1;
    int courseId = 1;

    // グループ名をクラスに変換
    Class<?> validationGroup = groupName.equals("OnUpdate") ? OnUpdate.class : Default.class;

    StudentDetail studentDetail = new StudentDetail(
        makeCompletedStudent(studentId),
        List.of(new StudentCourse(
            courseId,
            "Javaコース",
            studentId,
            null,
            null // courseEndAt
        ))
    );

    Set<ConstraintViolation<StudentDetail>> violations =
        validator.validate(studentDetail, validationGroup);

    // Act & Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .allMatch(v -> v.getPropertyPath().toString()
              .matches("studentCourseList\\[\\d+\\]\\.courseEndAt"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }


}