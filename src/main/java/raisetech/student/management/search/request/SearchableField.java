package raisetech.student.management.search.request;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum SearchableField {
  FULL_NAME("fullName", String.class),
  KANA_NAME("kanaName",String.class),
  NICKNAME("nickname",String.class),
  AGE("age", Integer.class),
  IS_DELETED("isDeleted", Boolean.class),
  COURSE_CODE("courseCode", String.class),
  STATUS_ID("statusId", Integer.class),
  COURSE_APPLY_AT("courseApplyAt", LocalDate.class),
  COURSE_START_AT("courseStartAt", LocalDate.class),
  COURSE_PLANNED_END_AT("coursePlannedEndAt", LocalDate.class),
  COURSE_FINISHED_AT("courseFinishedAt", LocalDate.class);

  private final String fieldName;
  private final Class<?> type;

  SearchableField(String fieldName, Class<?> type) {
    this.fieldName = fieldName;
    this.type = type;
  }

  /**
   * 引数の文字列がfieldNameとして存在するかチェックします。
   * @param inputField 検索フィルターで指定されたフィールド名
   * @return SearchableFieldに存在するならtrue,存在しないならfalse
   */
  public static boolean existsSearchableFields(String inputField) {
    return Arrays.stream(values())
        .anyMatch(field -> field.getFieldName().equals(inputField));
  }

  /**
   * fieldNameから対応するSearchableFieldインスタンスを取得します。
   * * @param inputField フィールド名
   * @return 一致するSearchableField
   * @throws IllegalArgumentException 一致するものがない場合
   */
  public static SearchableField getFromFieldName(String inputField) {
    return Arrays.stream(values())
        .filter(field -> field.getFieldName().equals(inputField))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("無効なフィールド名です: " + inputField));
  }

  /**
   * fieldNameから対応する型のクラス情報を取得します。
   * 一致するものがない場合はIllegalArgumentExceptionを投げます。
   * @param inputField 検索フィルターで指定されたフィールド名
   * @return 指定されたフィールドの型
   */
  public static Class<?> getTypeByFieldName(String inputField) {
    // 上記の共通メソッドを呼び出し、見つかったインスタンスから型を取り出す
    return getFromFieldName(inputField).getType();
  }

  /**
   * すべてのfieldNameをリストで取得します。
   * @return fieldNameのリスト
   */
  public static List<String> getAllFieldNames() {
    return Arrays.stream(values())
        .map(SearchableField::getFieldName)
        .collect(Collectors.toList());
  }
}
