package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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
}