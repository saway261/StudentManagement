package raisetech.student.management.search.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(
    description = "受講生高度検索条件",
    example = """
        {
          "filters": [
            {
              "field": "fullName",
              "operator": "CONTAINS",
              "value": "田中"
            },
            {
              "field": "statusId",
              "operator": "IN",
              "values": ["1", "2"]
            },
            {
              "field": "courseApplyAt",
              "operator": "BETWEEN",
              "values": ["2026-01-01", "2026-03-31"]
            }
          ]
        }
        """
)
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class StudentAdvancedSearchRequest {

  @Schema(
      description = "検索フィルター一覧。1件以上必須",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotEmpty
  @Valid
  private List<SearchFilter> filters;
}