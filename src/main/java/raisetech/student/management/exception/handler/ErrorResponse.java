package raisetech.student.management.exception.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Schema(description = "エラーレスポンス")
@AllArgsConstructor
@Getter
public class ErrorResponse {

  @Schema(description = "HTTPステータスコード", example = "400 BAD_REQUEST")
  private final HttpStatus status;

  @Schema(description = "エラーの概要", example = "validation error")
  private final String message;

  @Schema(description = "エラーの詳細", example = """
        [
                {
                    "field": "getStudent.studentId",
                    "message": "0 より大きな値にしてください"
                }
            ]
      """
  )
  private final List<Map<String, String>> errors;

}
