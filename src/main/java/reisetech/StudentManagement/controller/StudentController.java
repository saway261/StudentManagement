package reisetech.StudentManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reisetech.StudentManagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  @GetMapping("/allStudentAndCourseList")
  public String getAllStudentAndCourseList(Model model) {
    model.addAttribute("studentList", service.selectAllStudentList());
    model.addAttribute("courseList", service.selectAllStudentsCourseList());
    return "studentAndCourseList";
  }

}
