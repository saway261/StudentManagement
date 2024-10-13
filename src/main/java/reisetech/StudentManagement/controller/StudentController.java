package reisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reisetech.StudentManagement.controller.converter.StudentConverter;
import reisetech.StudentManagement.data.Student;
import reisetech.StudentManagement.data.StudentsCourses;
import reisetech.StudentManagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

//  @GetMapping("/allStudentList")
//  public String getAllStudentList(Model model) {
//    model.addAttribute("studentList", service.selectAllStudentList());
//    return "studentList";
//  }

  @GetMapping("/allStudentDetails")
  public String getAllStudentDetails(Model model) {
    List<Student> students = service.selectAllStudentList();
    List<StudentsCourses> studentsCourses = service.selectAllStudentsCourseList();

    model.addAttribute("studentDetails",
        converter.convertStudentDetails(students, studentsCourses));
    return "studentDetails";
  }

  @GetMapping("/allStudentAndCourseList")
  public String getAllStudentAndCourseList(Model model) {
    model.addAttribute("studentList", service.selectAllStudentList());
    model.addAttribute("courseList", service.selectAllStudentsCourseList());
    return "studentAndCourseList";
  }

}
