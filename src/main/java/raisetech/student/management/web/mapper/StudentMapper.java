package raisetech.student.management.web.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.value.Id;
import raisetech.student.management.web.form.StudentCourseForm;
import raisetech.student.management.web.form.StudentDetailForm;
import raisetech.student.management.web.form.StudentForm;
import raisetech.student.management.web.response.StudentDetailResponse;

@Component
public class StudentMapper {

  // --- フォーム → ドメイン（登録・更新用） ---
  public StudentDetail toDomain(StudentDetailForm form) {
    Student student = toDomain(form.getStudent());
    List<StudentCourse> courses = form.getStudentCourseList().stream()
        .map(this::toDomain)
        .collect(Collectors.toList());

    return new StudentDetail(student, courses);
  }

  private Student toDomain(StudentForm form) {
    return new Student(
        form.getStudentId() == null ? null : new Id(form.getStudentId()),
        form.getFullname(),
        form.getKanaName(),
        form.getNickname(),
        form.getEmail(),
        form.getArea(),
        form.getTelephone(),
        form.getAge(),
        form.getSex(),
        form.getRemark(),
        form.isDeleted()
    );
  }

  private StudentCourse toDomain(StudentCourseForm form) {
    return new StudentCourse(
        form.getCourseId() == null ? null : new Id(form.getCourseId()),
        form.getCourseName(),
        null,
        null,
        form.getCourseEndAt() == null ? null : form.getCourseEndAt()
    );
  }

  // --- ドメイン → レスポンスDTO（出力用） ---

  public StudentDetailResponse fromDomain(StudentDetail domain) {
    return new StudentDetailResponse(domain);
  }
}

