package raisetech.student.management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  @GetMapping("/students")
  public List<StudentDetail> getStudentList(){
    return service.serchStudentDetailList();
  }

  @GetMapping("/students/{studentId}")
  public StudentDetail getStudent(@PathVariable int studentId){
    return service.searchStudentDetail(studentId);
  }

  @PostMapping("/students")
  public ResponseEntity<String> registerStudent(@RequestBody StudentDetail studentDetail){
    service.registerStudentDetail(studentDetail);
    return ResponseEntity.ok("登録処理が成功しました。");
  }

  @PutMapping("/students")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail){
    service.updateStudentDetail(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました！");
  }

}
