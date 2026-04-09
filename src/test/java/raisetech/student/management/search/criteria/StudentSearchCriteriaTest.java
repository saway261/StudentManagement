package raisetech.student.management.search.criteria;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import raisetech.student.management.exception.InvalidSearchCriteriaException;
import raisetech.student.management.search.request.SearchFilter;
import raisetech.student.management.search.request.SearchOperator;
import raisetech.student.management.search.request.StudentSimpleSearchRequest;

class StudentSearchCriteriaTest {

  private StudentSearchCriteria sut;

  private static final Map<String, CriteriaGetter> GETTER_MAP;

  private static final Map<String, FilterApplier> APPLIER_MAP;

  static {
    Map<String, CriteriaGetter> map = new HashMap<>();

    // フィールド名:サフィックス の形式で登録
    // 氏名 (Full Name)
    map.put("fullName:Eq", StudentSearchCriteria::getFullNameEq);
    map.put("fullName:Like", StudentSearchCriteria::getFullNameLike);

    // 氏名よみがな (Kana Name)
    map.put("kanaName:Eq", StudentSearchCriteria::getKanaNameEq);
    map.put("kanaName:Like", StudentSearchCriteria::getKanaNameLike);

    // ニックネーム (Nickname)
    map.put("nickname:Eq", StudentSearchCriteria::getNicknameEq);
    map.put("nickname:Like", StudentSearchCriteria::getNicknameLike);

    // Email
    map.put("email:Eq", StudentSearchCriteria::getEmailEq);
    map.put("email:Like", StudentSearchCriteria::getEmailLike);

    // 地域 (Area)
    map.put("area:Eq", StudentSearchCriteria::getAreaEq);
    map.put("area:Like", StudentSearchCriteria::getAreaLike);

    // 電話番号 (Telephone)
    map.put("telephone:Eq", StudentSearchCriteria::getTelephoneEq);
    map.put("telephone:Like", StudentSearchCriteria::getTelephoneLike);

    // 年齢 (Age)
    map.put("age:Eq", StudentSearchCriteria::getAgeEq);
    map.put("age:Min", StudentSearchCriteria::getAgeMin);
    map.put("age:Max", StudentSearchCriteria::getAgeMax);

    // 性別 (Sex)
    map.put("sex:Eq", StudentSearchCriteria::getSexEq);
    map.put("sex:In", StudentSearchCriteria::getSexIn);

    // 備考 (Remark)
    map.put("remark:Eq", StudentSearchCriteria::getRemarkEq);
    map.put("remark:Like", StudentSearchCriteria::getRemarkLike);

    // 削除フラグ (isDeleted)
    map.put("isDeleted:Eq", StudentSearchCriteria::getIsDeleted);

    // コースコード (Course Code)
    map.put("courseCode:Eq", StudentSearchCriteria::getCourseCodeEq);
    map.put("courseCode:In", StudentSearchCriteria::getCourseCodeIn);

    // ステータスID (Status ID)
    map.put("statusId:Eq", StudentSearchCriteria::getStatusIdEq);
    map.put("statusId:In", StudentSearchCriteria::getStatusIdIn);

    // 申込日 (Course Apply At)
    map.put("courseApplyAt:Eq", StudentSearchCriteria::getCourseApplyAtEq);
    map.put("courseApplyAt:From", StudentSearchCriteria::getCourseApplyAtFrom);
    map.put("courseApplyAt:To", StudentSearchCriteria::getCourseApplyAtTo);

    // 受講開始日 (Course Start At)
    map.put("courseStartAt:Eq", StudentSearchCriteria::getCourseStartAtEq);
    map.put("courseStartAt:From", StudentSearchCriteria::getCourseStartAtFrom);
    map.put("courseStartAt:To", StudentSearchCriteria::getCourseStartAtTo);

    // 受講終了予定日 (Course Planned End At)
    map.put("coursePlannedEndAt:Eq", StudentSearchCriteria::getCoursePlannedEndAtEq);
    map.put("coursePlannedEndAt:From", StudentSearchCriteria::getCoursePlannedEndAtFrom);
    map.put("coursePlannedEndAt:To", StudentSearchCriteria::getCoursePlannedEndAtTo);

    // 受講終了実績日 (Course Finished At)
    map.put("courseFinishedAt:Eq", StudentSearchCriteria::getCourseFinishedAtEq);
    map.put("courseFinishedAt:From", StudentSearchCriteria::getCourseFinishedAtFrom);
    map.put("courseFinishedAt:To", StudentSearchCriteria::getCourseFinishedAtTo);

    GETTER_MAP = Collections.unmodifiableMap(map);
  }

