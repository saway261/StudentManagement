package raisetech.student.management.search.criteria;

import static raisetech.student.management.search.request.SearchOperator.EQ;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raisetech.student.management.exception.InvalidSearchCriteriaException;
import raisetech.student.management.search.request.SearchFilter;
import raisetech.student.management.search.request.SearchOperator;
import raisetech.student.management.search.request.SearchableField;
import raisetech.student.management.search.request.StudentSimpleSearchRequest;

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

  /** Email 完全一致 */
  private String emailEq;

  /** Email LIKE検索用*/
  private String emailLike;

  /** 地域 完全一致 */
  private String areaEq;

  /** 地域 LIKE検索用*/
  private String areaLike;

  /** 電話番号 完全一致 */
  private String telephoneEq;

  /** 電話番号 LIKE検索用*/
  private String telephoneLike;

  /** 年齢 完全一致 */
  private Integer ageEq;

  /** 年齢 下限 */
  private Integer ageMin;

  /** 年齢 上限 */
  private Integer ageMax;

  /** 性別 完全一致 */
  private String sexEq;

  /** 性別 IN */
  private List<String> sexIn;

  /** 備考 完全一致 */
  private String remarkEq;

  /** 備考 LIKE検索用*/
  private String remarkLike;

  /** 削除フラグ */
  private Boolean isDeleted;

  /** コースコード 完全一致 */
  private String courseCodeEq;

  /** コースコード IN */
  private List<String> courseCodeIn;

  /** ステータスID 完全一致 */
  private Integer statusIdEq;

  /** ステータスID IN */
  private List<Integer> statusIdIn;

  /** 申込日 完全一致 */
  private LocalDate courseApplyAtEq;

  /** 申込日 下限 */
  private LocalDate courseApplyAtFrom;

  /** 申込日 上限 */
  private LocalDate courseApplyAtTo;

  /** 受講開始日 完全一致 */
  private LocalDate courseStartAtEq;

  /** 受講開始日 下限 */
  private LocalDate courseStartAtFrom;

  /** 受講開始日 上限 */
  private LocalDate courseStartAtTo;

  /** 受講終了予定日 完全一致 */
  private LocalDate coursePlannedEndAtEq;

  /** 受講終了予定日 下限 */
  private LocalDate coursePlannedEndAtFrom;

  /** 受講終了予定日 上限 */
  private LocalDate coursePlannedEndAtTo;

  /** 受講終了実績日 完全一致 */
  private LocalDate courseFinishedAtEq;

  /** 受講終了実績日 下限 */
  private LocalDate courseFinishedAtFrom;

  /** 受講終了実績日 上限 */
  private LocalDate courseFinishedAtTo;

  public StudentSearchCriteria(StudentSimpleSearchRequest simplerRequest) {
    String valueForFullName = simplerRequest.getFullNameContains();
    this.fullNameLike = valueForFullName == null ? null : "%" + valueForFullName + "%";
    String valueForKanaName = simplerRequest.getKanaNameContains();
    this.kanaNameLike = valueForKanaName == null ? null : "%" + valueForKanaName + "%";
    String valueForArea = simplerRequest.getAreaContains();
    this.areaLike = valueForArea == null ? null : "%" + valueForArea + "%";
    this.ageMin = simplerRequest.getAgeMin();
    this.ageMax = simplerRequest.getAgeMax();
    this.sexEq = simplerRequest.getSexEq();
    this.courseCodeEq = simplerRequest.getCourseCode();
    this.statusIdIn = simplerRequest.getStatusId();
    this.courseApplyAtFrom = simplerRequest.getApplyFrom();
    this.courseApplyAtTo = simplerRequest.getApplyTo();
    this.courseStartAtFrom = simplerRequest.getStartFrom();
    this.courseStartAtTo = simplerRequest.getStartTo();
    this.isDeleted = simplerRequest.getIsDeleted();
  }

  /**====================
   * 再代入不可のsetter群
   *=====================*/

  private void setFullNameEq(String fullNameEq) {
    if(this.fullNameEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.FULL_NAME,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.fullNameEq = fullNameEq;
  }

  private void setFullNameLike(String fullNameLike) {
    if(this.fullNameLike != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.FULL_NAME,
          "このフィールドの部分一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.fullNameLike = fullNameLike;
  }

  private void setKanaNameEq(String kanaNameEq) {
    if(this.kanaNameEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.KANA_NAME,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.kanaNameEq = kanaNameEq;
  }

  private void setKanaNameLike(String kanaNameLike) {
    if(this.kanaNameLike != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.KANA_NAME,
          "このフィールドの部分一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.kanaNameLike = kanaNameLike;
  }

  private void setNicknameEq(String nicknameEq) {
    if(this.nicknameEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.NICKNAME,SearchOperator.EQ,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.nicknameEq = nicknameEq;
  }

  private void setNicknameLike(String nicknameLike) {
    if(this.nicknameLike != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.NICKNAME,
          "このフィールドの部分一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.nicknameLike = nicknameLike;
  }

  private void setEmailEq(String emailEq) {
    if(this.emailEq != null){
        throw new InvalidSearchCriteriaException(
          SearchableField.EMAIL,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.emailEq = emailEq;
  }

  private void setEmailLike(String emailLike) {
    if(this.emailLike != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.EMAIL,
          "このフィールドの部分一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.emailLike = emailLike;
  }

  private void setAreaEq(String areaEq) {
    if(this.areaEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.AREA,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.areaEq = areaEq;
  }

  private void setAreaLike(String areaLike) {
    if(this.areaLike != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.AREA,
          "このフィールドの部分一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.areaLike = areaLike;
  }

  private void setTelephoneEq(String telephoneEq) {
    if(this.telephoneEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.TELEPHONE,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.telephoneEq = telephoneEq;
  }

  private void setTelephoneLike(String telephoneLike) {
    if(this.telephoneLike != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.TELEPHONE,
          "このフィールドの部分一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.telephoneLike = telephoneLike;
  }

  private void setAgeEq(Integer ageEq) {
    if(this.ageEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.AGE,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.ageEq = ageEq;
  }

  private void setAgeMin(Integer ageMin) {
    if(this.ageMin != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.AGE,
          "このフィールドの下限値は既に設定されています。重複指定はできません。");
    }
    this.ageMin = ageMin;
  }

  private void setAgeMax(Integer ageMax) {
    if(this.ageMax != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.AGE,
          "このフィールドの上限値は既に設定されています。重複指定はできません。");
    }
    this.ageMax = ageMax;
  }

  private void setSexEq(String sexEq) {
    if(this.sexEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.SEX,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.sexEq = sexEq;
  }

  private void setSexIn(List<String> sexIn) {
    if(this.sexIn != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.SEX,
          "このフィールドの検索リストが既に設定されています。重複指定はできません。");
    }
    this.sexIn = sexIn;
  }

  private void setRemarkEq(String remarkEq) {
    if(this.remarkEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.REMARK,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.remarkEq = remarkEq;
  }

  private void setRemarkLike(String remarkLike) {
    if(this.remarkLike != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.REMARK,
          "このフィールドの部分一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.remarkLike = remarkLike;
  }

  private void setDeleted(Boolean deleted) {
    if(this.isDeleted != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.IS_DELETED,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.isDeleted = deleted;
  }

  private void setCourseCodeEq(String courseCodeEq) {
    if(this.courseCodeEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_CODE,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.courseCodeEq = courseCodeEq;
  }

  private void setCourseCodeIn(List<String> courseCodeIn) {
    if(this.courseCodeIn != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_CODE,
          "このフィールドの検索リストが既に設定されています。重複指定はできません。");
    }
    this.courseCodeIn = courseCodeIn;
  }

  private void setStatusIdEq(Integer statusIdEq) {
    if(this.statusIdEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.STATUS_ID,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.statusIdEq = statusIdEq;
  }

  private void setStatusIdIn(List<Integer> statusIdIn) {
    if(this.statusIdIn != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.STATUS_ID,
          "このフィールドの検索リストが既に設定されています。重複指定はできません。");
    }
    this.statusIdIn = statusIdIn;
  }

  private void setCourseApplyAtEq(LocalDate courseApplyAtEq) {
    if(this.courseApplyAtEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_APPLY_AT,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.courseApplyAtEq = courseApplyAtEq;
  }

  private void setCourseApplyAtFrom(LocalDate courseApplyAtFrom) {
    if(this.courseApplyAtFrom != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_APPLY_AT,
          "このフィールドの範囲条件の始端が既に設定されています。重複指定はできません。");
    }
    this.courseApplyAtFrom = courseApplyAtFrom;
  }

  private void setCourseApplyAtTo(LocalDate courseApplyAtTo) {
    if(this.courseApplyAtTo != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_APPLY_AT,
          "このフィールドの範囲条件の終端が既に設定されています。重複指定はできません。");
    }
    this.courseApplyAtTo = courseApplyAtTo;
  }

  private void setCourseStartAtEq(LocalDate courseStartAtEq) {
    if(this.courseStartAtEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_START_AT,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.courseStartAtEq = courseStartAtEq;
  }

  private void setCourseStartAtFrom(LocalDate courseStartAtFrom) {
    if(this.courseStartAtFrom != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_START_AT,
          "このフィールドの範囲条件の始端が既に設定されています。重複指定はできません。");
    }
    this.courseStartAtFrom = courseStartAtFrom;
  }

  private void setCourseStartAtTo(LocalDate courseStartAtTo) {
    if(this.courseStartAtTo != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_START_AT,
          "このフィールドの範囲条件の終端が既に設定されています。重複指定はできません。");
    }
    this.courseStartAtTo = courseStartAtTo;
  }

  private void setCoursePlannedEndAtEq(LocalDate coursePlannedEndAtEq) {
    if(this.coursePlannedEndAtEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_PLANNED_END_AT,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.coursePlannedEndAtEq = coursePlannedEndAtEq;
  }

  private void setCoursePlannedEndAtFrom(LocalDate coursePlannedEndAtFrom) {
    if(this.coursePlannedEndAtFrom != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_PLANNED_END_AT,
          "このフィールドの範囲条件の始端が既に設定されています。重複指定はできません。");
    }
    this.coursePlannedEndAtFrom = coursePlannedEndAtFrom;
  }

  private void setCoursePlannedEndAtTo(LocalDate coursePlannedEndAtTo) {
    if(this.coursePlannedEndAtTo != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_PLANNED_END_AT,
          "このフィールドの範囲条件の終端が既に設定されています。重複指定はできません。");
    }
    this.coursePlannedEndAtTo = coursePlannedEndAtTo;
  }

  private void setCourseFinishedAtEq(LocalDate courseFinishedAtEq) {
    if(this.courseFinishedAtEq != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_FINISHED_AT,
          "このフィールドの完全一致検索条件が既に設定されています。重複指定はできません。");
    }
    this.courseFinishedAtEq = courseFinishedAtEq;
  }

  private void setCourseFinishedAtFrom(LocalDate courseFinishedAtFrom) {
    if(this.courseFinishedAtFrom != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_FINISHED_AT,
          "このフィールドの範囲条件の始端が既に設定されています。重複指定はできません。");
    }
    this.courseFinishedAtFrom = courseFinishedAtFrom;
  }

  private void setCourseFinishedAtTo(LocalDate courseFinishedAtTo) {
    if(this.courseFinishedAtTo != null){
      throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_FINISHED_AT,
          "このフィールドの範囲条件の終端が既に設定されています。重複指定はできません。");
    }
    this.courseFinishedAtTo = courseFinishedAtTo;
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
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.FULL_NAME, operator, "このフィールドに指定できない演算子です。");
    }

    if (this.getFullNameEq() != null && this.getFullNameLike() != null) {
      throw new InvalidSearchCriteriaException(SearchableField.FULL_NAME,"完全一致検索と部分一致検索は併用できません");
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
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.KANA_NAME, operator, "このフィールドに指定できない演算子です。");
    }

    if (this.getKanaNameEq() != null && this.getKanaNameLike() != null) {
      throw new InvalidSearchCriteriaException(SearchableField.KANA_NAME,"完全一致検索と部分一致検索は併用できません");
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
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.NICKNAME, operator, "このフィールドに指定できない演算子です。");
    }

    if (this.getNicknameEq() != null && this.getNicknameLike() != null) {
      throw new InvalidSearchCriteriaException(SearchableField.NICKNAME,"完全一致検索と部分一致検索は併用できません");
    }
  }

  public void applyEmailFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();
    String text = filter.getValue();

    switch (operator) {
      case EQ-> {
        setEmailEq(text);
      }
      case STARTS_WITH -> {
        setEmailLike(text + "%");
      }
      case ENDS_WITH -> {
        setEmailLike("%" + text);
      }
      case CONTAINS -> {
        setEmailLike("%" + text + "%");
      }
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.EMAIL, operator, "このフィールドに指定できない演算子です。");
    }

    if (this.getEmailEq() != null && this.getEmailLike() != null) {
      throw new InvalidSearchCriteriaException(SearchableField.EMAIL,"完全一致検索と部分一致検索は併用できません");
    }
  }

  public void applyAreaFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();
    String text = filter.getValue();

    switch (operator) {
      case EQ-> {
        setAreaEq(text);
      }
      case STARTS_WITH -> {
        setAreaLike(text + "%");
      }
      case ENDS_WITH -> {
        setAreaLike("%" + text);
      }
      case CONTAINS -> {
        setAreaLike("%" + text + "%");
      }
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.AREA, operator, "このフィールドに指定できない演算子です。");
    }

    if (this.getAreaEq() != null && this.getAreaLike() != null) {
      throw new InvalidSearchCriteriaException(SearchableField.AREA,"完全一致検索と部分一致検索は併用できません");
    }
  }

  public void applyTelephoneFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();
    String text = filter.getValue();

    switch (operator) {
      case EQ-> {
        setTelephoneEq(text);
      }
      case STARTS_WITH -> {
        setTelephoneLike(text + "%");
      }
      case ENDS_WITH -> {
        setTelephoneLike("%" + text);
      }
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.TELEPHONE, operator, "このフィールドに指定できない演算子です。");
    }
    // 電話番号検索で中間一致を求めるケースが思いつかないためCONTAINSは持たない

    if (this.getTelephoneEq() != null && this.getTelephoneLike() != null) {
      throw new InvalidSearchCriteriaException(SearchableField.TELEPHONE,"完全一致検索と部分一致検索は併用できません");
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
          throw new InvalidSearchCriteriaException(SearchableField.AGE, operator, "2件の等しくない日付で指定してください");
        }

        setAgeMin(Collections.min(range));
        setAgeMax(Collections.max(range));
      }
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.AGE, operator, "このフィールドに指定できない演算子です。");
    }

    if (this.getAgeEq() != null && (this.getAgeMin() != null || this.getAgeMax() != null)) {
      throw new InvalidSearchCriteriaException(SearchableField.AGE,"完全一致検索と範囲検索は併用できません");
    }

  }

  public void applySexFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();

    switch (operator) {
      case EQ -> {
        String sex = filter.getValue();
        setSexEq(sex);
      }
      case IN -> {
        List<String> sexes = filter.getValues();
        setSexIn(sexes);
      }
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.SEX, operator, "このフィールドに指定できない演算子です。");
    }

    if (this.getSexEq() != null && this.getSexIn() != null) {
      throw new InvalidSearchCriteriaException(SearchableField.SEX,"完全一致検索とリスト検索は併用できません");
    }
  }

  public void applyRemarkFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();
    String text = filter.getValue();

    switch (operator) {
      case EQ-> {
        setRemarkEq(text);
      }
      case CONTAINS -> {
        setRemarkLike("%" + text + "%");
      }
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.REMARK, operator, "このフィールドに指定できない演算子です。");
    }
    // 備考は記載が自由すぎるので、STARTS_WITH, ENDS_WITHのメリットが薄いため持たない。EQは念のため持つ

    if (this.getRemarkEq() != null && this.getRemarkLike() != null) {
      throw new InvalidSearchCriteriaException(SearchableField.REMARK,"完全一致検索と部分一致検索は併用できません");
    }
  }

  public void applyIsDeletedFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();
    String value = filter.getValue();

    if (!EQ.equals(operator)) {
      throw new InvalidSearchCriteriaException(
          SearchableField.IS_DELETED, operator, "このフィールドに指定できない演算子です。");
    }
    Boolean isDeleted = Boolean.parseBoolean(value);
    setDeleted(isDeleted);
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
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_CODE, operator, "このフィールドに指定できない演算子です。");
    }

    if (this.getCourseCodeEq() != null && this.getCourseCodeIn() != null) {
      throw new InvalidSearchCriteriaException(SearchableField.COURSE_CODE,"完全一致検索とリスト検索は併用できません");
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
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.STATUS_ID, operator, "このフィールドに指定できない演算子です。");
    }

    if (this.getStatusIdEq() != null && this.getStatusIdIn() != null) {
      throw new InvalidSearchCriteriaException(SearchableField.STATUS_ID,"完全一致検索とリスト検索は併用できません");
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
          throw new InvalidSearchCriteriaException(SearchableField.COURSE_APPLY_AT, operator, "2件の等しくない日付で指定してください");
        }
        setCourseApplyAtFrom(Collections.min(range));
        setCourseApplyAtTo(Collections.max(range));
      }
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_APPLY_AT, operator, "このフィールドに指定できない演算子です。");
    }

    if (this.getCourseApplyAtEq() != null
        && (this.getCourseApplyAtFrom() != null || this.getCourseApplyAtTo() != null)) {
      throw new InvalidSearchCriteriaException(SearchableField.COURSE_APPLY_AT,"完全一致検索と範囲検索は併用できません");
    }
  }

  public void applyCourseStartAtFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();

    switch (operator) {
      case EQ -> {
        String rawValue = filter.getValue();
        LocalDate date = LocalDate.parse(rawValue);
        setCourseStartAtEq(date);
      }
      case GTE -> {
        String rawValue = filter.getValue();
        LocalDate from = LocalDate.parse(rawValue);
        setCourseStartAtFrom(from);
      }
      case LTE -> {
        String rawValue = filter.getValue();
        LocalDate to = LocalDate.parse(rawValue);
        setCourseStartAtTo(to);
      }
      case BETWEEN -> {
        List<String> rawValues = filter.getValues();
        List<LocalDate> range = rawValues.stream().map(LocalDate::parse).toList();
        if (range.size() != 2 || range.get(0).equals(range.get(1))) {
          throw new InvalidSearchCriteriaException(SearchableField.COURSE_START_AT, operator, "2件の等しくない日付で指定してください");
        }
        setCourseStartAtFrom(Collections.min(range));
        setCourseStartAtTo(Collections.max(range));
      }
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_START_AT, operator, "このフィールドに指定できない演算子です。");
    }

    if (this.getCourseStartAtEq() != null
        && (this.getCourseStartAtFrom() != null || this.getCourseStartAtTo() != null)) {
      throw new InvalidSearchCriteriaException(SearchableField.COURSE_START_AT,"完全一致検索と範囲検索は併用できません");
    }
  }

  public void applyCoursePlannedEndAtFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();

    switch (operator) {
      case EQ -> {
        String rawValue = filter.getValue();
        LocalDate date = LocalDate.parse(rawValue);
        setCoursePlannedEndAtEq(date);
      }
      case GTE -> {
        String rawValue = filter.getValue();
        LocalDate from = LocalDate.parse(rawValue);
        setCoursePlannedEndAtFrom(from);
      }
      case LTE -> {
        String rawValue = filter.getValue();
        LocalDate to = LocalDate.parse(rawValue);
        setCoursePlannedEndAtTo(to);
      }
      case BETWEEN -> {
        List<String> rawValues = filter.getValues();
        List<LocalDate> range = rawValues.stream().map(LocalDate::parse).toList();
        if (range.size() != 2 || range.get(0).equals(range.get(1))) {
          throw new InvalidSearchCriteriaException(SearchableField.COURSE_PLANNED_END_AT, operator, "2件の等しくない日付で指定してください");
        }
        setCoursePlannedEndAtFrom(Collections.min(range));
        setCoursePlannedEndAtTo(Collections.max(range));
      }
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_PLANNED_END_AT, operator, "このフィールドに指定できない演算子です。");
    }

    if (this.getCoursePlannedEndAtEq() != null
        && (this.getCoursePlannedEndAtFrom() != null || this.getCoursePlannedEndAtTo() != null)) {
      throw new InvalidSearchCriteriaException(SearchableField.COURSE_PLANNED_END_AT,"完全一致検索と範囲検索は併用できません");
    }
  }

  public void applyCourseFinishedAtFilter(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();

    switch (operator) {
      case EQ -> {
        String rawValue = filter.getValue();
        LocalDate date = LocalDate.parse(rawValue);
        setCourseFinishedAtEq(date);
      }
      case GTE -> {
        String rawValue = filter.getValue();
        LocalDate from = LocalDate.parse(rawValue);
        setCourseFinishedAtFrom(from);
      }
      case LTE -> {
        String rawValue = filter.getValue();
        LocalDate to = LocalDate.parse(rawValue);
        setCourseFinishedAtTo(to);
      }
      case BETWEEN -> {
        List<String> rawValues = filter.getValues();
        List<LocalDate> range = rawValues.stream().map(LocalDate::parse).toList();
        if (range.size() != 2 || range.get(0).equals(range.get(1))) {
          throw new InvalidSearchCriteriaException(SearchableField.COURSE_FINISHED_AT, operator, "2件の等しくない日付で指定してください");
        }
        setCourseFinishedAtFrom(Collections.min(range));
        setCourseFinishedAtTo(Collections.max(range));
      }
      default -> throw new InvalidSearchCriteriaException(
          SearchableField.COURSE_FINISHED_AT, operator, "このフィールドに指定できない演算子です。");
    }

    if (this.getCourseFinishedAtEq() != null
        && (this.getCourseFinishedAtFrom() != null || this.getCourseFinishedAtTo() != null)) {
      throw new InvalidSearchCriteriaException(SearchableField.COURSE_FINISHED_AT,"完全一致検索と範囲検索は併用できません");
    }
  }

}