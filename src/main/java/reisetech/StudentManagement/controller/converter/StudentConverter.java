package reisetech.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import reisetech.StudentManagement.data.Student;
import reisetech.StudentManagement.data.StudentsCourses;
import reisetech.StudentManagement.domain.StudentDetail;

@Component
public class StudentConverter {

  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentsCourses> studentsCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    for (Student student : students) {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);
      List<StudentsCourses> convertStudentCourses = new ArrayList<>();
      for (StudentsCourses studentsCourse : studentsCourses) {
        if (student.getStudentId().equals(studentsCourse.getStudentId())) {
          convertStudentCourses.add(studentsCourse);
        }
      }
      studentDetail.setStudentsCourses(convertStudentCourses);
      studentDetails.add(studentDetail);
    }
    return studentDetails;
  }

}