  static {
    Map<String, FilterApplier> map = new HashMap<>();

    // 基本情報
    map.put("fullName", StudentSearchCriteria::applyFullNameFilter);
    map.put("kanaName", StudentSearchCriteria::applyKanaNameFilter);
    map.put("nickname", StudentSearchCriteria::applyNicknameFilter);
    map.put("email", StudentSearchCriteria::applyEmailFilter);
    map.put("area", StudentSearchCriteria::applyAreaFilter);
    map.put("telephone", StudentSearchCriteria::applyTelephoneFilter);
    map.put("age", StudentSearchCriteria::applyAgeFilter);
    map.put("sex", StudentSearchCriteria::applySexFilter);
    map.put("remark", StudentSearchCriteria::applyRemarkFilter);
    map.put("isDeleted", StudentSearchCriteria::applyIsDeletedFilter);

    // 受講・コース情報
    map.put("courseCode", StudentSearchCriteria::applyCourseCodeFilter);
    map.put("statusId", StudentSearchCriteria::applyStatusIdFilter);
    map.put("courseApplyAt", StudentSearchCriteria::applyCourseApplyAtFilter);
    map.put("courseStartAt", StudentSearchCriteria::applyCourseStartAtFilter);
    map.put("coursePlannedEndAt", StudentSearchCriteria::applyCoursePlannedEndAtFilter);
    map.put("courseFinishedAt", StudentSearchCriteria::applyCourseFinishedAtFilter);

    APPLIER_MAP = Collections.unmodifiableMap(map);
  }

  @FunctionalInterface
  private interface CriteriaGetter {
    /**
     * @param criteria 検索条件オブジェクト
     * @return 保持されている値
     */
    Object get(StudentSearchCriteria criteria);
  }

  @FunctionalInterface
  private interface FilterApplier {
    void apply(StudentSearchCriteria criteria, SearchFilter filter);
  }

  // 3. 補助的な呼び出しメソッド
  // 戻り値を T (呼び出し側が期待する型) にする
  @SuppressWarnings("unchecked")
  private <T> T getValue(StudentSearchCriteria criteria, String fieldName, String suffix) {
    String key = fieldName + ":" + suffix;
    Object value = Optional.ofNullable(GETTER_MAP.get(key))
        .map(g -> g.get(criteria))
        .orElseThrow(() -> new AssertionError("定義されていないプロパティ: " + key));

    return (T) value; // ここでキャスト
  }

  private void applyFilter(StudentSearchCriteria criteria, SearchFilter filter) {
    FilterApplier applier = APPLIER_MAP.get(filter.getField());
    if (applier == null) {
      throw new IllegalArgumentException("未定義のフィルタフィールドです: " + filter.getField());
    }
    applier.apply(criteria, filter);
  }

  @BeforeEach
  void setUp() {
    sut = new StudentSearchCriteria();
  }

  @ParameterizedTest(name = "[{index}] apply{0}Filter_EQで {0}Eqにセットできること")
  @ValueSource(strings = {
      "fullName",
      "kanaName",
      "nickname",
      "email",
      "area",
      "telephone",
      "sex",
      "remark",
      "courseCode"
  })
  void String型フィールド_EQで完全一致条件にセットできること(String fieldName) {
    SearchFilter filter = new SearchFilter(fieldName, SearchOperator.EQ, "あ", null);

    applyFilter(sut,filter);

    assertEquals("あ", getValue(sut,fieldName,"Eq"));
  }

