package raisetech.student.management.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import raisetech.student.management.data.domain.StudentDetail;

@Getter
@Schema(description = "受講生詳細（出力用）")
public class StudentDetailResponse {

  @Schema(description = "受講生情報")
  private final StudentResponse student;

  @Schema(description = "受講生が登録しているすべてのコース情報")
  private final List<StudentCourseResponse> studentCourseList;

  public StudentDetailResponse(StudentDetail studentDetail) {
    this.student = new StudentResponse(studentDetail.getStudent());
    this.studentCourseList = studentDetail.getStudentCourseList().stream()
        .map(StudentCourseResponse::new)
        .toList();
  }
}
