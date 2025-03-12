package raisetech.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Component
public class StudentConverter {

  StudentRepository repository;

  @Autowired
  public StudentConverter(StudentRepository repository) {
    this.repository = repository;
  }

  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentCourse> courses) {
    List<StudentDetail> studentDetails = new ArrayList<>();

    for (Student student : students) {
      StudentDetail studentDetail = new StudentDetail();

      studentDetail.setStudent(student);
      studentDetail.setStudentsCourses(getStudentLinkedCourses(student, courses));

      studentDetails.add(studentDetail);
    }
    return studentDetails;
  }

  private List<StudentCourse> getStudentLinkedCourses(Student student,
      List<StudentCourse> courses) {
    List<StudentCourse> studentLinkedCourses = courses.stream()
        .filter(course -> student.getStudentId() == course.getStudentId())
        .collect(Collectors.toList());

    return studentLinkedCourses;
  }
}
