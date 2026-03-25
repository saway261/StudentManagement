package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.master.Course;
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
}
