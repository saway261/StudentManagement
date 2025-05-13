package raisetech.student.management.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;

@Getter
@Schema(description = "受講生詳細（出力用）")
@AllArgsConstructor
public class StudentDetailResponse {

  @Schema(description = "受講生情報")
  private final StudentResponse student;

  @Schema(description = "受講生が登録しているすべてのコース情報")
  private final List<StudentCourseResponse> studentCourseList;

  public static StudentDetailResponse fromDomain(StudentDetail domain) {
    StudentResponse studentResponse = new StudentResponse(domain.getStudent());
    List<StudentCourseResponse> studentCourseResponseList = new ArrayList<>();

    for (StudentCourse course : domain.getStudentCourseList()) {
      studentCourseResponseList.add(new StudentCourseResponse(course));
    }

    return new StudentDetailResponse(studentResponse, studentCourseResponseList);
  }
}
