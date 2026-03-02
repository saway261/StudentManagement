package raisetech.student.management.testutil;

import java.time.LocalDate;
import java.util.List;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;

public class TestDataFactory {

  public static Student makeCompletedStudent(int studentId) {
    return new Student(studentId, "山田太郎", "やまだたろう", "タロー", "taro@email.com",
        "東京都練馬区", "090-0000-0000", 20, "男", "特になし", false);
  }

  public static StudentCourse makeCompletedStudentCourse(int studentId, int courseId) {
    LocalDate now = LocalDate.now();
    return new StudentCourse(courseId, studentId, "Javaコース",  now, now.plusYears(1));
  }

  public static StudentDetail makeCompletedStudentDetail(int studentId, int courseId) {
    return new StudentDetail(makeCompletedStudent(studentId),
        List.of(makeCompletedStudentCourse(studentId, courseId)));
  }

}
