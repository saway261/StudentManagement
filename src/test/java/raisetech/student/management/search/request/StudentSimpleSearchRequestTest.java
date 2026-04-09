package raisetech.student.management.search.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StudentSimpleSearchRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setUpValidator() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @ParameterizedTest(name = "[{index}] フィールド：{0} が上限50文字に対して {1} 文字のとき violation={2}")
  @CsvSource({
      "fullNameContains,50,false",
      "fullNameContains,51,true",
      "kanaNameContains,50,false",
      "kanaNameContains,51,true",
      "areaContains,50,false",
      "areaContains,51,true"
  })
  void 文字列項目の文字数境界値テスト(String fieldName, int length, boolean expectViolation) {
    // Arrange
    String value = "あ".repeat(length);
    StudentSimpleSearchRequest request = new StudentSimpleSearchRequest();

    switch (fieldName) {
      case "fullNameContains" -> request.setFullNameContains(value);
      case "kanaNameContains" -> request.setKanaNameContains(value);
      case "areaContains" -> request.setAreaContains(value);
      default -> throw new IllegalArgumentException("想定外のfieldNameです: " + fieldName);
    }

    // Act
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations = validator.validate(request);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName)))
          .isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @ParameterizedTest(name = "[{index}] sexEq が {0} のとき violation={1}")
  @CsvSource({
      "男,false",
      "女,false",
      "その他,false",
      "男性,true",
      "female,true",
      "man,true",
      "それ以外,true"
  })
  void sexEqのパターンバリデーションテスト(String sexEq, boolean expectViolation) {
    // Arrange
    StudentSimpleSearchRequest request = new StudentSimpleSearchRequest();
    request.setSexEq(sexEq);

    // Act
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations = validator.validate(request);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("sexEq"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @ParameterizedTest(name = "[{index}] statusId={0} のとき要素バリデーション violation={1}")
  @CsvSource({
      "0,true",
      "1,false",
      "5,false",
      "6,true"
  })
  void statusId要素の境界値テスト(int statusId, boolean expectViolation) {
    // Arrange
    StudentSimpleSearchRequest request = new StudentSimpleSearchRequest();
    request.setStatusId(List.of(statusId));

    // Act
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations = validator.validate(request);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().contains("statusId"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @Test
  void statusIdに複数の妥当な値を指定したときバリデーション違反が起きない() {
    // Arrange
    StudentSimpleSearchRequest request = new StudentSimpleSearchRequest();
    request.setStatusId(List.of(1, 3, 5));

    // Act
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations = validator.validate(request);

    // Assert
    assertThat(violations).isEmpty();
  }

  @ParameterizedTest(name = "[{index}] 年齢範囲 ageMin={0}, ageMax={1} のとき violation={2}")
  @CsvSource({
      "20,30,false",
      "20,20,true",
      "30,20,true"
  })
  void 年齢範囲の相関チェック(Integer ageMin, Integer ageMax, boolean expectViolation) {
    // Arrange
    StudentSimpleSearchRequest request = new StudentSimpleSearchRequest();
    request.setAgeMin(ageMin);
    request.setAgeMax(ageMax);

    // Act
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations = validator.validate(request);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("ageRangeValid"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @Test
  void 年齢範囲は片方のみ指定ならバリデーション違反が起きない() {
    // Arrange
    StudentSimpleSearchRequest request1 = new StudentSimpleSearchRequest();
    request1.setAgeMin(20);

    StudentSimpleSearchRequest request2 = new StudentSimpleSearchRequest();
    request2.setAgeMax(30);

    // Act
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations1 = validator.validate(request1);
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations2 = validator.validate(request2);

    // Assert
    assertThat(violations1).isEmpty();
    assertThat(violations2).isEmpty();
  }

  @ParameterizedTest(name = "[{index}] 申込日範囲 applyFrom={0}, applyTo={1} のとき violation={2}")
  @CsvSource({
      "2026-01-01,2026-01-31,false",
      "2026-01-01,2026-01-01,true",
      "2026-02-01,2026-01-31,true"
  })
  void 申込日範囲の相関チェック(String applyFrom, String applyTo, boolean expectViolation) {
    // Arrange
    StudentSimpleSearchRequest request = new StudentSimpleSearchRequest();
    request.setApplyFrom(LocalDate.parse(applyFrom));
    request.setApplyTo(LocalDate.parse(applyTo));

    // Act
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations = validator.validate(request);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("applyDateValid"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @Test
  void 申込日範囲は片方のみ指定ならバリデーション違反が起きない() {
    // Arrange
    StudentSimpleSearchRequest request1 = new StudentSimpleSearchRequest();
    request1.setApplyFrom(LocalDate.of(2026, 1, 1));

    StudentSimpleSearchRequest request2 = new StudentSimpleSearchRequest();
    request2.setApplyTo(LocalDate.of(2026, 1, 31));

    // Act
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations1 = validator.validate(request1);
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations2 = validator.validate(request2);

    // Assert
    assertThat(violations1).isEmpty();
    assertThat(violations2).isEmpty();
  }

  @ParameterizedTest(name = "[{index}] 受講開始日範囲 startFrom={0}, startTo={1} のとき violation={2}")
  @CsvSource({
      "2026-02-01,2026-02-28,false",
      "2026-02-01,2026-02-01,true",
      "2026-03-01,2026-02-28,true"
  })
  void 受講開始日範囲の相関チェック(String startFrom, String startTo, boolean expectViolation) {
    // Arrange
    StudentSimpleSearchRequest request = new StudentSimpleSearchRequest();
    request.setStartFrom(LocalDate.parse(startFrom));
    request.setStartTo(LocalDate.parse(startTo));

    // Act
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations = validator.validate(request);

    // Assert
    if (expectViolation) {
      assertThat(violations).isNotEmpty();
      assertThat(violations.stream()
          .anyMatch(v -> v.getPropertyPath().toString().equals("startDateValid"))).isTrue();
    } else {
      assertThat(violations).isEmpty();
    }
  }

  @Test
  void 受講開始日範囲は片方のみ指定ならバリデーション違反が起きない() {
    // Arrange
    StudentSimpleSearchRequest request1 = new StudentSimpleSearchRequest();
    request1.setStartFrom(LocalDate.of(2026, 2, 1));

    StudentSimpleSearchRequest request2 = new StudentSimpleSearchRequest();
    request2.setStartTo(LocalDate.of(2026, 2, 28));

    // Act
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations1 = validator.validate(request1);
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations2 = validator.validate(request2);

    // Assert
    assertThat(violations1).isEmpty();
    assertThat(violations2).isEmpty();
  }

  @Test
  void 各フィールドに妥当な値を持つときバリデーション違反が起きない() {
    // Arrange
    StudentSimpleSearchRequest request = new StudentSimpleSearchRequest();
    request.setFullNameContains("田中");
    request.setKanaNameContains("たなか");
    request.setAreaContains("東京");
    request.setAgeMin(20);
    request.setAgeMax(30);
    request.setSexEq("女");
    request.setCourseCode("JA");
    request.setStatusId(List.of(1, 2));
    request.setApplyFrom(LocalDate.of(2026, 1, 1));
    request.setApplyTo(LocalDate.of(2026, 1, 31));
    request.setStartFrom(LocalDate.of(2026, 2, 1));
    request.setStartTo(LocalDate.of(2026, 2, 28));
    request.setIsDeleted(false);

    // Act
    Set<ConstraintViolation<StudentSimpleSearchRequest>> violations = validator.validate(request);

    // Assert
    assertThat(violations).isEmpty();
  }
}