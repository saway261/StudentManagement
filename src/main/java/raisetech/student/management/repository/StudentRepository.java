package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

  @Update("UPDATE students SET full_name=#{fullName}, kana_name=#{kanaName}, nickname=#{nickname},"
      + "email=#{email}, area=#{area}, age=#{age}, sex=#{sex}, remark=#{remark}, is_deleted=#{isDeleted} "
      + "WHERE student_id=#{studentId}" )
  void updateStudent(Student student);

  @Update("UPDATE student_courses SET course_name=#{courseName} WHERE course_id=#{courseId}")
  void updateStudentCourse(StudentCourse studentCourse);

}
