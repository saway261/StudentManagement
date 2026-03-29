package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raisetech.student.management.validation.CourseCodeExists;
import raisetech.student.management.validation.CreateGroup;
import raisetech.student.management.validation.UpdateGroup;

@Schema(description = "受講生コース情報")
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourse {

  @Schema(description = "受講生コースID 登録時は自動採番を行うため不要、更新時のみ必要", example = "1")
  @NotNull(groups = UpdateGroup.class)
  @Positive(groups = UpdateGroup.class)
  private Integer studentCourseId;

  @Schema(description = "紐づく受講生を指定する受講生ID プログラム側が値をセットするため常に入力不要", example = "1")
  private Integer studentId;

  @Schema(description = "コースコード", example = "JA")
  @NotNull(groups = CreateGroup.class)
  @CourseCodeExists(groups = CreateGroup.class)
  private String courseCode;

  @Schema(description = "ステータスID 申込状況を表す", example = "1")
  @NotNull(groups = UpdateGroup.class)
  @Min(value = 1,groups = UpdateGroup.class)
  @Max(value = 5,groups = UpdateGroup.class)
  private Integer statusId;

  @Schema(description = "受講申込日 プログラム側が値をセットするため常に入力不要", example = "2026/01/01")
  private LocalDate courseApplyAt;

  @Schema(description = "受講開始日 プログラム側が値をセットするため常に入力不要", example = "2026/01/31")
  private LocalDate courseStartAt;

  @Schema(description = "受講終了予定日 プログラム側が値をセットするため常に入力不要", example = "2026/06/31")
  private LocalDate courseEndAt;

}
