package raisetech.student.management.search.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import raisetech.student.management.search.criteria.StudentSearchCriteria;
import raisetech.student.management.search.request.SearchFilter;
import raisetech.student.management.search.request.SearchOperator;
import raisetech.student.management.search.request.StudentAdvancedSearchRequest;
import raisetech.student.management.search.request.StudentSimpleSearchRequest;

class StudentSearchCriteriaConverterTest {

  private StudentSearchCriteriaConverter sut;

  @BeforeEach
  void setUp() {
    sut = new StudentSearchCriteriaConverter();
  }

  // Converterでの正常系テストは、fieldごとのapply*Filter呼び出しルーティングが正常にできているかを検証する。

  @Test
  void fullName_EQをfullNameEqに変換できること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("fullName", SearchOperator.EQ, "山田太郎", null)
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertEquals("山田太郎", actual.getFullNameEq());
  }

  @Test
  void kanaName_EQをkanaNameEqに変換できること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("kanaName", SearchOperator.EQ, "やまだたろう", null)
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertEquals("やまだたろう", actual.getKanaNameEq());
  }

  @Test
  void Nickname_EQをnicknameEqに変換できること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("nickname", SearchOperator.EQ, "タロー", null)
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertEquals("タロー", actual.getNicknameEq());
  }

  @Test
  void email_EQをemailEqに変換できること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("email", SearchOperator.EQ, "taro@example.com", null)
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertEquals("taro@example.com", actual.getEmailEq());
  }

  @Test
  void area_EQをareaEqに変換できること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("area", SearchOperator.EQ, "東京都", null)
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertEquals("東京都", actual.getAreaEq());
  }

  @Test
  void telephone_EQをtelephoneEqに変換できること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("telephone", SearchOperator.EQ, "090-0000-0000", null)
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertEquals("090-0000-0000", actual.getTelephoneEq());
  }

  @Test
  void age_EQをageEqに変換できること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("age", SearchOperator.EQ, "20", null)
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertEquals(20, actual.getAgeEq());
  }

  @Test
  void sex_INをsexInに変換できること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("sex", SearchOperator.IN, null, List.of("男", "女"))
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertIterableEquals(List.of("男", "女"), actual.getSexIn());
  }

  @Test
  void remark_CONTAINSをremarkLikeに変換できること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("remark", SearchOperator.CONTAINS, "転職", null)
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertEquals("%転職%", actual.getRemarkLike());
  }

  @Test
  void isDeleted_EQをisDeletedに変換できること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("isDeleted", SearchOperator.EQ, "true", null)
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertEquals(true, actual.getIsDeleted());
  }

  @Test
  void courseCode_INをcourseCodeInに変換できること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("courseCode", SearchOperator.IN, null, List.of("JA", "PY"))
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertIterableEquals(List.of("JA", "PY"), actual.getCourseCodeIn());
  }

  @Test
  void statusId_INをstatusIdInに変換できること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("statusId", SearchOperator.IN, null, List.of("1", "3", "5"))
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertIterableEquals(List.of(1, 3, 5), actual.getStatusIdIn());
  }

  @ParameterizedTest(name = "[{index}] {0}_EQを {0}Eq に変換できること")
  @MethodSource("dateEqPatterns")
  void 日付フィード_EQを完全一致検索条件に変換できること(
      String fieldName, Function<StudentSearchCriteria, LocalDate> getter) {
    List<SearchFilter> filters = List.of(
        new SearchFilter(fieldName, SearchOperator.EQ, "2026-04-01", null)
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertEquals(LocalDate.of(2026, 4, 1), getter.apply(actual));
  }

  private static Stream<Arguments> dateEqPatterns() {
    return Stream.of(
        Arguments.of("courseApplyAt", (Function<StudentSearchCriteria, LocalDate>) StudentSearchCriteria::getCourseApplyAtEq),
        Arguments.of("courseStartAt", (Function<StudentSearchCriteria, LocalDate>) StudentSearchCriteria::getCourseStartAtEq),
        Arguments.of("coursePlannedEndAt", (Function<StudentSearchCriteria, LocalDate>) StudentSearchCriteria::getCoursePlannedEndAtEq),
        Arguments.of("courseFinishedAt", (Function<StudentSearchCriteria, LocalDate>) StudentSearchCriteria::getCourseFinishedAtEq)
    );
  }

  @Test
  void 複数フィルタをまとめて変換できること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("fullName", SearchOperator.CONTAINS, "山田", null),
        new SearchFilter("courseCode", SearchOperator.IN, null, List.of("JA", "JS")),
        new SearchFilter("statusId", SearchOperator.EQ, "2", null),
        new SearchFilter("isDeleted", SearchOperator.EQ, "false", null)
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertEquals("%山田%", actual.getFullNameLike());
    assertIterableEquals(List.of("JA", "JS"), actual.getCourseCodeIn());
    assertEquals(2, actual.getStatusIdEq());
    assertEquals(false, actual.getIsDeleted());
  }

  @Test
  void 不正なfield名のとき例外を投げること() {
    List<SearchFilter> filters = List.of(
        new SearchFilter("unknownField", SearchOperator.EQ, "x", null)
    );
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    // SearchableFieldインスタンスを取得するときに失敗してIllegalArgumentExceptionが投げられる
    assertThrows(IllegalArgumentException.class, () -> sut.toCriteria(input));
  }

  @Test
  void 空のSearchFilterリストを受け取ったとき空のcriteriaを返すこと(){
    List<SearchFilter> filters = new ArrayList<>();
    StudentAdvancedSearchRequest input = new StudentAdvancedSearchRequest(filters);

    StudentSearchCriteria expected = new StudentSearchCriteria();
    StudentSearchCriteria actual = sut.toCriteria(input);

    assertEquals(expected, actual);
  }

  @Test
  void StudentSimpleSearchRequestをcriteriaに変換できること(){
    StudentSimpleSearchRequest input = new StudentSimpleSearchRequest();
    input.setFullNameContains("佐藤");
    input.setAgeMax(40);
    input.setSexEq("女");
    input.setApplyFrom(LocalDate.of(2025,1,1));

    StudentSearchCriteria actual = sut.toCriteria(input);

    assertEquals("%佐藤%", actual.getFullNameLike());
    assertEquals(40, actual.getAgeMax());
    assertEquals("女", actual.getSexEq());
    assertEquals(LocalDate.of(2025,1,1), actual.getCourseApplyAtFrom());
  }

  @Test
  void 空のStudentSimpleSearchRequestを空のcriteriaに変換できること(){
    StudentSearchCriteria actual = sut.toCriteria(new StudentSimpleSearchRequest());

    StudentSearchCriteria expected = new StudentSearchCriteria();

    assertEquals(expected,actual);

  }
}