package raisetech.student.management.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Optional;
import lombok.Getter;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.value.Id;

@Schema(description = "受講生が受講しているコース情報（出力用）")
@Getter
public class StudentCourseResponse {

  @Schema(description = "コースID", example = "1")
  private final Integer courseId;

  @Schema(description = "コース名", example = "Javaコース")
  private final String courseName;

  @Schema(description = "コース開始日", example = "2025-01-01")
  private final LocalDate courseStartAt;

  @Schema(description = "コース終了予定日", example = "2025-07-01")
  private final LocalDate courseEndAt;

  StudentCourseResponse(StudentCourse domain) { //package private
    this.courseId = Optional.ofNullable(domain.getCourseId())// Id → int
        .map(Id::getValue)
        .orElseThrow(() -> new IllegalArgumentException("courseIdは必須です"));
    this.courseName = domain.getCourseName();
    this.courseStartAt = domain.getCourseStartAt();
    this.courseEndAt = domain.getCourseEndAt();
  }

}

