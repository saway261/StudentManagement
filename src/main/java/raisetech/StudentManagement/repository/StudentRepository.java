package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生テーブルと受講生コーステーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧
   */
  @Select("SELECT * FROM students")
  List<Student> searchAllStudentList();

  /**
   * アクティブ受講生の全件検索を行います。
   *
   * @return アクティブ受講生一覧
   */
  @Select("SELECT * FROM students WHERE is_deleted = 0")
  List<Student> searchActiveStudentList();

  /**
   * 受講生の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生
   */
  @Select("SELECT * FROM students WHERE student_id=#{studentId}")
  Student serchStudent(int studentId);

  /**
   * 受講生コースの全件検索を行います。
   *
   * @return 受講生コース一覧
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchAllCourseList();

  /**
   * アクティブ受講生の受講生IDに紐づいたコース情報の全件検索を行います。
   *
   * @return アクティブ受講生のコース一覧
   */
  @Select("SELECT sc.* FROM students_courses sc "
      + "JOIN students s ON sc.student_id = s.student_id "
      + "WHERE s.is_deleted = 0")
  List<StudentCourse> searchActiveCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します・
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報(複数)
   */
  @Select("SELECT * FROM students_courses WHERE student_id=#{studentId}")
  List<StudentCourse> searchCourses(int studentId);

  @Insert("INSERT INTO students(fullname, kana_name, nickname, email, area,"
      + " telephone, age, sex, remark, is_deleted) "
      + "VALUES (#{fullname}, #{kanaName}, #{nickname}, #{email}, #{area},"
      + " #{telephone}, #{age}, #{sex}, #{remark}, false);")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void registerStudent(Student student);

  @Insert("INSERT INTO students_courses(course_name, student_id, course_start_at, course_end_at) "
      + "VALUES (#{courseName}, #{studentId}, #{courseStartAt}, #{courseEndAt});")
  @Options(useGeneratedKeys = true, keyProperty = "courseId")
  void registerCourse(StudentCourse course);

  @Update("UPDATE students SET "
      + "fullname=#{fullname}, kana_name=#{kanaName}, nickname=#{nickname}, "
      + "email=#{email}, area=#{area}, telephone=#{telephone}, age=#{age}, "
      + "sex=#{sex}, remark=#{remark} , is_deleted=#{isDeleted} "
      + "WHERE student_id=#{studentId}")
  void updateStudent(Student student);

  @Update("UPDATE students_courses SET "
      + "course_name=#{courseName}, course_end_at=#{courseEndAt} "
      + "WHERE course_id=#{courseId}")
  void updateCourse(StudentCourse course);


}
