package raisetech.student.management.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;

@Getter
@Schema(description = "受講生詳細（出力用）")
public class StudentDetailResponse {

  @Schema(description = "受講生情報")
  private final StudentResponse student;

  @Schema(description = "受講生が登録しているすべてのコース情報")
  private final List<StudentCourseResponse> studentCourseList;

  private StudentDetailResponse(StudentDetail domain) {
    this.student = new StudentResponse(domain.getStudent());
    List<StudentCourseResponse> courseResponseList = new ArrayList<>();
    for (StudentCourse course : domain.getStudentCourseList()) {
      courseResponseList.add(new StudentCourseResponse(course));
    }
    this.studentCourseList = courseResponseList;
  }

  public static StudentDetailResponse fromDomain(StudentDetail domain) {
    return new StudentDetailResponse(domain);
  }
}
