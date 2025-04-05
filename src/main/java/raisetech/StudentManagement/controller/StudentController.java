package raisetech.StudentManagement.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.data.domain.StudentDetail;
import raisetech.StudentManagement.data.domain.validation.OnCreate;
import raisetech.StudentManagement.data.domain.validation.OnUpdate;
import raisetech.StudentManagement.exception.InvalidAccessException;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * アクティブ受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return アクティブ受講生詳細一覧
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
  public StudentDetail viewStudentDetail(@PathVariable("studentId") @Positive int studentId) {
    return service.searchstudentDetail(studentId);
  }

  @GetMapping("/studentAndCourses")
  public ResponseEntity<StudentDetail> pastGetStudentDetails() throws InvalidAccessException {
    throw new InvalidAccessException(
        "現在無効なURLです。受講生一覧を見るには /studentList にアクセスしてください。");
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @PostMapping("/registerStudent")
  @Validated(OnCreate.class)
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。 キャンセルフラグの更新もここで行います(論理削除)
   *
   * @param studentDetail
   * @return 実行結果
   */
  @PutMapping("/updateStudent")
  @Validated(OnUpdate.class)
  public ResponseEntity<StudentDetail> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.updateStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }//バリデーションをつけたいときは、Exception Handlerというものを使う
}
