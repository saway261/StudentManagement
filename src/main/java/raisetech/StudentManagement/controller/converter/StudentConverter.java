package raisetech.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@Component
public class StudentConverter {

  StudentService service;

  @Autowired
  public StudentConverter(StudentService service) {
    this.service = service;
  }

  public List<StudentDetail> convertStudentDetails(List<Student> students) {
    List<StudentDetail> studentDetails = new ArrayList<>();

    for (Student student : students) {
      studentDetails.add(service.searchStudent(student.getStudentId()));
    }
    return studentDetails;
  }

}
