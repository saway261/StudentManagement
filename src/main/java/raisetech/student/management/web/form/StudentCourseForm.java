package raisetech.student.management.web.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.validation.CourseName;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;
import raisetech.student.management.data.value.Id;

@Schema(description = "受講生コース登録・更新用フォーム")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseForm {

  @Schema(description = "コースID 登録時は自動採番を行うため不要、更新時のみ必要", example = "1")
  @NotNull(groups = OnUpdate.class)
  @Positive(groups = OnUpdate.class)
  private Integer courseId;

  @Schema(
      description = "コース名 'Javaコース','AWSコース','デザインコース','Webマーケティングコース','フロントエンドコース'のみが入力可能",
      example = "Javaコース"
  )
  @NotNull(groups = {OnRegister.class, OnUpdate.class})
  @CourseName(groups = {OnRegister.class, OnUpdate.class})
  private String courseName;

  @Schema(description = "コース終了予定日（更新時に設定）", example = "2025-07-01")
  @NotNull(groups = OnUpdate.class)
  private LocalDate courseEndAt;

  public static StudentCourse toDomain(StudentCourseForm form, Id studentId) { //package private

    return new StudentCourse(form, studentId);

  }

}

