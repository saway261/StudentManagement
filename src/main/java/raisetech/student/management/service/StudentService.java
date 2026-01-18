package raisetech.student.management.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<StudentDetail> serchStudentDetailList(){
    List<Student> students = repository.searchAllStudentList();

    return students.stream()
        .map(student -> buildStudentDetail(student.getStudentId())).collect(Collectors.toList());
  }

  public StudentDetail searchStudentDetail(int studentId){
    return buildStudentDetail(studentId);
  }

  @Transactional
  public StudentDetail registerStudentDetail(StudentDetail studentDetail){
    Student student = studentDetail.getStudent();
    repository.registerStudent(student);

    for(StudentCourse studentCourse : studentDetail.getStudentCourses()){
      initStudentCourse(studentCourse, student);
      repository.registerStudentCourse(studentCourse);
    }
    return studentDetail;
  }


  @Transactional
  public StudentDetail updateStudentDetail(StudentDetail studentDetail){
    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourse studentCourse : studentDetail.getStudentCourses()){
      repository.updateStudentCourse(studentCourse);
    }
    return studentDetail;
  }

  private static void initStudentCourse(StudentCourse studentCourse, Student student) {
    LocalDate now = LocalDate.now();

    studentCourse.setStudentId(student.getStudentId());
    studentCourse.setCourseStartAt(now);
    studentCourse.setCourseEndAt(now.plusYears(1));
  }

  private StudentDetail buildStudentDetail(int studentId){
    Student student = repository.searchStudent(studentId);
    List<StudentCourse> studentCourses = repository.searchStudentCourses(studentId);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourses(studentCourses);

    return studentDetail;
  }

}
