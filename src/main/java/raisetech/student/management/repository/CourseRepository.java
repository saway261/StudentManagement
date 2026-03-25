package raisetech.student.management.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CourseRepository {

  /**
   * 指定されたコースコードがDBに存在するか確認する
   * @return 存在すればtrue、しなければfalse
   */
  @Select("SELECT COUNT(*) > 0 FROM COURSE_MASTER WHERE course_code = #{courseCode}")
  boolean existsByCourseCode(String courseCode);
}