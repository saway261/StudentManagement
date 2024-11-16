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
import org.springframework.web.bind.annotation.RequestParam;
import reisetech.StudentManagement.data.Student;
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

//  @GetMapping("/students")
//  public String getPresentStudentAndCourseList(Model model) {
//    model.addAttribute("studentList", service.selectPresentStudentList());
//    model.addAttribute("courseList", service.selectPresentCourseList());
//    return "studentAndCourseList";
//  }

  @GetMapping("/students")
  public String getStudentList(
      @RequestParam(name = "deleted", required = false, defaultValue = "false") boolean deleted,
      Model model) {
    if (deleted) {
      // 退会者リスト
      model.addAttribute("studentList", service.selectDeletedStudentList());
      return "deletedStudentList"; // 退会者リスト用テンプレート
    } else {
      // 在籍学生リスト
      model.addAttribute("studentList", service.selectPresentStudentList());
      model.addAttribute("courseList", service.selectPresentCourseList());
      return "studentAndCourseList"; // 在籍学生リスト用テンプレート
    }
  }


  @GetMapping("/students/new")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentsCourses(Arrays.asList(new StudentsCourses()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @GetMapping("/students/{studentId}")
  public String getStudent(@PathVariable int studentId, Model model) {
    StudentDetail studentDetail = service.searchStudentDetail(studentId);
    if (studentDetail.getStudent().isDeleted()) {
      // 退会者の場合はエラーページか別の処理にリダイレクト
      return "redirect:/students/deleted/{studentId}";
    }
    model.addAttribute("studentDetail", studentDetail);
    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setStudentId(studentId);
    model.addAttribute("newCourse", studentsCourses);
    return "updateStudent";
  }

//  @GetMapping("/students?deleted=true")
//  public String getDeletedStudentList(Model model) {
//    model.addAttribute("studentList", service.selectDeletedStudentList());
//    return "deletedStudentList";
//  }

  @GetMapping("/students/deleted/{studentId}")
  public String getDeletedStudent(@PathVariable int studentId, Model model) {
    StudentDetail studentDetail = service.searchStudentDetail(studentId);
    if (!studentDetail.getStudent().isDeleted()) {
      return "redirect:/students";
    }
    model.addAttribute("student", studentDetail.getStudent());
    return "switchStudentStatus";
  }

  @PostMapping("/students/new")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    service.registerStudent(studentDetail);
    service.registerCourse(studentDetail);
    return "redirect:/students";
  }

  @PostMapping("/students/update")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "updateStudent";
    }
    service.updateStudent(studentDetail);
    service.updateCourses(studentDetail);
    return "redirect:/students";
  }

  @PostMapping("/students/courses/new")
  public String addCourse(@ModelAttribute StudentsCourses newCourse,
      BindingResult result) {
    if (result.hasErrors()) {
      return "updateStudent";
    }
    service.addCourse(newCourse);
    return "redirect:/students";
  }

  @PostMapping("/students/switch-status")
  public String switchStudentStatus(@ModelAttribute Student student, BindingResult result) {
    if (result.hasErrors()) {
      return "switchStudentStatus";
    }
    service.switchStudentStatus(student);
    return "redirect:/students";

  }
}
