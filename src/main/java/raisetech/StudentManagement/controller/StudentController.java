package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.data.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;
import raisetech.StudentManagement.service.converter.converter.StudentConverter;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるContorollerです。
 */
@RestController
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  /**
   * 受講生一覧（キャンセル除く）検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生一覧(キャンセル除く全件)
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getActiveStudentDetailList() {
    return service.searchActiveStudentDetailList();
  }

  /**
   * 受講生検索です。キャンセルの有無を問わず、受講生の全件からIDに紐づく任意の受講生の詳細を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  @GetMapping("/student/{studentId}")
  public StudentDetail viewStudentDetail(@PathVariable("studentId") int studentId) {
    return service.searchstudentDetail(studentId);
  }

  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }


  @PostMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新成功！");
  }//バリデーションをつけたいときは、Exception Handlerというものを使う
}
