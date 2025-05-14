package raisetech.student.management.data.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;

class IdTest {

  private static Validator validator;

  @BeforeAll
  static void setupValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    void nullを渡すと例外を投げずに生成されること() {
      Id id = new Id(null);
      assertThat(id.isNull()).isTrue();
      assertThat(id.getValue()).isNull();
    }

    @Test
    void 引数が1以上の整数の場合正常に生成されること() {
      Id id = new Id(10);
      assertThat(id.isNull()).isFalse();
      assertThat(id.getValue()).isEqualTo(10);
    }

    @Test
    void 引数が0以下の整数のとき例外が発生すること() {
      assertThrows(IllegalArgumentException.class, () -> new Id(0));
      assertThrows(IllegalArgumentException.class, () -> new Id(-1));
    }
  }

  @Nested
  @DisplayName("バリデーション: OnRegisterグループ")
  class OnRegisterValidation {

    @Test
    void nullは許容される() {
      Id id = new Id(null);
      Set<ConstraintViolation<Id>> violations = validator.validate(id, OnRegister.class);
      assertThat(violations).isEmpty();
    }

    @Test
    void 正の整数は許容される() {
      Id id = new Id(1);
      Set<ConstraintViolation<Id>> violations = validator.validate(id, OnRegister.class);
      assertThat(violations).isEmpty();
    }
  }

  @Nested
  @DisplayName("バリデーション: OnUpdateグループ")
  class OnUpdateValidation {

    @Test
    void nullは許容される() {
      Id id = new Id(null);
      Set<ConstraintViolation<Id>> violations = validator.validate(id, OnUpdate.class);
      assertThat(violations).isEmpty();
    }

    @Test
    void 正の値は許容される() {
      Id id = new Id(10);
      Set<ConstraintViolation<Id>> violations = validator.validate(id, OnUpdate.class);
      assertThat(violations).isEmpty();
    }
  }

  @Test
  void toStringは値の文字列表現を返すこと() {
    Id id = new Id(123);
    assertThat(id.toString()).isEqualTo("123");
  }

  @Test
  void equalsとhashCodeの動作確認() {
    Id id1 = new Id(5);
    Id id2 = new Id(5);
    Id id3 = new Id(6);
    Id idNull1 = new Id(null);
    Id idNull2 = new Id(null);

    assertThat(id1).isEqualTo(id2);
    assertThat(id1).hasSameHashCodeAs(id2);
    assertThat(id1).isNotEqualTo(id3);
    assertThat(id1).isNotEqualTo(null);
    assertThat(id1).isNotEqualTo("some string");
    assertThat(idNull1).isEqualTo(idNull2);
  }
}