  @ParameterizedTest(name = "[{index}] apply{0}Filter_{1}が許可されている場合は {0}Likeにセットでき、許可されていない場合は例外を送出すること")
  @CsvSource({
      "fullName,STARTS_WITH,true",
      "fullName,ENDS_WITH,true",
      "fullName,CONTAINS,true",
      "kanaName,STARTS_WITH,true",
      "kanaName,ENDS_WITH,true",
      "kanaName,CONTAINS,true",
      "nickname,STARTS_WITH,true",
      "nickname,ENDS_WITH,true",
      "nickname,CONTAINS,true",
      "email,STARTS_WITH,true",
      "email,ENDS_WITH,true",
      "email,CONTAINS,true",
      "area,STARTS_WITH,true",
      "area,ENDS_WITH,true",
      "area,CONTAINS,true",
      "telephone,STARTS_WITH,true",
      "telephone,ENDS_WITH,true",
      "telephone,CONTAINS,false",
      "sex,STARTS_WITH,false",
      "sex,ENDS_WITH,false",
      "sex,CONTAINS,false",
      "remark,STARTS_WITH,false",
      "remark,ENDS_WITH,false",
      "remark,CONTAINS,true",
      "courseCode,STARTS_WITH,false",
      "courseCode,ENDS_WITH,false",
      "courseCode,CONTAINS,false"
  })
  void String型フィールド_部分一致演算子が許可されている場合のみ部分一致条件にセットできること(String fieldName,SearchOperator operator,boolean isAllowed) {
    SearchFilter filter = new SearchFilter(fieldName, operator, "あ", null);

    String expectValue = switch (operator){
      case STARTS_WITH ->  "あ%";
      case ENDS_WITH ->  "%あ";
      case CONTAINS ->  "%あ%";
      default -> "";
    };

    if(isAllowed){
      applyFilter(sut,filter);
      assertEquals(expectValue, getValue(sut,fieldName,"Like"));
    }else{
      assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter));
    }
  }

  @ParameterizedTest(name = "[{index}] apply{0}Filter_INが許可されている場合は {0}Inにセットでき、許可されていない場合は例外を送出すること")
  @CsvSource({
      "fullName,false",
      "kanaName,false",
      "nickname,false",
      "email,false",
      "area,false",
      "telephone,false",
      "sex,true",
      "remark,false",
      "courseCode,true"
  })
  void String型フィールド_INが許可されている場合のみリスト検索条件にセットできること(String fieldName,boolean isAllowed){
    SearchFilter filter = new SearchFilter(fieldName, SearchOperator.IN, null, List.of("あ","い"));

    if(isAllowed){
      applyFilter(sut,filter);
      assertIterableEquals(List.of("あ","い"), getValue(sut,fieldName,"In"));
    }else{
      assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter));
    }
  }

  @ParameterizedTest(name = "[{index}] apply{0}Filter_EQを重複指定したときに例外を投げること")
  @ValueSource(strings = {
      "fullName",
      "kanaName",
      "nickname",
      "email",
      "area",
      "telephone",
      "sex",
      "remark",
      "courseCode"
  })
  void String型フィールド_EQを重複指定したとき例外を投げること(String fieldName) {
    SearchFilter filter1  = new SearchFilter(fieldName, SearchOperator.EQ, "あいう", null);
    assertDoesNotThrow(() -> applyFilter(sut, filter1));

    SearchFilter filter2 = new SearchFilter(fieldName, SearchOperator.EQ, "えお", null);

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter2));
  }

  @ParameterizedTest(name = "[{index}] apply{0}Filter_部分検索演算子を重複指定したときに例外を投げること")
  @CsvSource({// 第2引数には代表的な部分検索演算子を１つ指定する
      "fullName,STARTS_WITH",
      "kanaName,CONTAINS",
      "nickname,ENDS_WITH",
      "email,STARTS_WITH",
      "area,CONTAINS",
      "telephone,STARTS_WITH",
      "remark,CONTAINS",
  })
  void String型フィールド_部分条件演算子を重複指定したとき例外を投げること(String fieldName, SearchOperator operator) {
    SearchFilter filter1  = new SearchFilter(fieldName, operator, "あいう", null);
    assertDoesNotThrow(() -> applyFilter(sut, filter1));

    SearchFilter filter2 = new SearchFilter(fieldName, operator, "えお", null);

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter2));
  }

  @ParameterizedTest(name = "[{index}] apply{0}Filter_完全一致検索と部分一致検索を併用したときに例外を投げること")
  @CsvSource({// 第2引数には代表的な部分検索演算子を１つ指定する
      "fullName,STARTS_WITH",
      "kanaName,CONTAINS",
      "nickname,ENDS_WITH",
      "email,STARTS_WITH",
      "area,CONTAINS",
      "telephone,STARTS_WITH",
      "remark,CONTAINS",
  })
  void String型フィールド_完全一致検索と部分一致検索を併用したとき例外を投げること(String fieldName, SearchOperator operator) {
    SearchFilter filter1  = new SearchFilter(fieldName, SearchOperator.EQ, "あいう", null);
    assertDoesNotThrow(() -> applyFilter(sut, filter1));

    SearchFilter filter2 = new SearchFilter(fieldName, operator, "えお", null);

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter2));
  }

  @ParameterizedTest(name = "[{index}] apply{0}Filter_INを重複指定したときに例外を投げること")
  @ValueSource(strings = {"sex","courseCode"})
  void String型フィールド_INを重複指定したとき例外を投げること(String fieldName) {
    SearchFilter filter1  = new SearchFilter(fieldName, SearchOperator.IN, null,List.of("あ","い"));
    assertDoesNotThrow(() -> applyFilter(sut, filter1));

    SearchFilter filter2 = new SearchFilter(fieldName, SearchOperator.IN, null,List.of("う","え"));

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter2));
  }

  @ParameterizedTest(name = "[{index}] apply{0}Filter_完全一致検索とリスト検索を併用したときに例外を投げること")
  @ValueSource(strings = {"sex","courseCode"})
  void String型フィールド_完全一致検索とリスト検索を併用したとき例外を投げること(String fieldName) {
    SearchFilter filter1  = new SearchFilter(fieldName, SearchOperator.EQ, "あ", null);
    assertDoesNotThrow(() -> applyFilter(sut, filter1));

    SearchFilter filter2 = new SearchFilter(fieldName, SearchOperator.IN,null, List.of("い","う"));

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter2));
  }

  @ParameterizedTest(name = "[{index}] applyAgeFilter_{0}で に適切なプロパティセットできること")
  @ValueSource(strings = {"EQ","GTE","LTE"})
  void Integer型フィールドage_演算子によって適切なプロパティにセットできること(SearchOperator operator) {
    SearchFilter filter = new SearchFilter("age", operator, "20", null);

    applyFilter(sut,filter);

    String suffix = switch (operator){
      case EQ -> "Eq";
      case GTE -> "Min";
      case LTE -> "Max";
      default -> "";
    };
    Integer actual = getValue(sut,"age",suffix);
    assertEquals(20, actual);
  }

  @Test
  void Integer型フィールドage_BETWEENで小さい方をageMin大きい方をageMaxにセットできること() {
    SearchFilter filter = new SearchFilter("age", SearchOperator.BETWEEN, null, List.of("30", "18"));

    applyFilter(sut,filter);

    Integer actualMin = getValue(sut,"age","Min");
    Integer actualMax = getValue(sut,"age","Max");
    assertEquals(18, actualMin);
    assertEquals(30, actualMax);
  }

  @Test
  void Integer型フィールドage_BETWEENで同値2件のとき例外を投げること() {
    SearchFilter filter = new SearchFilter("age", SearchOperator.BETWEEN, null, List.of("20", "20"));

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter));
  }

  @ParameterizedTest(name = "[{index}] applyAgeFilter_{0}は許可されず例外を投げること")
  @ValueSource(strings = {"CONTAINS","STARTS_WITH","ENDS_WITH","IN"})
  void Integer型フィールドage_許可されていない演算子を指定されたら例外を投げること(SearchOperator operator) {
    SearchFilter filter = new SearchFilter("age", operator, "20", null);

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter));
  }

  @ParameterizedTest(name = "[{index}] applyAgeFilter_{0}を重複指定したとき例外を投げること")
  @ValueSource(strings = {"EQ","GTE","LTE"})
  void Integer型フィールドage_EQ_GTE_LTEで同じ検索条件プロパティを重複指定したとき例外を投げること(
      SearchOperator operator) {
    SearchFilter filter1 = new SearchFilter("age", operator, "1", null);
    assertDoesNotThrow(() -> applyFilter(sut, filter1));

    SearchFilter filter2 = new SearchFilter("age", operator, "2", null);

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut, filter2));
  }

  @Test
  void Integer型フィールドage_完全一致検索と範囲検索を併用したとき例外を投げること() {
    SearchFilter filter1 = new SearchFilter("age", SearchOperator.EQ, "1", null);
    assertDoesNotThrow(() -> applyFilter(sut, filter1));

    SearchFilter filter2 = new SearchFilter("age", SearchOperator.GTE, "2", null);

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut, filter2));
  }

  @Test
  void Integer型フィールドstatusId_EQでstatusIdEqにセットできること() {
    SearchFilter filter = new SearchFilter("statusId", SearchOperator.EQ, "2", null);

    applyFilter(sut,filter);

    Integer actual = getValue(sut,"statusId","Eq");
    assertEquals(2, actual);
  }

  @Test
  void Integer型フィールドstatusId_INでstatusIdInにセットできること() {
    SearchFilter filter = new SearchFilter("statusId", SearchOperator.IN, null, List.of("1", "3", "5"));

    applyFilter(sut,filter);

    List<Integer> actual = getValue(sut,"statusId","In");
    assertIterableEquals(List.of(1, 3, 5),actual);
  }

  @ParameterizedTest(name = "[{index}] applyStatusIdFilter_{0}は許可されず例外を投げること")
  @ValueSource(strings = {"CONTAINS","STARTS_WITH","ENDS_WITH","GTE","LTE","BETWEEN"})
  void Integer型フィールドstatusId_許可されていない演算子を指定されたら例外を投げること(SearchOperator operator) {
    SearchFilter filter = new SearchFilter("statusId", operator, "1", List.of("1", "2"));

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter));
  }

  @Test
  void Integer型フィールドstatusId_EQを重複指定したとき例外を投げること() {
    SearchFilter filter1 = new SearchFilter("statusId", SearchOperator.EQ, "1", null);
    assertDoesNotThrow(() -> applyFilter(sut, filter1));

    SearchFilter filter2 = new SearchFilter("statusId", SearchOperator.EQ, "2", null);

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut, filter2));
  }

  @Test
  void Integer型フィールドstatusId_INを重複指定したとき例外を投げること() {
    SearchFilter filter1 = new SearchFilter("statusId", SearchOperator.IN, null, List.of("1","2"));
    assertDoesNotThrow(() -> applyFilter(sut, filter1));

    SearchFilter filter2 = new SearchFilter("statusId", SearchOperator.IN, null, List.of("3","4"));

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut, filter2));
  }

  @Test
  void Integer型フィールドstatusId_完全一致検索とリスト検索を併用したとき例外を投げること() {
    SearchFilter filter1 = new SearchFilter("statusId", SearchOperator.EQ, "1", null);
    assertDoesNotThrow(() -> applyFilter(sut, filter1));

    SearchFilter filter2 = new SearchFilter("statusId", SearchOperator.IN, null, List.of("2","3"));

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut, filter2));
  }

  @ParameterizedTest(name = "[{index}] applyIsDeletedFilter_{0}は許可されず例外をなげること")
  @ValueSource(strings = {"CONTAINS","STARTS_WITH","ENDS_WITH","IN","GTE","LTE","BETWEEN"})
  void boolean型フィールドisDeleted_EQ以外の演算子が指定された場合例外を投げること(SearchOperator operator){
    SearchFilter filter = new SearchFilter("isDeleted", operator, "true", null);

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter));
  }

  @ParameterizedTest(name = "[{index}] applyIsDeletedFilter_EQで文字列{0}がbool値{0}に変換されisDeletedにセットできること")
  @ValueSource(strings = {"true","false"})
  void boolean型フィールドisDeleted_EQ_trueでisDeletedに値をセットできること(String value) {
    SearchFilter filter = new SearchFilter("isDeleted", SearchOperator.EQ, value, null);

    applyFilter(sut,filter);

    Boolean expect = switch(value){
      case "true" -> true;
      case "false" -> false;
      default -> null;
    };

    assertEquals(expect, getValue(sut,"isDeleted","Eq"));
  }

  @Test
  void boolean型フィールドisDeleted_EQを重複指定したとき例外を投げること() {
    SearchFilter filter1  = new SearchFilter("isDeleted", SearchOperator.EQ, "true", null);
    assertDoesNotThrow(() -> applyFilter(sut, filter1));

    SearchFilter filter2 = new SearchFilter("isDeleted", SearchOperator.EQ, "false", null);

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter2));
  }

  @ParameterizedTest(name = "[{index}] apply{0}Filter_{1}で対応するプロパティにセットできること")
  @CsvSource({
      "courseApplyAt,EQ",
      "courseApplyAt,GTE",
      "courseApplyAt,LTE",
      "courseStartAt,EQ",
      "courseStartAt,GTE",
      "courseStartAt,LTE",
      "coursePlannedEndAt,EQ",
      "coursePlannedEndAt,GTE",
      "coursePlannedEndAt,LTE",
      "courseFinishedAt,EQ",
      "courseFinishedAt,GTE",
      "courseFinishedAt,LTE"
  })
  void 日付型フィールド_演算子に対応するプロパティにセットできること(String fieldName,SearchOperator operator) {
    SearchFilter filter = new SearchFilter(fieldName, operator, "2026-04-01", null);

    applyFilter(sut,filter);

    String suffix = switch (operator){
      case EQ -> "Eq";
      case GTE -> "From";
      case LTE -> "To";
      default -> "";
    };
    assertEquals(LocalDate.of(2026,4,1), getValue(sut,fieldName,suffix));
  }

  @ParameterizedTest(name = "[{index}] apply{0}Filter_BETWEENでFromとToに昇順でセットできること")
  @ValueSource(strings = {"courseApplyAt","courseStartAt","coursePlannedEndAt","courseFinishedAt"})
  void 日付型フィールド_BETWEENで対応するFromToプロパティにセットできること(String fieldName) {
    SearchFilter filter = new SearchFilter(fieldName, SearchOperator.BETWEEN, null,
        List.of("2026-04-10", "2026-04-01"));

    applyFilter(sut,filter);

    assertEquals(LocalDate.of(2026, 4, 1), getValue(sut,fieldName,"From"));
    assertEquals(LocalDate.of(2026, 4, 10), getValue(sut,fieldName,"To"));
  }

  @ParameterizedTest(name = "[{index}] apply{0}Filter_BETWEENで同値2件のとき例外を投げること")
  @ValueSource(strings = {"courseApplyAt","courseStartAt","coursePlannedEndAt","courseFinishedAt"})
  void 日付型フィールド_BETWEENで同値2件のとき例外を投げること(String fieldName) {
    SearchFilter filter = new SearchFilter(fieldName, SearchOperator.BETWEEN, null,
        List.of("2026-04-01", "2026-04-01"));

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter));
  }

  @ParameterizedTest(name = "[{index}] apply{0}Filter_{1}を重複指定したとき例外を投げること")
  @CsvSource({
      "courseApplyAt,EQ",
      "courseApplyAt,GTE",
      "courseApplyAt,LTE",
      "courseStartAt,EQ",
      "courseStartAt,GTE",
      "courseStartAt,LTE",
      "coursePlannedEndAt,EQ",
      "coursePlannedEndAt,GTE",
      "coursePlannedEndAt,LTE",
      "courseFinishedAt,EQ",
      "courseFinishedAt,GTE",
      "courseFinishedAt,LTE"
  })
  void 日付型フィールド_EQ_GTE_LTEで同じ検索条件プロパティを重複指定したとき例外を投げること(String fieldName, SearchOperator operator){
    SearchFilter filter1 = new SearchFilter(fieldName,operator, "2026-04-01", null);
    assertDoesNotThrow(() -> applyFilter(sut, filter1));

    SearchFilter filter2 = new SearchFilter(fieldName, operator, "2026-04-02", null);

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter2));
  }

  @ParameterizedTest(name = "[{index}] {0} EQとGTEを併用したとき例外を投げること")
  @ValueSource(strings = {"courseApplyAt","courseStartAt","coursePlannedEndAt","courseFinishedAt"})
  void 日付型フィールド_EQとGTEを併用したとき例外を投げること(String fieldName) {
    SearchFilter filter1 = new SearchFilter(fieldName, SearchOperator.EQ, "2026-04-01", null);
    assertDoesNotThrow(() -> applyFilter(sut, filter1));

    SearchFilter filter2 = new SearchFilter(fieldName, SearchOperator.GTE, "2026-04-02", null);

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter2));
  }

  @ParameterizedTest(name = "[{index}] {0} CONTAINSは許可されず例外を投げること")
  @ValueSource(strings = {"courseApplyAt","courseStartAt","coursePlannedEndAt","courseFinishedAt"})
  void 日付型フィールド_許可されていない演算子のとき例外を投げること(String fieldName) {
    SearchFilter filter = new SearchFilter(fieldName, SearchOperator.CONTAINS, "2026-04", null);

    assertThrows(InvalidSearchCriteriaException.class, () -> applyFilter(sut,filter));
  }

  @Test
  void StudentSimpleSearchRequestを受け取り適切なフィールドをだけをセットしたcriteriaに変換できること(){
    StudentSimpleSearchRequest input = new StudentSimpleSearchRequest();
    input.setAreaContains("東京");
    input.setStatusId(List.of(1,2));

    StudentSearchCriteria actual = new StudentSearchCriteria(input);

    assertEquals("%東京%", actual.getAreaLike());
    assertEquals(List.of(1,2), actual.getStatusIdIn());

    // 上記以外のフィールドが全てnullであることを検証
    Set<String> expectedKeys = Set.of(
        "area:Like",
        "statusId:In"
    );

    GETTER_MAP.forEach((key, getter) -> {
      // expectedKeysに含まれていないフィールドだけをチェック
      if (!expectedKeys.contains(key)) {
        Object value = getter.get(actual);
        assertNull(value);
      }
    });
  }

  @Test
  void 空のStudentSimpleSearchRequestを受け取ると全てのフィールドがnullのcriteriaに変換できること(){
    StudentSearchCriteria actual = new StudentSearchCriteria(new StudentSimpleSearchRequest());

    GETTER_MAP.forEach((key, getter) -> {
      Object value = getter.get(actual);
      assertNull(value);
    });
  }

}