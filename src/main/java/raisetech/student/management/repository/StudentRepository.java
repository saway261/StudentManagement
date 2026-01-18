package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルに紐づくRepositoryです。
 *
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   * @return 受講生一覧(全件)
   */
  @Select("SELECT * FROM students")
  List<Student> searchAllStudentList();

  /**
   * 受講生の検索を行います。
   * @param studentId 受講生ID
   * @return 受講生
   */
  @Select("SELECT * FROM students WHERE student_id=#{studentId}")
  Student searchStudent(int studentId);

  /**
   * 受講生IDに紐づく受講生のコース情報の検索を行います。
   * @param studentId 受講性ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  @Select("SELECT * FROM student_courses WHERE student_id=#{studentId}")
  List<StudentCourse> searchStudentCourses(int studentId);

  /**
   * 受講生の新規登録を行います。受講生IDは自動採番を行います。
   * @param student 受講生
   */
  @Insert("INSERT INTO students(full_name, kana_name, nickname, email, area, age, sex, remark, is_deleted) "
      + "VALUES(#{fullName}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{sex}, #{remark}, false)")
  @Options(useGeneratedKeys = true,keyProperty = "studentId")
  void registerStudent(Student student);

  /**
   * 受講生コースの新規登録を行います。受講生コースIDは自動採番を行います。
   * @param studentCourse 受講生コース
   */
  @Insert("INSERT INTO student_courses(student_id, course_name, course_start_at, course_end_at) "
      + "VALUES(#{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt})")
  @Options(useGeneratedKeys = true,keyProperty = "courseId")
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生の更新を行います。削除フラグの更新（論理削除）もここで行います。
   * @param student 受講生
   */
  @Update("UPDATE students SET full_name=#{fullName}, kana_name=#{kanaName}, nickname=#{nickname},"
      + "email=#{email}, area=#{area}, age=#{age}, sex=#{sex}, remark=#{remark}, is_deleted=#{isDeleted} "
      + "WHERE student_id=#{studentId}" )
  void updateStudent(Student student);

  /**
   * 受講生コースの更新を行います。
   * @param studentCourse 受講生コース
   */
  @Update("UPDATE student_courses SET course_name=#{courseName} WHERE course_id=#{courseId}")
  void updateStudentCourse(StudentCourse studentCourse);

}
