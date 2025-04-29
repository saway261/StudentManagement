package raisetech.student.management.testutil;

import java.time.LocalDate;
import java.util.List;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;

public class TestDataFactory {

  public static Student newDummyStudent(int studentId) {
    return new Student(studentId, "山田太郎", "やまだたろう", "タロー", "taro@email.com",
        "東京都練馬区", "090-0000-0000", 20, "男", "特になし", false);
  }

  public static StudentCourse newDummyStudentCourse(int studentId, int courseId) {
    LocalDate now = LocalDate.now();
    return new StudentCourse(courseId, "Javaコース", studentId, now, now.plusMonths(6));
  }

  public static StudentCourse newDummyStudentCourseOnRegister(int studentId, int courseId) {
    return new StudentCourse(courseId, "Javaコース", studentId, null, null);
  }

  public static StudentDetail newDummyStudentDetail(int studentId, int courseId) {
    return new StudentDetail(newDummyStudent(studentId),
        List.of(newDummyStudentCourse(studentId, courseId)));
  }
}
