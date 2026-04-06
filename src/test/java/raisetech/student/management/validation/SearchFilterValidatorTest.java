package raisetech.student.management.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.search.request.SearchFilter;
import raisetech.student.management.search.request.SearchOperator;

@ExtendWith(MockitoExtension.class)
class SearchFilterValidatorTest {

  private SearchFilterValidator sut;

  @Mock
  private ConstraintValidatorContext context;

  @Mock
  private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

  @Mock
  private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;

  @BeforeEach
  void setUp() {
    sut = new SearchFilterValidator();
  }

  // ConstraintValidatorContext のメソッドチェーンにスタブを設定する。
  private void stubAddError(){
    Mockito.when(context.buildConstraintViolationWithTemplate(anyString()))
        .thenReturn(violationBuilder);
    Mockito.when(violationBuilder.addPropertyNode(anyString()))
        .thenReturn(nodeBuilder);
    Mockito.when(nodeBuilder.addConstraintViolation())
        .thenReturn(context);
  }

  /************************
   * リクエストボディそのものと各フィールドがnull・空文字の時に通過するかのテスト
   ************************/

  @Test
  void filterがnullの場合trueを返すこと() {
    boolean result = sut.isValid(null, context);

    assertThat(result).isTrue();
  }

  @Test
  void fieldが空文字の場合trueを返すこと() {//NotBlankは別のアノテーションで担保するため
    SearchFilter filter = new SearchFilter("", SearchOperator.EQ, "佐藤", null);

    boolean result = sut.isValid(filter, context);

    assertThat(result).isTrue();
  }

  @Test
  void operatorがnullの場合trueを返すこと() {//NotNullは別のアノテーションで担保するため
    SearchFilter filter = new SearchFilter("fullName", null, "佐藤", null);

    boolean result = sut.isValid(filter, context);

    assertThat(result).isTrue();
  }


  /************************
   * 演算子と value / values の整合性チェックのテスト
   ************************/

  @ParameterizedTest(name = "[{index}] operatorが{0}のときvalueがnullだとfalseを返すこと")
  @EnumSource(value = SearchOperator.class, names = {"EQ", "CONTAINS","STARTS_WITH","ENDS_WITH","GTE","LTE"})
  void 単一値をもとめるoperatorを指定するときvalueがnullだとfalseを返すこと(SearchOperator operator) {
    SearchFilter filter = new SearchFilter("age", operator, null, List.of("1"));
    stubAddError();

    boolean result = sut.isValid(filter, context);

    assertThat(result).isFalse();
  }


  @ParameterizedTest(name = "[{index}] operatorが{0}のときvaluesがnullだとfalseを返すこと")
  @EnumSource(value = SearchOperator.class, names = {"BETWEEN", "IN"})
  void operatorがINとBETWEENのときvaluesがnullだとfalseを返すこと(SearchOperator operator) {
    SearchFilter filter = new SearchFilter("age", operator, "1", null);
    stubAddError();

    boolean result = sut.isValid(filter, context);

    assertThat(result).isFalse();
  }

  @ParameterizedTest(name = "[{index}] operatorにBETWEENを指定した際にvaluesの要素数が{0}個のとき violation={1}")
  @CsvSource({
      "0,false",
      "1,false",
      "2,true",
      "3,false",
  })
  void operatorがBETWEENのときのvaluesの要素数の妥当性検証(int count,boolean expectViolation) {
    List<String> values = new ArrayList<>(Collections.nCopies(count, "1"));
    SearchFilter filter = new SearchFilter("age", SearchOperator.BETWEEN, null, values);
    if(!expectViolation){
      stubAddError();
    }

    boolean result = sut.isValid(filter, context);

    assertThat(result).isEqualTo(expectViolation);
  }

  @ParameterizedTest(name = "[{index}] operatorにINを指定した際にvaluesの要素数が{0}個のとき{1}を返す")
  @CsvSource({
      "0,false",
      "1,false",
      "2,true",
      "3,true",
  })
  void operatorがINのときのvaluesの要素数の妥当性検証(int count,boolean expectViolation) {
    List<String> values = new ArrayList<>(Collections.nCopies(count, "1"));
    SearchFilter filter = new SearchFilter("statusId", SearchOperator.IN, null, values);
    if(!expectViolation){
      stubAddError();
    }

    boolean result = sut.isValid(filter, context);

    assertThat(result).isEqualTo(expectViolation);
  }


  /************************
   * フィールド名有効性チェックのテスト
   ************************/

  @Test
  void 存在しないfield名の場合falseを返すこと() {
    SearchFilter filter = new SearchFilter("invalidField", SearchOperator.EQ, "abc", null);
    stubAddError();

    boolean result = sut.isValid(filter, context);

    assertThat(result).isFalse();
  }



  /************************
   * 型整合性チェック(isTypeConsistent)のテスト
   ************************/

  @ParameterizedTest(name = "[{index}] String型項目に {0} 文字列を指定したとき 型変換可能であること")
  @ValueSource(strings = {"山田","abc","2025-01-01","1"})
  void String型フィールドの型変換可能性を検証(String valueStr) {
    // そもそもStringで受け取っているのでString型フィールドはすべての値が型変換チェックを通過する
    SearchFilter filter = new SearchFilter("fullName", SearchOperator.EQ, valueStr, null);

    boolean result = sut.isValid(filter, context);

    assertThat(result).isTrue();
  }

  @ParameterizedTest(name = "[{index}] Integer型項目に {0} 文字列を指定したとき 型変換={1}")
  @CsvSource({
      "123,true",
      "１２３,true",// 全角文字も通過
      "abc,false",
      "あいう,false",
  })
  void Integer型フィールドの型変換可能性を検証(String valueStr, boolean isParsable) {
    // Arrange
    SearchFilter filter = new SearchFilter("age", SearchOperator.EQ, valueStr, null);
    // 変換不可の場合のみaddErrorが呼ばれるためスタブ設定
    if(!isParsable){
      stubAddError();
    };

    boolean result = sut.isValid(filter, context);

    assertThat(result).isEqualTo(isParsable);
  }

  @ParameterizedTest(name = "[{index}] LocalDate型項目に {0} 文字列を指定したとき 型変換={1}")
  @CsvSource({
      "2025-01-01,true",//ISO_LOCAL_DATE形式だけが通過する
      "2025-1-1,false",
      "2025/01/01,false",
      "2025年1月1日,false",
      "abc,false",
  })
  void LocalDate型フィールドの型変換可能性を検証(String valueStr, boolean isParsable) {
    // Arrange
    SearchFilter filter = new SearchFilter("courseApplyAt", SearchOperator.EQ, valueStr, null);
    // 変換不可の場合のみaddErrorが呼ばれるためスタブ設定
    if(!isParsable){
      stubAddError();
    };

    boolean result = sut.isValid(filter, context);

    assertThat(result).isEqualTo(isParsable);
  }

  @ParameterizedTest(name = "[{index}] Boolean型項目に {0} 文字列を指定したとき 型変換={1}")
  @CsvSource({
      "true,true",
      "false,true",
      "abc,false", // true false以外の値は変換不可
      "あいう,false" // true false以外の値は変換不可
  })
  void Boolean型フィールドの型変換可能性を検証(String valueStr, boolean isParsable) {
    // Arrange
    SearchFilter filter = new SearchFilter("isDeleted", SearchOperator.EQ, valueStr, null);
    // 変換不可の場合のみaddErrorが呼ばれるためスタブ設定
    if(!isParsable){
      stubAddError();
    };

    boolean result = sut.isValid(filter, context);

    assertThat(result).isEqualTo(isParsable);
  }



}