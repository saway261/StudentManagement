package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.master.Course;
import raisetech.student.management.exception.handler.ErrorResponse;
import raisetech.student.management.service.CourseService;

@RestController
public class CourseController {

  private CourseService service;

  @Autowired
  public CourseController(CourseService service) {
    this.service = service;
  }

  @Operation(
      summary = "提供コース一覧の検索",
      description = "コースマスタに存在する提供コースの一覧を検索します。全件検索を行うので、条件指定はしません",
      responses = {@ApiResponse(
          content = @Content(mediaType = "application/json",
          array = @ArraySchema(schema= @Schema(implementation = Course.class)))
      )}
  )
  @GetMapping("/courses")
  public List<Course> getCourseList(){
    return service.searchCourseMasterList();
  }

  @Operation(
      summary = "提供コース登録",
      description = "提供コース登録を行います",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "新規に追加したいコース名とその識別子",
          required = true,
          content = @Content(
              schema = @Schema(implementation = Course.class)
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "200", description = "ok",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Course.class)
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
  @PostMapping("/courses")
  public void registerCourse(@RequestBody @Validated Course course){
    service.registerCourse(course);
  }
}
