package raisetech.student.management.data.master;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Schema(description = "コース")
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class Course {

  @Schema(description = "コースコード 英字大文字2~5文字", example = "JA")
  @NotBlank
  @Size(min = 2, max = 5)
  @Pattern(
      regexp = "^[A-Z]+$",
      message = "コースコードは英字大文字のみで入力してください"
  )
  private final String courseCode;

  @Schema(description = "コース名" ,example = "Javaコース")
  @NotBlank
  @Size(max = 20)
  private final String courseName;

}