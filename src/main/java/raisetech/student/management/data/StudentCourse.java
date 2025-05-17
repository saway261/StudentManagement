package raisetech.student.management.data;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import raisetech.student.management.data.value.Id;

@Getter
@AllArgsConstructor
public class StudentCourse {

  private final Id courseId;

  private final String courseName;

  private final Id studentId;

  private final LocalDate courseStartAt;

  private final LocalDate courseEndAt;

  /**
   * 受講生詳細登録で呼び出されるコンストラクタです。コース名,受講生IDを受け取り、コンストラクタが呼び出された日付をもとに、受講開始日と受講終了予定日を自動的にセットします。
   * 受講生詳細更新で呼び出すと、courseIdがnullなのでのちの処理でInvalidIdExceptionが投げられます。これにより、受講生詳細更新で子のコンストラクタを呼び出し、意図せず受講終了予定日が書き替えられることを防ぎます。
   *
   * @param courseName コース名
   * @param studentId  受講生ID
   */
  public StudentCourse(String courseName, Id studentId) {
    this.courseId = null;
    this.courseName = courseName;
    this.studentId = studentId;

    LocalDate now = LocalDate.now();
    this.courseStartAt = now;
    this.courseEndAt = now.plusMonths(6);
  }

  /**
   * 受講生詳細更新で呼び出されるコンストラクタです。受講生コースオブジェクトと受講生IDを受け取ります。
   * 受講生IDが0またはコース名,コース終了予定日がnullのStudentCourseオブジェクトを受け取った場合はNullPointerExceptionを投げます。
   *
   * @param requestCourse 受講生コースコースオブジェクト
   * @param studentId     受講生ID
   */
  public StudentCourse(StudentCourse requestCourse, Id studentId) {
    if (requestCourse.getCourseId() == null ||
        requestCourse.getCourseName().isEmpty() ||
        requestCourse.getCourseEndAt() == null) {
      throw new NullPointerException();

    } else {
      this.courseId = requestCourse.getCourseId();
      this.courseName = requestCourse.getCourseName();
      this.studentId = studentId;
      this.courseStartAt = requestCourse.courseStartAt;
      this.courseEndAt = requestCourse.getCourseEndAt();
    }
  }

}
