package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.master.Course;

@Mapper
public interface CourseRepository {

  /**
   * 指定されたコースコードがDBに存在するか確認する
   * @return 存在すればtrue、しなければfalse
   */
  @Select("SELECT COUNT(*) > 0 FROM COURSE_MASTER WHERE course_code = #{courseCode}")
  boolean existsByCourseCode(String courseCode);

  /**
   * 提供コースの全件検索を行います。
   * @return 提供コース一覧
   */
  @Select("SELECT * FROM course_master")
  List<Course> searchCourseList();

  /**
   * 提供コースの新規追加を行います。
   * @param course 提供コース
   */
  @Insert("INSERT INTO course_master VALUES(#{courseCode}, #{courseName})")
  void registerCourse(Course course);


  /**
   * コースコードを指定してコース名の更新を行います。
   * @param course 提供コース
   * @return 更新成功件数
   */
  @Update("UPDATE course_master SET course_name=#{courseName} WHERE course_code=#{courseCode}")
  int updateCourse(Course course);

}