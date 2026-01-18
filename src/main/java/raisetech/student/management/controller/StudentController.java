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

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。全件検索を行うので条件指定は行いません。
   * @return 受講生詳細一覧
   */
  @GetMapping("/students")
  public List<StudentDetail> getStudentList(){
    return service.serchStudentDetailList();
  }

  /**
   * 受講生詳細の検索です。IDに紐づく任意の受講生詳細を取得します。
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  @GetMapping("/students/{studentId}")
  public StudentDetail getStudent(@PathVariable int studentId){
    return service.searchStudentDetail(studentId);
  }

  /**
   * 受講生詳細の新規登録です。リクエストボディとして受講生詳細を受け取ります。
   * @param studentDetail 受講生詳細
   * @return 登録後の受講生詳細
   */
  @PostMapping("/students")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail){
    StudentDetail responseStudentDetail = service.registerStudentDetail(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新です。リクエストボディとして受講生詳細を受け取ります。削除フラグの更新(論理削除)もここで行います。
   * @param studentDetail 受講生詳細
   * @return 更新後の受講生詳細
   */
  @PutMapping("/students")
  public ResponseEntity<StudentDetail> updateStudent(@RequestBody StudentDetail studentDetail){
    StudentDetail responseStudentDetail = service.updateStudentDetail(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

}
