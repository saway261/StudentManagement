package raisetech.student.management.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import raisetech.student.management.data.StudentCourse;

@Schema(description = "受講生コース情報（レスポンス）")
public class StudentCourseResponse {

  @Schema(description = "コースID", example = "1")
  private final Integer courseId;

  @Schema(description = "コース名", example = "Javaコース")
  private final String courseName;

  @Schema(description = "コース受講開始日", example = "2025-01-01")
  private final LocalDate courseStartAt;

  @Schema(description = "コース終了予定日", example = "2025-07-01")
  private final LocalDate courseEndAt;

  public StudentCourseResponse(StudentCourse course) {
    this.courseId = course.getCourseId().getValue();
    this.courseName = course.getCourseName();
    this.courseStartAt = course.getCourseStartAt();
    this.courseEndAt = course.getCourseEndAt();
  }

  // getter
}

