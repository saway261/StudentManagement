package raisetech.student.management.search.criteria;

import static raisetech.student.management.search.request.SearchOperator.EQ;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raisetech.student.management.search.request.SearchFilter;
import raisetech.student.management.search.request.SearchOperator;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class StudentSearchCriteria {

  /** 氏名 完全一致 */
  private String fullNameEq;

  /** 氏名 LIKE検索用。例: "田中%", "%田中", "%田中%" */
  private String fullNameLike;

  /** 氏名よみがな 完全一致 */
  private String kanaNameEq;

  /** 氏名よみがな LIKE検索用*/
  private String kanaNameLike;

  /** ニックネーム 完全一致 */
  private String nicknameEq;

  /** ニックネーム LIKE検索用*/
  private String nicknameLike;

  /** コースコード 完全一致 */
  private String courseCodeEq;

  /** コースコード IN */
  private List<String> courseCodeIn;

  /** ステータスID 完全一致 */
  private Integer statusIdEq;

  /** ステータスID IN */
  private List<Integer> statusIdIn;

  /** 年齢 完全一致 */
  private Integer ageEq;

  /** 年齢 下限 */
  private Integer ageMin;

  /** 年齢 上限 */
  private Integer ageMax;

  /** 申込日 完全一致 */
  private LocalDate courseApplyAtEq;

  /** 申込日 下限 */
  private LocalDate courseApplyAtFrom;

  /** 申込日 上限 */
  private LocalDate courseApplyAtTo;

  /** 削除フラグ */
  private Boolean isDeleted;


  /**====================
   * 再代入不可のsetter群
   *=====================*/

  private void setFullNameEq(String fullNameEq) {
    if(this.fullNameEq != null){
      throw new IllegalStateException("fullNameEq は既に設定されています。重複指定はできません。");
    }
    this.fullNameEq = fullNameEq;
  }

  private void setFullNameLike(String fullNameLike) {
    if(this.fullNameLike != null){
      throw new IllegalStateException("fullNameLike は既に設定されています。重複指定はできません。");
    }
    this.fullNameLike = fullNameLike;
  }

  private void setKanaNameEq(String kanaNameEq) {
    if(this.kanaNameEq != null){
      throw new IllegalStateException("kanaNameEq は既に設定されています。重複指定はできません。");
    }
    this.kanaNameEq = kanaNameEq;
  }

  private void setKanaNameLike(String kanaNameLike) {
    if(this.kanaNameLike != null){
      throw new IllegalStateException("kanaNameLike は既に設定されています。重複指定はできません。");
    }
    this.kanaNameLike = kanaNameLike;
  }

  private void setNicknameEq(String nicknameEq) {
    if(this.nicknameEq != null){
      throw new IllegalStateException("nicknameEq は既に設定されています。重複指定はできません。");
    }
    this.nicknameEq = nicknameEq;
  }

  private void setNicknameLike(String nicknameLike) {
    if(this.nicknameLike != null){
      throw new IllegalStateException("nicknameLike は既に設定されています。重複指定はできません。");
    }
    this.nicknameLike = nicknameLike;
  }

  private void setCourseCodeEq(String courseCodeEq) {
    if(this.courseCodeEq != null){
      throw new IllegalStateException("courseCodeEq は既に設定されています。重複指定はできません。");
    }
    this.courseCodeEq = courseCodeEq;
  }

  private void setCourseCodeIn(List<String> courseCodeIn) {
    if(this.courseCodeIn != null){
      throw new IllegalStateException("courseCodeIn は既に設定されています。重複指定はできません。");
    }
    this.courseCodeIn = courseCodeIn;
  }

  private void setStatusIdEq(Integer statusIdEq) {
    if(this.statusIdEq != null){
      throw new IllegalStateException("statusIdEq は既に設定されています。重複指定はできません。");
    }
    this.statusIdEq = statusIdEq;
  }

  private void setStatusIdIn(List<Integer> statusIdIn) {
    if(this.statusIdIn != null){
      throw new IllegalStateException("statusIdIn は既に設定されています。重複指定はできません。");
    }
    this.statusIdIn = statusIdIn;
  }

  private void setAgeEq(Integer ageEq) {
    if(this.ageEq != null){
      throw new IllegalStateException("ageEq は既に設定されています。重複指定はできません。");
    }
    this.ageEq = ageEq;
  }

  private void setAgeMin(Integer ageMin) {
    if(this.ageMin != null){
      throw new IllegalStateException("ageMin は既に設定されています。重複指定はできません。");
    }
    this.ageMin = ageMin;
  }

  private void setAgeMax(Integer ageMax) {
    if(this.ageMax != null){
      throw new IllegalStateException("ageMax は既に設定されています。重複指定はできません。");
    }
    this.ageMax = ageMax;
  }

  private void setCourseApplyAtEq(LocalDate courseApplyAtEq) {
    if(this.courseApplyAtEq != null){
      throw new IllegalStateException("courseApplyAtEq は既に設定されています。重複指定はできません。");
    }
    this.courseApplyAtEq = courseApplyAtEq;
  }

  private void setCourseApplyAtFrom(LocalDate courseApplyAtFrom) {
    if(this.courseApplyAtFrom != null){
      throw new IllegalStateException("courseApplyAtFrom は既に設定されています。重複指定はできません。");
    }
    this.courseApplyAtFrom = courseApplyAtFrom;
  }

  private void setCourseApplyAtTo(LocalDate courseApplyAtTo) {
    if(this.courseApplyAtTo != null){
      throw new IllegalStateException("courseApplyAtTo は既に設定されています。重複指定はできません。");
    }
    this.courseApplyAtTo = courseApplyAtTo;
  }

  private void setDeleted(Boolean deleted) {
    if(this.isDeleted != null){
      throw new IllegalStateException("isDeleted は既に設定されています。重複指定はできません。");
    }
    this.isDeleted = deleted;
  }

  /**====================
   * 検索対象カラムごとの整合性を保ちつつ
   * 値をフィールドにセットするメソッド群
   *=====================*/

  public void applyFullNameFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();
    String text = filter.getValue();

    switch (operator) {
      case EQ-> {
        setFullNameEq(text);
      }
      case STARTS_WITH -> {
        setFullNameLike(text + "%");
      }
      case ENDS_WITH -> {
        setFullNameLike("%" + text);
      }
      case CONTAINS -> {
        setFullNameLike("%" + text + "%");
      }
      default -> throw new IllegalArgumentException("fullName に指定できない operator です: " + operator);
    }

    if (this.getFullNameEq() != null && this.getFullNameLike() != null) {
      throw new IllegalStateException("fullName EQ(完全一致) と STARTS_WITH,ENDS_WITH,CONTAINS(部分一致) は併用できません");
    }
  }

  public void applyKanaNameFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();
    String text = filter.getValue();

    switch (operator) {
      case EQ-> {
        setKanaNameEq(text);
      }
      case STARTS_WITH -> {
        setKanaNameLike(text + "%");
      }
      case ENDS_WITH -> {
        setKanaNameLike("%" + text);
      }
      case CONTAINS -> {
        setKanaNameLike("%" + text + "%");
      }
      default -> throw new IllegalArgumentException("kanaName に指定できない operator です: " + operator);
    }

    if (this.getKanaNameEq() != null && this.getKanaNameLike() != null) {
      throw new IllegalStateException("kanaName EQ(完全一致) と STARTS_WITH,ENDS_WITH,CONTAINS(部分一致) は併用できません");
    }
  }

  public void applyNicknameFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();
    String text = filter.getValue();

    switch (operator) {
      case EQ-> {
        setNicknameEq(text);
      }
      case STARTS_WITH -> {
        setNicknameLike(text + "%");
      }
      case ENDS_WITH -> {
        setNicknameLike("%" + text);
      }
      case CONTAINS -> {
        setNicknameLike("%" + text + "%");
      }
      default -> throw new IllegalArgumentException("nickname に指定できない operator です: " + operator);
    }

    if (this.getNicknameEq() != null && this.getNicknameLike() != null) {
      throw new IllegalStateException("nickname EQ(完全一致) と STARTS_WITH,ENDS_WITH,CONTAINS(部分一致) は併用できません");
    }
  }

  public void applyCourseCodeFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();

    switch (operator) {
      case EQ -> {
        String courseCode = filter.getValue();
        setCourseCodeEq(courseCode);
      }
      case IN -> {
        List<String> courseCodes = filter.getValues();
        setCourseCodeIn(courseCodes);
      }
      default -> throw new IllegalArgumentException("courseCode に指定できない operator です: " + operator);
    }

    if (this.getCourseCodeEq() != null && this.getCourseCodeIn() != null) {
      throw new IllegalStateException("courseCode eq と in は併用できません");
    }
  }

  public void applyStatusIdFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();

    switch (operator) {
      case EQ -> {
        String rawValue = filter.getValue();
        Integer statusId = Integer.parseInt(rawValue);
        setStatusIdEq(statusId);
      }
      case IN -> {
        List<String> rawValues = filter.getValues();
        List<Integer> statusIds = rawValues.stream().map(Integer::parseInt).collect(Collectors.toList());
        setStatusIdIn(statusIds);
      }
      default -> throw new IllegalArgumentException("statusId に指定できない operator です: " + operator);
    }

    if (this.getStatusIdEq() != null && this.getStatusIdIn() != null) {
      throw new IllegalStateException("statusId eq と in は併用できません");
    }
  }

  public void applyAgeFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();

    switch (operator) {
      case EQ -> {
        String rawValue = filter.getValue();
        Integer age = Integer.parseInt(rawValue);
        setAgeEq(age);
      }
      case GTE -> {
        String rawValue = filter.getValue();
        Integer ageMin = Integer.parseInt(rawValue);
        setAgeMin(ageMin);
      }
      case LTE -> {
        String rawValue = filter.getValue();
        Integer ageMax = Integer.parseInt(rawValue);
        setAgeMax(ageMax);
      }
      case BETWEEN -> {
        List<String> rawValues = filter.getValues();
        List<Integer> range = rawValues.stream().map(Integer::parseInt).toList();

        if(range.size() != 2 || range.get(0).equals(range.get(1))){
          throw new IllegalArgumentException("age between は 2件の等しくない整数値で指定してください");
        }

        setAgeMin(Collections.min(range));
        setAgeMax(Collections.max(range));
      }
      default -> throw new IllegalArgumentException("age に指定できない operator です: " + operator);
    }

    if (this.getAgeEq() != null && (this.getAgeMin() != null || this.getAgeMax() != null)) {
      throw new IllegalStateException("age eq と age の範囲条件は併用できません");
    }

  }

  public void applyCourseApplyAtFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();

    switch (operator) {
      case EQ -> {
        String rawValue = filter.getValue();
        LocalDate date = LocalDate.parse(rawValue);
        setCourseApplyAtEq(date);
      }
      case GTE -> {
        String rawValue = filter.getValue();
        LocalDate from = LocalDate.parse(rawValue);
        setCourseApplyAtFrom(from);
      }
      case LTE -> {
        String rawValue = filter.getValue();
        LocalDate to = LocalDate.parse(rawValue);
        setCourseApplyAtTo(to);
      }
      case BETWEEN -> {
        List<String> rawValues = filter.getValues();
        List<LocalDate> range = rawValues.stream().map(LocalDate::parse).toList();
        if (range.size() != 2 || range.get(0).equals(range.get(1))) {
          throw new IllegalArgumentException("courseApplyAt between は 2件の等しくない日付で指定してください");
        }
        setCourseApplyAtFrom(Collections.min(range));
        setCourseApplyAtTo(Collections.max(range));
      }
      default -> throw new IllegalArgumentException("courseApplyAt に指定できない operator です: " + operator);
    }

    if (this.getCourseApplyAtEq() != null
        && (this.getCourseApplyAtFrom() != null || this.getCourseApplyAtTo() != null)) {
      throw new IllegalStateException("courseApplyAt eq と期間条件は併用できません");
    }
  }

  public void applyIsDeletedFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();
    String value = filter.getValue();

    if (!EQ.equals(operator)) {
      throw new IllegalArgumentException("isDeleted に指定できる operator は eq のみです");
    }
    Boolean isDeleted = Boolean.parseBoolean(value);
    setDeleted(isDeleted);
  }

}