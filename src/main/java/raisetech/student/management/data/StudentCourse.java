package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.student.management.data.domain.validation.CourseName;
import raisetech.student.management.data.domain.validation.OnUpdate;

@Schema(description = "受講生コース")
@Getter
@Setter
@NoArgsConstructor
public class StudentCourse {

  @Schema(description = "コースID 自動裁判を行う", example = "1")
  @Positive(groups = OnUpdate.class)
  private int courseId;

  @Schema(
      description = "コース名 'Javaコース','AWSコース','デザインコース','Webマーケティングコース','フロントエンドコース'のみが入力可能",
      example = "Javaコース"
  )
  @NotNull
  @CourseName
  private String courseName;

  @Schema(description = "受講生ID", example = "1")
  private int studentId;

  @Schema(description = "コース開始日 登録処理が実行された日付", example = "2025-01-01")
  private LocalDate courseStartAt;

  @Schema(description = "コース終了予定日 コース開始日の6か月後の日付", example = "2025-07-01")
  private LocalDate courseEndAt;


  public StudentCourse(String courseName, int studentId) {
    this.courseName = courseName;
    this.studentId = studentId;

    LocalDate now = LocalDate.now();
    this.courseStartAt = now;
    this.courseEndAt = now.plusMonths(6);
  }
}
