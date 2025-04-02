package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生テーブルと受講生コーステーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * アクティブ受講生の全件検索を行います。
   *
   * @return アクティブ受講生一覧
   */
  List<Student> searchActiveStudentList();

  /**
   * 受講生の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生
   */
  Student searchStudent(int studentId);

  /**
   * 受講生IDに紐づく受講生コース情報を検索します
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報(複数)
   */
  List<StudentCourse> searchCourses(int studentId);

  /**
   * 受講生を新規登録します。IDに関しては自動採番を行う。
   *
   * @param student 新規受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コースを新規登録します。コースIDに関しては自動裁判を行う。
   *
   * @param course 新規受講生コース
   */
  void registerCourse(StudentCourse course);

  /**
   * 受講生IDから受講生を検索し、一致した受講生の情報を更新します。論理削除も行います。
   *
   * @param student 更新後の受講生
   */
  @Update("UPDATE students SET "
      + "fullname=#{fullname}, kana_name=#{kanaName}, nickname=#{nickname}, "
      + "email=#{email}, area=#{area}, telephone=#{telephone}, age=#{age}, "
      + "sex=#{sex}, remark=#{remark} , is_deleted=#{isDeleted} "
      + "WHERE student_id=#{studentId}")
  void updateStudent(Student student);

  /**
   * コースIDから受講生コースを検索し、一致した受講生コースのコース名やコース終了予定日を更新します。
   *
   * @param course 更新後の受講生コース
   */
  @Update("UPDATE students_courses SET "
      + "course_name=#{courseName}, course_end_at=#{courseEndAt} "
      + "WHERE course_id=#{courseId}")
  void updateCourse(StudentCourse course);


}
