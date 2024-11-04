package reisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import reisetech.StudentManagement.data.Student;
import reisetech.StudentManagement.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students WHERE is_deleted = 0")
  List<Student> searchActiveStudentList();

  @Select("SELECT * FROM students_courses WHERE is_deleted = 0")
  List<StudentsCourses> searchActiveCourseList();

  @Select("SELECT * FROM students WHERE student_id=#{studentId}")
  Student searchStudentByStudentId(int studentId);

  @Select("SELECT * FROM students_courses WHERE student_id=#{studentId}")
  List<StudentsCourses> searchCoursesByStudentId(int studentId);

  @Insert(
      "INSERT INTO students (fullname, kana_name, nickname, email, city, telephone, age, gender, remark, is_deleted) "
          + "VALUES (#{fullname}, #{kanaName}, #{nickname},"
          + "#{email}, #{city}, #{telephone}, #{age}, #{gender}, #{remark}, false);")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void registerStudent(Student student);

  @Insert(
      "INSERT INTO students_courses  (course_name, student_id, course_start_at, course_end_at) "
          + "VALUES (#{courseName},#{studentId},#{courseStartAt},#{courseEndAt});")
  @Options(useGeneratedKeys = true, keyProperty = "courseId")
  void registerCourse(StudentsCourses courses);


  @Update("UPDATE students SET "
      + "fullname=#{fullname}, kana_name=#{kanaName}, nickname=#{nickname}, email=#{email}, city=#{city},"
      + " telephone=#{telephone}, age=#{age}, gender=#{gender}, remark=#{remark}, is_deleted=#{isDeleted} "
      + "WHERE student_id=#{studentId}")
  void updateStudent(Student student);

  @Update("UPDATE students_courses SET "
      + "course_name=#{courseName}, course_start_at=#{courseStartAt}, course_end_at=#{courseEndAt}"
      + "WHERE course_id=#{courseId}")
  void updateCourse(StudentsCourses courses);

}
