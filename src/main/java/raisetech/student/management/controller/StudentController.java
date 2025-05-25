package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;
import raisetech.student.management.exception.InvalidAccessException;
import raisetech.student.management.exception.InvalidIdException;
import raisetech.student.management.exception.handling.ErrorResponseBody;
import raisetech.student.management.service.StudentService;
import raisetech.student.management.web.form.StudentDetailForm;
import raisetech.student.management.web.response.StudentDetailResponse;

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
              array = @ArraySchema(schema = @Schema(implementation = StudentDetailResponse.class))
          )
      )}
  )
  @GetMapping("/students")
  public List<StudentDetailResponse> getActiveStudentDetailList() {
    return service.searchActiveStudentDetailList();
  }

  @Operation(
      summary = "受講生詳細検索",
      description = "アクティブ・非アクティブを問わず、受講生詳細の全件から受講生IDが一致する受講生の詳細を取得します。",
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
                  schema = @Schema(implementation = StudentDetailResponse.class)
              )),
          @ApiResponse(
              responseCode = "404", description = "指定された受講生IDが存在しなかったときのエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponseBody.class)
              )),
          @ApiResponse(
              responseCode = "400", description = "受講生IDの形式が不正であったときのエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponseBody.class)
              ))
      }
  )
  @GetMapping("/students/{studentId}")
  public StudentDetailResponse viewStudentDetail(
      @PathVariable("studentId") @Positive int studentIdNumber)
      throws InvalidIdException {
    return service.searchStudentDetail(studentIdNumber);
  }

  @Operation(
      summary = "アクティブな受講生詳細一覧の検索(旧ver・非使用)",
      description = "現在使われていないURIです。エラーを返し、正しいアドレスへのアクセスを促します",
      responses = {
          @ApiResponse(responseCode = "404", description = "not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponseBody.class)
              ))
      }
  )
  @GetMapping("/studentAndCourses")
  public ResponseEntity<StudentDetail> pastGetStudentDetails() throws InvalidAccessException {
    throw new InvalidAccessException();
  }

  @Operation(
      summary = "受講生詳細登録",
      description = "受講生の登録を行います",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "新規に登録したい受講生詳細",
          required = true,
          content = @Content(
              schema = @Schema(implementation = StudentDetailForm.class)
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "200", description = "ok",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetailResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "400", description = "入力値のバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponseBody.class))
          )
      }
  )
  @PostMapping("/students")
  @Validated(OnRegister.class)
  public ResponseEntity<StudentDetailResponse> registerStudentDetail(
      @RequestBody @Valid StudentDetailForm form) {
    return ResponseEntity.ok(service.registerStudentDetail(form));
  }

  @Operation(
      summary = "受講生詳細更新",
      description = "受講生詳細の更新を行います。キャンセルフラグの更新(アクティブ⇔非アクティブ)もここで行います。受講生IDが登録されていない、または受講生コースIDがひとつでも受講生IDと紐づかない場合はエラーを返します。",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "更新したい受講生詳細",
          required = true,
          content = @Content(
              schema = @Schema(implementation = StudentDetailForm.class)
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "200", description = "ok",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetailResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "400", description = "入力値のバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponseBody.class))
          ),
          @ApiResponse(
              responseCode = "404", description = "指定された受講生IDが存在しないか、受講生コースIDが受講生IDに紐づかないときのエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponseBody.class))
          )
      }
  )
  @PutMapping("/students")
  @Validated(OnUpdate.class)
  public ResponseEntity<StudentDetailResponse> updateStudentDetail(
      @RequestBody @Valid StudentDetailForm form) throws InvalidIdException {
    return ResponseEntity.ok(service.updateStudentDetail(form));
  }

}
