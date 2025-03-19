package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourses();

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

}
