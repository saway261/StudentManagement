package raisetech.student.management.search.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raisetech.student.management.validation.ValidSearchFilter;

@Schema(description = "検索フィルター1件分。operator に応じて value または values を指定します。")
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ValidSearchFilter // クラスレベルカスタムバリデータ
public class SearchFilter {

  @Schema(description = "検索対象フィールド名", example = "fullName")
  @NotBlank
  private String field;

  @Schema(description = "検索演算子", example = "CONTAINS", implementation = SearchOperator.class)
  @NotNull
  private SearchOperator operator;

  @Schema(
      description = "単一値を取る演算子で使用する値。EQ, CONTAINS, STARTS_WITH, ENDS_WITH, GTE, LTE で使用",
      example = "田中",
      nullable = true
  )
  private String value;

  @Schema(
      description = "複数値を取る演算子で使用する値一覧。IN, BETWEEN で使用",
      example = "[\"1\", \"2\"]",
      nullable = true
  )
  private List<String> values;


}