package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchStudentList();

  @Select("SELECT * FROM students WHERE student_id=#{studentId}")
  Student serchStudent(int studentId);

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchCourseList();

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
