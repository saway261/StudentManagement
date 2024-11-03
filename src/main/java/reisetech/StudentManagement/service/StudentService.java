package reisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
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
    return repository.search();
  }

  public List<Student> serchStudentList() {
    List<Student> allStudents = repository.search();

    List<Student> filteredStudents = allStudents.stream()
        .filter(student -> student.getAge() >= 30)
        .collect(Collectors.toList());

    return filteredStudents;
  }

  public List<StudentsCourses> selectAllStudentsCourseList() {
    return repository.searchStudentsCourses();
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    List<StudentsCourses> allStudentsCourses = repository.searchStudentsCourses();

    List<StudentsCourses> filteredStudentsCourses = allStudentsCourses.stream()
        .filter(courses -> courses.getCourseName().equals("Java"))
        .collect(Collectors.toList());

    return filteredStudentsCourses;

  }

  @Transactional//サービス層の登録・更新・削除をするメソッドに必ずつける
  public void registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.registerStudent(student);
  }

  @Transactional
  public void registerCourse(StudentDetail studentDetail) {
    List<StudentsCourses> courses = studentDetail.getStudentsCourses();
    String studentId = studentDetail.getStudent().getStudentId();
    LocalDate today = LocalDate.now();
    for (StudentsCourses course : courses) {
      course.setStudentId(studentId);
      course.setCourseStartAt(today);
      course.setCourseEndAt(today.plusMonths(6));
      repository.registerCourse(course);
    }
  }

  @Transactional
  public StudentDetail searchStudentDetail(String studentId) {
    StudentDetail studentDetail = new StudentDetail();

    Student student = repository.searchStudentByStudentId(studentId);
    List<StudentsCourses> courses = repository.searchCoursesByStudentId(studentId);

    studentDetail.setStudent(student);
    studentDetail.setStudentsCourses(courses);

    return studentDetail;
  }
}
