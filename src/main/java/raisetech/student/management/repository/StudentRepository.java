package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルに紐づくRepositoryです。
 *
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生IDの全件検索を行います。serachStudentメソッド,searchStudentCoursesメソッドと組み合わせて使われる想定です。
   * @return 受講生ID一覧(アクティブな全件)
   */
  List<Integer> searchActiveStudentIdList();

  /**
   * 受講生の検索を行います。
   * @param studentId 受講生ID
   * @return 受講生
   */
  Student searchStudent(int studentId);

  /**
   * 受講生IDに紐づく受講生のコース情報の検索を行います。
   * @param studentId 受講性ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourses(int studentId);

  /**
   * 受講生の新規登録を行います。受講生IDは自動採番を行います。
   * @param student 受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コースの新規登録を行います。受講生コースIDは自動採番を行います。
   * @param studentCourse 受講生コース
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生の更新を行います。削除フラグの更新（論理削除）もここで行います。
   * @param student 受講生
   */
  int updateStudent(Student student);

  /**
   * 受講生コースの更新を行います。
   * @param studentCourse 受講生コース
   */
  int updateStudentCourse(StudentCourse studentCourse);

}
