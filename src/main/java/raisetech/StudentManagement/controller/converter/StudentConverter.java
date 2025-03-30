package raisetech.StudentManagement.controller.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.service.StudentService;

@Component
public class StudentConverter {

  StudentService service;

  @Autowired
  public StudentConverter(StudentService service) {
    this.service = service;
  }

}
