package reisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import reisetech.StudentManagement.data.Student;
import reisetech.StudentManagement.data.StudentsCourses;

/**
 * 受講生情報を扱うリポジトリ。 全件検索や単一条件での検索、コース情報の検索が行えるクラスです。
 */
@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

}
//課題：students_coursesの全件をとる練習