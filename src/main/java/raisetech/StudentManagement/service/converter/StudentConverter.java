package raisetech.StudentManagement.service.converter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

/**
 * Serviceから渡された受講生に対して、紐づくコース情報を取得して受講生詳細へ変換を行うコンバーターです。
 */
@Component
public class StudentConverter {

  StudentRepository repository;

  @Autowired
  public StudentConverter(StudentRepository repository) {
    this.repository = repository;
  }

  /**
   * Serviceから渡された単一の受講生に対して、紐づく受講生コースを取得して単一の受講生詳細へ変換を行います。
   *
   * @param student 受講生
   * @return 受講生に紐づけられた受講生詳細
   */
  public StudentDetail convertToStudentDetail(Student student) {
    List<StudentCourse> courses = repository.searchCourses(student.getStudentId());
    StudentDetail studentDetail = new StudentDetail(student, courses);
    return studentDetail;
  }

  /**
   * Serviceから渡された受講生リストに対して、それぞれに紐づく受講生コースを取得して受講生詳細へ変換し、受講生詳細のリストとして返します。
   *
   * @param studentList 受講生リスト
   * @return 受講生詳細リスト
   */
  public List<StudentDetail> convertToStudentDetailList(List<Student> studentList) {
    List<StudentDetail> studentDetailList = new ArrayList<>();

    for (Student student : studentList) {
      studentDetailList.add(convertToStudentDetail(student));
    }

    return studentDetailList;
  }

  /**
   * 登録処理を行う受講生コースに対して、受講生ID、コース開始日、コース終了予定日を補完して返します。
   *
   * @param student 登録する受講生
   * @param course  登録する受講生コース
   * @return 受講生ID、コース開始日、コース終了予定日が補完された受講生コース
   */
  public StudentCourse complementCourse(Student student, StudentCourse course) {
    StudentCourse responsetCourse = new StudentCourse(
        course.getCourseName(), student.getStudentId());
    return responsetCourse;
  }

}
