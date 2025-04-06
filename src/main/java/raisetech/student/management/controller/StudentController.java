package raisetech.student.management.controller;

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
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.domain.validation.OnCreate;
import raisetech.student.management.data.domain.validation.OnUpdate;
import raisetech.student.management.exception.InvalidAccessException;
import raisetech.student.management.exception.NotExistIdException;
import raisetech.student.management.service.StudentService;

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
  @GetMapping("/students")
  public List<StudentDetail> getActiveStudentDetailList() {
    return service.searchActiveStudentDetailList();
  }

  /**
   * 受講生検索です。キャンセルの有無を問わず、受講生の全件からIDに紐づく任意の受講生の詳細を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  @GetMapping("/students/{studentId}")
  public StudentDetail viewStudentDetail(@PathVariable("studentId") @Positive int studentId) {
    return service.searchstudentDetail(studentId);
  @GetMapping("/student/{studentId}")
  public StudentDetail viewStudentDetail(@PathVariable("studentId") @Positive int studentId)
      throws NotExistIdException {
    if (service.isExistStudentId(studentId)) {
      return service.searchstudentDetail(studentId);
    } else {
      throw new NotExistIdException("この受講生IDは登録されていません");
    }
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
  @PostMapping("/students")
  @Validated(OnCreate.class)
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。
   * キャンセルフラグの更新もここで行います(論理削除)、受講生IDが登録されていない、または受講生コースIDがひとつでも受講生IDと紐づかない場合は例外を返します。
   *
   * @param studentDetail
   * @return 実行結果
   */
  @PutMapping("/students")
  @Validated(OnUpdate.class)
  public ResponseEntity<StudentDetail> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) throws NotExistIdException {

    if (!service.isExistStudentId(studentDetail.getStudent().getStudentId())) {
      throw new NotExistIdException("この受講生IDは登録されていません");
    }
    if (!service.isLinkedCourseIdWithStudentId(studentDetail)) {
      throw new NotExistIdException("受講生IDと紐づかないコースIDがあります");
    }

    StudentDetail responseStudentDetail = service.updateStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);

  }
}
