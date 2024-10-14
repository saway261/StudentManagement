package reisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
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
      "INSERT INTO students (student_id, fullname, furigana, nickname, email, city, telephone, age, gender, remark) "
          + "VALUES (#{studentId},#{fullname},#{furigana},#{nickname},"
          + "#{email},#{city},#{telephone},#{age},#{gender},#{remark});")
  void registerStudent(String studentId, String fullname, String furigana, String nickname,
      String email, String city, String telephone, int age, String gender, String remark);
}