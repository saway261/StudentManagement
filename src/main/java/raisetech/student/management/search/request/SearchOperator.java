package raisetech.student.management.search.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "検索演算子"
)
public enum SearchOperator {
  EQ,
  CONTAINS,
  STARTS_WITH,
  ENDS_WITH,
  IN,
  GTE,
  LTE,
  BETWEEN
}