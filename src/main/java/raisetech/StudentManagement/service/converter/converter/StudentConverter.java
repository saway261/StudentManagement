package raisetech.StudentManagement.service.converter.converter;

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

  public StudentDetail convertToStudentDetail(Student student) {
    List<StudentCourse> courses = new ArrayList<>();

    //受講生IDに紐づくコースがない場合は、「未登録」としたコースのインスタンスを生成し、受講生詳細に変換する
    if (repository.searchCourses(student.getStudentId()).isEmpty()) {
      StudentCourse course = new StudentCourse(student.getStudentId());
      courses.add(course);
    } else {
      courses = repository.searchCourses(student.getStudentId());
    }

    StudentDetail studentDetail = new StudentDetail(student, courses);
    return studentDetail;
  }

  public List<StudentDetail> convertToStudentDetailList(List<Student> students) {
    List<StudentDetail> studentDetailList = new ArrayList<>();

    for (Student student : students) {
      studentDetailList.add(convertToStudentDetail(student));
    }

    return studentDetailList;
  }

  public StudentCourse complementCourse(Student student, StudentCourse course) {
    StudentCourse responsetCourse = new StudentCourse(course.getCourseName(),
        student.getStudentId(),
        course.getCourseStartAt());
    return responsetCourse;
  }

}
