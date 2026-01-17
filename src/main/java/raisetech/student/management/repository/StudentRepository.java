package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchAllStudentList();

  @Select("SELECT * FROM students WHERE student_id=#{studentId}")
  Student searchStudent(int studentId);

  @Select("SELECT * FROM student_courses WHERE student_id=#{studentId}")
  List<StudentCourse> searchStudentCourses(int studentId);

  @Insert("INSERT INTO students(full_name, kana_name, nickname, email, area, age, sex, remark, is_deleted) "
      + "VALUES(#{fullName}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{sex}, #{remark}, false)")
  @Options(useGeneratedKeys = true,keyProperty = "studentId")
  void registerStudent(Student student);

  @Insert("INSERT INTO student_courses(student_id, course_name, course_start_at, course_end_at) "
      + "VALUES(#{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt})")
  @Options(useGeneratedKeys = true,keyProperty = "courseId")
  void registerStudentCourse(StudentCourse studentCourse);

  @Update("UPDATE students SET full_name=#{fullName}, kana_name=#{kanaName}, nickname=#{nickname},"
      + "email=#{email}, area=#{area}, age=#{age}, sex=#{sex}, remark=#{remark}, is_deleted=#{isDeleted} "
      + "WHERE student_id=#{studentId}" )
  void updateStudent(Student student);

  @Update("UPDATE student_courses SET course_name=#{courseName} WHERE course_id=#{courseId}")
  void updateStudentCourse(StudentCourse studentCourse);

}
