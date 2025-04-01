package raisetech.StudentManagement.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchActiveStudentList() {
    return repository.searchActiveStudentList();
  }

  public List<StudentCourse> searchActiveCourseList() {
    return repository.searchActiveCourseList();
  }

  public StudentDetail searchStudent(int studentId) {
    Student student = repository.serchStudent(
        studentId);//全件（キャンセルの有無にかかわらず）からstudentIdに一致するレコードをとってくる

    List<StudentCourse> courses = new ArrayList<>();
    if (repository.searchCourses(studentId).isEmpty()) {
      StudentCourse course = new StudentCourse();
      course.setCourseName("未登録");
      courses.add(course);
    } else {
      courses = repository.searchCourses(studentId);
    }

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentsCourses(courses);
    return studentDetail;
  }


  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    List<StudentCourse> courseList = studentDetail.getStudentsCourses();

    repository.registerStudent(studentDetail.getStudent());
    for (StudentCourse course : courseList) {
      repository.registerCourse(complementCourse(student, course));
    }
    return studentDetail;
  }

  public StudentCourse complementCourse(Student student, StudentCourse course) {
    StudentCourse studentCourse = course; // returnする（DBにInsertする)StudentCourseインスタンスを生成し、引数course(viewから受け取ったもの)を代入

    //studentIDに引数studentのstudentIdをsetする
    studentCourse.setStudentId(student.getStudentId());
    //courseEndAtにcourseStartAtの半年後の値をsetする
    studentCourse.setCourseEndAt(course.getCourseStartAt().plusMonths(6));

    return studentCourse;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourse studentCourse : studentDetail.getStudentsCourses()) {
      repository.updateCourse(studentCourse);
    }
  }
}

