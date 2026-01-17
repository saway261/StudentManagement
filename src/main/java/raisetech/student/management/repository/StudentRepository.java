package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchAllStudentList();

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchAllStudentCourseList();

  @Select("SELECT * FROM students WHERE student_id=#{studentId}")
  Student searchStudent(int studentId);

  @Select("SELECT * FROM students_courses WHERE student_id=#{studentId}")
  List<StudentCourse> searchStudentCourses(int studentId);

}
