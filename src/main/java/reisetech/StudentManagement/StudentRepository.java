package reisetech.StudentManagement;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentRepository {

  @Select("SELECT name, age FROM students WHERE name = #{name}")
  Student selectByName(String name);

  @Select("SELECT name, age FROM students")
  List<Student> selectAllStudents();

  @Insert("INSERT INTO students (name, age) VALUES (#{name}, #{age})")
  void createStudent(String name, int age);

  @Update("UPDATE students SET age = #{age} WHERE name = #{name}")
  void updateStudent(String name, int age);

  @Delete("DELETE FROM students WHERE name = #{name}")
  void deleteStudent(String name);


}
