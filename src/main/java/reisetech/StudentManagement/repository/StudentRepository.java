package reisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import reisetech.StudentManagement.data.Student;
import reisetech.StudentManagement.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

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
}