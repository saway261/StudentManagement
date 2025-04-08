package raisetech.student.management.exception.handling;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Schema(description = "エラーレスポンス")
@Getter
public final class ErrorResponseBody {

  @Schema(description = "HTTPステータス", example = "BAD_REQUEST")
  private final HttpStatus status;

  @Schema(description = "簡潔なエラーメッセージ", example = "validation error")
  private final String message;

  @Schema(description = "エラー詳細 エラー発生個所とエラーメッセージを持ちます。", examples = "field : studentId \n message : 受講生IDが存在しません")
  private final List<Map<String, String>> errors;

  public ErrorResponseBody(HttpStatus status, String message, List<Map<String, String>> errors) {
    this.status = status;
    this.message = message;
    this.errors = errors;
  }

}
