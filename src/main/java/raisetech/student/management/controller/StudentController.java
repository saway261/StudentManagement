package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.exception.handler.ErrorResponse;
import raisetech.student.management.service.StudentService;
import raisetech.student.management.validation.CreateGroup;
import raisetech.student.management.validation.UpdateGroup;

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
      summary = "受講生詳細一覧の検索",
      description = "受講生詳細の一覧を検索します。全件検索を行うので、条件指定はしません",
      responses = {@ApiResponse(
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = StudentDetail.class))
          )
      )}
  )
  @GetMapping("/students")
  public List<StudentDetail> getStudentList(){
    return service.searchStudentDetailList();
  }

  @Operation(
      summary = "受講生詳細検索",
      description = "受講生詳細の全件から受講生IDが一致する受講生の詳細を取得します。",
      parameters = {
          @Parameter(in = ParameterIn.PATH,
              name = "studentId", required = true,
              description = "受講生ID",
              schema = @Schema(
                  type = "integer",
                  format = "int32"
              )
          )},
      responses = {
          @ApiResponse(
              responseCode = "200", description = "ok",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class)
              )),
          @ApiResponse(
              responseCode = "404", description = "指定された受講生IDが存在しなかったときのエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )),
          @ApiResponse(
              responseCode = "400", description = "受講生IDの形式が不正であったときのエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              ))
      }
  )
  @GetMapping("/students/{studentId}")
  public StudentDetail getStudent(@PathVariable @Positive int studentId){
    return service.searchStudentDetail(studentId);
  }

  @Operation(
      summary = "受講生詳細登録",
      description = "受講生の登録を行います",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "新規に登録したい受講生詳細",
          required = true,
          content = @Content(
              schema = @Schema(implementation = StudentDetail.class)
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "201", description = "ok",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class)
              )
          ),
          @ApiResponse(
              responseCode = "400", description = "入力値のバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          )
      }
  )
  @PostMapping("/students")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Validated(CreateGroup.class) StudentDetail studentDetail){
    StudentDetail responseStudentDetail = service.registerStudentDetail(studentDetail);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseStudentDetail);
  }

  @Operation(
      summary = "受講生更新",
      description = "受講生の更新を行います。キャンセルフラグの更新(アクティブ⇔非アクティブ)もここで行います。受講生IDが登録されていない場合はエラーを返します。",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "更新したい受講生詳細",
          required = true,
          content = @Content(
              schema = @Schema(implementation = Student.class)
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "200", description = "更新成功",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Student.class)
              )
          ),
          @ApiResponse(
              responseCode = "400", description = "入力値のバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          ),
          @ApiResponse(
              responseCode = "404", description = "指定された受講生IDが存在しないときのエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          )
      }
  )
  @PutMapping("/students")
  public ResponseEntity<Student> updateStudent(@RequestBody @Validated(UpdateGroup.class) Student request){
    Student response = service.updateStudent(request);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "受講生コース追加",
      description = "登録済みの受講生の受講コースを追加します",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "新規に追加したい受講生コース情報",
          required = true,
          content = @Content(
              schema = @Schema(implementation = StudentCourse.class)
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "201", description = "ok",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentCourse.class)
              )
          ),
          @ApiResponse(
              responseCode = "400", description = "入力値のバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          ),
          @ApiResponse(
              responseCode = "404", description = "指定された受講生IDが存在しないときのエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          )
      }
  )
  @PostMapping("/students/{studentId}/courses")
  public ResponseEntity<StudentCourse> addStudentCourse(
      @PathVariable @Positive int studentId,
      @RequestBody @Validated(CreateGroup.class) StudentCourse request
  ){
    StudentCourse response = service.registerStudentCourse(request,studentId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(
      summary = "受講生コース更新",
      description = "受講生コース更新を行います。ステータスのみを更新します",
      parameters = {
          @Parameter(in = ParameterIn.PATH,
              name = "studentId", required = true,
              description = "受講生ID",
              schema = @Schema(
                  type = "integer",
                  format = "int32"
              )
          )},
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "更新したい受講生コース情報",
          required = true,
          content = @Content(
              schema = @Schema(implementation = StudentCourse.class)
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "200", description = "更新成功",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentCourse.class)
              )
          ),
          @ApiResponse(
              responseCode = "400", description = "入力値のバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          ),
          @ApiResponse(
              responseCode = "404", description = "指定された受講生コースIDが存在しないか、受講生IDと紐づかないときのエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          )
      }
  )
  @PutMapping("/students/{studentId}/courses")
  public ResponseEntity<StudentCourse> updateStudentCourse(
      @PathVariable @Positive int studentId,
      @RequestBody @Validated(UpdateGroup.class) StudentCourse request){
    StudentCourse response = service.updateStudentCourse(request,studentId);
    return ResponseEntity.ok(response);
  }

}
