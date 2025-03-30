package raisetech.StudentManagement.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  @GetMapping("/studentAndCourseList")
  public String getStudentDetails(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> courses = service.searchCourseList();

    model.addAttribute("studentList", students);
    model.addAttribute("courseList", courses);
    return "studentAndCourseList";
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentsCourses(Arrays.asList(new StudentCourse()));//空のstudentsCoursesをセット
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    service.registerStudent(studentDetail);
    return "redirect:/studentAndCourseList";
  }

  @GetMapping("/student/{studentId}")
  public String viewStudent(@PathVariable("studentId") int studentId, Model model) {
    StudentDetail studentDetail = service.searchStudent(studentId);
    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }

  @PostMapping("/updateStudent")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "updateStudent";
    }
    service.updateStudent(studentDetail);
    return "redirect:/studentAndCourseList";
  }

  //TODO:論理削除の実装
}
