package raisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import raisetech.student.management.data.domain.validation.CourseName;
import raisetech.student.management.data.domain.validation.OnUpdate;

@Schema(description = "受講生コース")
@Getter
@Setter
public class StudentCourse {

  @Schema(description = "コースID 自動採番を行う", example = "1")
  @Positive(groups = OnUpdate.class)
  private int courseId;

  @Schema(
      description = "コース名 'Javaコース','AWSコース','デザインコース','Webマーケティングコース','フロントエンドコース'のみが入力可能",
      example = "Javaコース"
  )
  @NotNull
  @CourseName
  private String courseName;

  @Schema(description = "受講生ID", example = "1")
  private int studentId;

  @Schema(description = "コース開始日 登録処理が実行された日付", example = "2025-01-01")
  private LocalDate courseStartAt;

  @Schema(description = "コース終了予定日 コース開始日の6か月後の日付", example = "2025-07-01")
  @NotNull(groups = OnUpdate.class)
  private LocalDate courseEndAt;

  /**
   * 受講生詳細登録で呼び出されるコンストラクタです。コース名,受講生IDを受け取り、コンストラクタが呼び出された日付をもとに、受講開始日と受講終了予定日を自動的にセットします。
   * 受講生詳細更新で呼び出すと、courseIdが0なのでのちの処理でInvalidIdExceptionが投げられます。これにより、受講生詳細更新で子のコンストラクタを呼び出し、意図せず受講終了予定日が書き替えられることを防ぎます。
   *
   * @param courseName コース名
   * @param studentId  受講生ID
   */
  public StudentCourse(String courseName, int studentId) {
    this.courseId = 0;
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
  public StudentCourse(StudentCourse requestCourse, int studentId) {
    //requestCourseが、受講生詳細更新に必要なコースID,コース名,受講終了予定日を持っていない場合はNullPointerExceptionを投げる
    if (requestCourse.getCourseId() == 0 |
        requestCourse.getCourseName().isEmpty() |
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

  @JsonCreator
  public StudentCourse(@JsonProperty("courseId") int courseId,
      @JsonProperty("courseName") String courseName,
      @JsonProperty("studentId") int studentId,
      @JsonProperty("courseStartAt") LocalDate courseStartAt,
      @JsonProperty("courseEndAt") LocalDate courseEndAt) {
    this.courseId = courseId;
    this.courseName = courseName;
    this.studentId = studentId;
    this.courseStartAt = courseStartAt;
    this.courseEndAt = courseEndAt;
  }
}
