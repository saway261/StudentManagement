package reisetech.StudentManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import reisetech.StudentManagement.data.Student;
import reisetech.StudentManagement.domain.StudentDetail;
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

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(new Student());
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    service.registerStudent(studentDetail);
    service.registerCourse(studentDetail);
    return "redirect:/allStudentAndCourseList";
  }

}
