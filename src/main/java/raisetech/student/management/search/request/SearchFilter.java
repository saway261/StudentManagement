package raisetech.student.management.search.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raisetech.student.management.validation.ValidSearchFilter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ValidSearchFilter // クラスレベルカスタムバリデータ
public class SearchFilter {

  @NotBlank
  private String field;

  @NotNull
  private SearchOperator operator;

  private String value;
  private List<String> values;


}