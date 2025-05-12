package raisetech.student.management.testutil;

import java.time.LocalDate;
import java.util.List;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.value.Id;

public class TestDataFactory {

  public static Student makeCompletedStudent(Id studentId) {
    return new Student(studentId, "山田太郎", "やまだたろう", "タロー", "taro@email.com",
        "東京都練馬区", "090-0000-0000", 20, "男", "特になし", false);
  }

  public static StudentCourse makeCompletedStudentCourse(Id studentId, Id courseId) {
    LocalDate now = LocalDate.now();
    return new StudentCourse(courseId, "Javaコース", studentId, now, now.plusMonths(6));
  }

  public static StudentCourse makeEnoughStudentCourseOnRegister(Id studentId,
      Id courseId) {
    return new StudentCourse(courseId, "Javaコース", studentId, null, null);
  }

  public static StudentDetail makeCompletedStudentDetail(Id studentId, Id courseId) {
    return new StudentDetail(makeCompletedStudent(studentId),
        List.of(makeCompletedStudentCourse(studentId, courseId)));
  }
}
