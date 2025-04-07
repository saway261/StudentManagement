package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.domain.validation.OnCreate;
import raisetech.student.management.data.domain.validation.OnUpdate;
import raisetech.student.management.exception.InvalidAccessException;
import raisetech.student.management.exception.InvalidIdException;
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

  @Operation(
      summary = "アクティブな受講生詳細一覧の検索",
      description = "アクティブな受講生詳細の一覧を検索します。全件検索を行うので、条件指定はしません",
      responses = {@ApiResponse(
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = StudentDetail.class))
          )
      )}
  )
  @GetMapping("/students")
  public List<StudentDetail> getActiveStudentDetailList() {
    return service.searchActiveStudentDetailList();
  }

  @Operation(
      summary = "受講生詳細検索",
      description = "アクティブ・非アクティブを問わず、受講生詳細の全件から受講生IDが一致する受講生の詳細を取得します。",
      parameters = {
          @Parameter(description = "検索したい受講生ID", required = true, schema = @Schema(type = "int"))},
      responses = {@ApiResponse(
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = StudentDetail.class))
          ))}
  )
  @GetMapping("/students/{studentId}")
  public StudentDetail viewStudentDetail(@PathVariable("studentId") @Positive int studentId)
      throws InvalidIdException {
    if (service.isExistStudentId(studentId)) {
      return service.searchstudentDetail(studentId);
    } else {
      throw new InvalidIdException(studentId);
    }
  }

  @Operation(
      summary = "アクティブな受講生詳細一覧の検索(旧ver・非使用)",
      description = "現在使われていないURIです。エラーを返し、正しいアドレスへのアクセスを促します",
      responses = {
          @ApiResponse(responseCode = "404", description = "not found")
      }
  )
  @GetMapping("/studentAndCourses")
  public ResponseEntity<StudentDetail> pastGetStudentDetails() throws InvalidAccessException {
    throw new InvalidAccessException();
  }

  @Operation(
      summary = "受講生詳細登録",
      description = "受講生の登録を行います",
      parameters = {
          @Parameter(description = "登録したい受講生詳細", required = true, schema = @Schema(implementation = StudentDetail.class))},
      responses = {@ApiResponse(
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = StudentDetail.class))
          )
      )}
  )
  @PostMapping("/students")
  @Validated(OnCreate.class)
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  @Operation(
      summary = "受講生詳細更新",
      description = "受講生詳細の更新を行います。キャンセルフラグの更新(アクティブ⇔非アクティブ)もここで行います。受講生IDが登録されていない、または受講生コースIDがひとつでも受講生IDと紐づかない場合はエラーを返します。",
      parameters = {
          @Parameter(description = "更新したい受講生詳細", required = true, schema = @Schema(implementation = StudentDetail.class))},
      responses = {
          @ApiResponse(
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = StudentDetail.class)))),
          @ApiResponse(
              responseCode = "400", description = "id not exist"
          )
      }
  )
  @PutMapping("/students")
  @Validated(OnUpdate.class)
  public ResponseEntity<StudentDetail> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail)
      throws InvalidIdException {

    int studentId = studentDetail.getStudent().getStudentId();

    if (!service.isExistStudentId(studentId)) {
      throw new InvalidIdException(studentDetail.getStudent());
    }
    for (StudentCourse course : studentDetail.getStudentCourseList()) {
      if (!service.isLinkedCourseIdWithStudentId(studentId, course.getCourseId())) {
        throw new InvalidIdException(course);
      }
    }

    StudentDetail responseStudentDetail = service.updateStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);

  }
}
