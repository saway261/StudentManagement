package raisetech.student.management.web.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;

@Schema(description = "受講生詳細登録・更新用フォーム")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetailForm {

  @NotNull(groups = {OnRegister.class, OnUpdate.class})
  @Valid
  private StudentForm student;

  @NotEmpty(groups = {OnRegister.class, OnUpdate.class})
  @Valid
  private List<StudentCourseForm> studentCourseList;

  public static StudentDetail toDomain(StudentDetailForm form) {
    Student student = new StudentForm().toDomain(form.getStudent());
    List<StudentCourse> studentCourseList = new ArrayList<>();

    for (StudentCourseForm formCourse : form.getStudentCourseList()) {
      studentCourseList.add(new StudentCourseForm().toDomain(formCourse));
    }

    return new StudentDetail(student, studentCourseList);
  }

}
