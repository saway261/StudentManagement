package raisetech.student.management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.master.Course;
import raisetech.student.management.repository.CourseRepository;

@Service
public class CourseService {

  private CourseRepository repository;

  @Autowired
  public CourseService(CourseRepository repository) {
    this.repository = repository;
  }

  /**
   * コースマスタに存在する提供コースの一覧検索を行います。
   * @return 提供コースの一覧
   */
  public List<Course> searchCourseMasterList(){
    return repository.searchCourseList();
  }

  /**
   * 提供コースの新規登録を行います。
   * @param course 新規登録する提供コース
   */
  public void registerCourse(Course course){
    repository.registerCourse(course);
  }
}
