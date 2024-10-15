package reisetech.StudentManagement.domain;

import lombok.Getter;
import lombok.Setter;
import reisetech.StudentManagement.data.Student;
import reisetech.StudentManagement.data.StudentsCourses;

@Getter
@Setter
public class StudentDetail {

  private Student student;
  private StudentsCourses studentsCourses;
}