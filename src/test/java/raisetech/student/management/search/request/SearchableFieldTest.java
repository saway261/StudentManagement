package raisetech.student.management.search.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SearchableFieldTest {

  @Test
  void exists_fieldNameとしてnullが渡されたときfalseを返すこと() {
    assertFalse(SearchableField.exists(null));
  }

  @ParameterizedTest(name = "[{index}] fieldNameとして{0}文字列が渡されたとき{1}を返すこと")
  @CsvSource({
      "fullName,true",
      "age,true",
      "courseApplyAt,true",
      "isDeleted,true",
      "studentId,false",
      "full_name,false",
      "unknown,false"
  })
  void exists_fieldNameとして存在する値としない値を渡して適切に真偽値を返すかのテスト(String fieldName, boolean expectExists) {
    assertThat(SearchableField.exists(fieldName)).isEqualTo(expectExists);
  }



  @Test
  void fromFieldName_有効なFieldNameに対応するインスタンスを返すこと() {
    assertEquals(SearchableField.FULL_NAME, SearchableField.fromFieldName("fullName"));
    assertEquals(SearchableField.AGE, SearchableField.fromFieldName("age"));
    assertEquals(SearchableField.COURSE_APPLY_AT, SearchableField.fromFieldName("courseApplyAt"));
    assertEquals(SearchableField.IS_DELETED, SearchableField.fromFieldName("isDeleted"));
  }

  @Test
  void fromFieldName_存在しないFieldNameが渡されたときIllegalArgumentExceptionを投げること() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class,
            () -> SearchableField.fromFieldName("invalidField"));
  }

  @Test
  void fromFieldName_nullが渡されたときIllegalArgumentExceptionを投げること() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class,
            () -> SearchableField.fromFieldName(null));
  }

  @Test
  void typeOf_fieldNameに対応する型を返すこと() {
    assertEquals(String.class, SearchableField.typeOf("fullName"));
    assertEquals(String.class, SearchableField.typeOf("courseCode"));

    assertEquals(Integer.class, SearchableField.typeOf("age"));
    assertEquals(Integer.class, SearchableField.typeOf("statusId"));

    assertEquals(Boolean.class, SearchableField.typeOf("isDeleted"));

    assertEquals(LocalDate.class, SearchableField.typeOf("courseApplyAt"));
    assertEquals(LocalDate.class, SearchableField.typeOf("courseFinishedAt"));
  }

  @Test
  void typeOf_無効なfieldNameが渡されたときIllegalArgumentExceptionを投げること() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class,
            () -> SearchableField.typeOf("invalidField"));

  }

  @Test
  void getAllFieldNames_件数がenum定数数と一致すること() {
    List<String> actual = SearchableField.getAllFieldNames();

    assertEquals(SearchableField.values().length, actual.size());
  }
}