package reisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reisetech.StudentManagement.data.Student;
import reisetech.StudentManagement.data.StudentsCourses;
import reisetech.StudentManagement.domain.StudentDetail;
import reisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;
  private Student student;
  private StudentsCourses studentsCourses;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> selectAllStudentList() {
    return repository.searchStudentList();
  }

  public List<StudentsCourses> selectAllStudentsCourseList() {
    return repository.searchStudentsCourseList();
  }

  @Transactional//サービス層の登録・更新・削除をするメソッドに必ずつける
  public void registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.registerStudent(student);
  }

  @Transactional
  public void registerCourse(StudentDetail studentDetail) {
    List<StudentsCourses> courses = studentDetail.getStudentsCourses();
    int studentId = studentDetail.getStudent().getStudentId();
    LocalDate today = LocalDate.now();
    for (StudentsCourses course : courses) {
      course.setStudentId(studentId);
      course.setCourseStartAt(today);
      course.setCourseEndAt(today.plusMonths(6));
      repository.registerCourse(course);
    }
  }

  @Transactional
  public StudentDetail searchStudentDetail(int studentId) {
    StudentDetail studentDetail = new StudentDetail();

    Student student = repository.searchStudentByStudentId(studentId);
    List<StudentsCourses> courses = repository.searchCoursesByStudentId(studentId);

    studentDetail.setStudent(student);
    studentDetail.setStudentsCourses(courses);

    return studentDetail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
  }

  @Transactional
  public void updateCourses(StudentDetail studentDetail) {
    List<StudentsCourses> courses = studentDetail.getStudentsCourses();
    for (StudentsCourses course : courses) {
      repository.updateCourse(course);
    }
  }

  @Transactional
  public void addCourse(StudentsCourses additionalCourse) {
    additionalCourse.setCourseEndAt(additionalCourse.getCourseStartAt().plusMonths(6));
    repository.registerCourse(additionalCourse);
  }

}
