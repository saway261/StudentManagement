package raisetech.student.management.data;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import raisetech.student.management.data.value.Id;
import raisetech.student.management.web.form.StudentCourseForm;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class StudentCourse {

  private final Id courseId;

  private final String courseName;

  private final Id studentId;

  private final LocalDate courseStartAt;

  private final LocalDate courseEndAt;

  public StudentCourse(StudentCourseForm form, Id studentId) {
    if (studentId == null) {
      throw new IllegalArgumentException(
          "studentIdがnullの場合、StudentCourseインスタンスが生成できません。");
    }

    if (form.getCourseId() == null) {
      LocalDate now = LocalDate.now();

      this.courseId = null;
      this.courseName = form.getCourseName();
      this.studentId = studentId;
      this.courseStartAt = now;
      this.courseEndAt = now.plusMonths(6);

    } else {

      this.courseId = new Id(form.getCourseId());
      this.courseName = form.getCourseName();
      this.studentId = studentId;
      this.courseStartAt = null;
      this.courseEndAt = form.getCourseEndAt();
    }

  }

  public Id getCourseId() {
    if (this.courseId == null) {
      throw new NullPointerException("courseIdがnullのため、getCourseId()を実行できません");
    }
    return courseId;
  }

  public Id getStudentId() {
    if (this.studentId == null) {
      throw new NullPointerException("studentIdがnullのため、getStudentId()を実行できません");
    }
    return studentId;
  }


}
