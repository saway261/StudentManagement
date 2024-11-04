package reisetech.StudentManagement.controller;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reisetech.StudentManagement.data.StudentsCourses;
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

  @GetMapping("/student/{studentId}")
  public String getStudent(@PathVariable int studentId, Model model) {
    StudentDetail studentDetail = service.searchStudentDetail(studentId);
    model.addAttribute("studentDetail", studentDetail);
    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setStudentId(studentId);
    model.addAttribute("additionalCourse", studentsCourses);
    return "updateStudent";
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentsCourses(Arrays.asList(new StudentsCourses()));
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

  @PostMapping("/updateStudent")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "updateStudent";
    }
    service.updateStudent(studentDetail);
    service.updateCourses(studentDetail);
    return "redirect:/allStudentAndCourseList";
  }

  @PostMapping("/addCourse")
  public String addCourse(@ModelAttribute StudentsCourses additionalCourse,
      BindingResult result) {
    if (result.hasErrors()) {
      return "updateStudent";
    }
    service.addCourse(additionalCourse);
    return "redirect:/allStudentAndCourseList";
  }


}
