package raisetech.StudentManagement.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {

    List<Student> thirtiesStudents = repository.search().stream()
        .filter(student -> 30 <= student.getAge() && student.getAge() < 40)
        .collect(Collectors.toList());

    return thirtiesStudents;
  }

  public List<StudentCourse> searchStudentCourseList() {

    List<StudentCourse> javaCourseInfos = repository.searchStudentCourses().stream()
        .filter(course -> course.getCourseName().equals("Java")).collect(Collectors.toList());
    return javaCourseInfos;
  }
}
