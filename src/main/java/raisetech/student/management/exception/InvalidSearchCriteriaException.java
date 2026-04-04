package raisetech.student.management.exception;

import jakarta.annotation.Nullable;
import lombok.Getter;
import raisetech.student.management.search.request.SearchOperator;
import raisetech.student.management.search.request.SearchableField;

@Getter
public class InvalidSearchCriteriaException extends RuntimeException {

  private final SearchableField field;
  private final @Nullable SearchOperator operator;

  public InvalidSearchCriteriaException(SearchableField field,SearchOperator operator,String message) {
    super(message);
    this.field = field;
    this.operator = operator;
  }

  public InvalidSearchCriteriaException(SearchableField field,String message) {
    super(message);
    this.field = field;
    this.operator = null;
  }
}
