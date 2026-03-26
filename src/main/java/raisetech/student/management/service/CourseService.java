package raisetech.student.management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.master.Course;
import raisetech.student.management.exception.TargetNotFoundException;
import raisetech.student.management.repository.CourseRepository;

@Service
public class CourseService {

  private CourseRepository repository;

  @Autowired
  public CourseService(CourseRepository repository) {
    this.repository = repository;
  }

  /**
   * コースマスタに存在するコースの一覧検索を行います。
   * @return コースの一覧
   */
  public List<Course> searchCourseMasterList(){
    return repository.searchCourseList();
  }

  /**
   * コースの新規登録を行います。
   * @param course 新規登録するコース
   */
  public void registerCourse(Course course){
    repository.registerCourse(course);
  }

  /**
   * コースコードを指定してコースのコース名を更新します。
   * @param course 更新するコース
   */
  public void updateCourse(Course course){
    int updated = repository.updateCourse(course);
    if(updated == 0) {
      throw new TargetNotFoundException("courseCode", "更新対象のコースが見つかりませんでした");
    }
  }
}
