package raisetech.student.management.controller;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static raisetech.student.management.testutil.TestDataFactory.newDummyStudentDetail;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.exception.InvalidIdException;
import raisetech.student.management.exception.handling.ErrorDetailsBuilder;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


  @TestConfiguration
  static class MockConfig {

    @Bean
    public StudentService service() {
      return Mockito.mock(StudentService.class);
    }

    @Bean
    public ErrorDetailsBuilder errorDetailsBuilder() {
      return Mockito.mock(ErrorDetailsBuilder.class);
    }
  }

  @Test
  void アクティブ受講生一覧検索_サービスの処理が呼び出せていること() throws Exception {
    // Act
    mockMvc.perform(MockMvcRequestBuilders.get("/students"))
        .andExpect(status().isOk());

    // Assert
    Mockito.verify(service, times(1)).searchActiveStudentDetailList();
  }

  @Test
  void 受講生詳細単一検索成功_サービスの処理が適切に呼び出されること()
      throws Exception {
    // Arrange
    int studentId = 1;
    int courseId = 1;
    StudentDetail studentDetail = newDummyStudentDetail(studentId, courseId);
    Mockito.when(service.searchStudentDetail(studentId)).thenReturn(studentDetail);

    // Act
    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + studentId))
        .andExpect(status().isOk());

    // Assert
    Mockito.verify(service, times(1)).searchStudentDetail(studentId);
  }

  @Test
  void 受講生詳細単一検索失敗_サービスから例外を受け取りそのまま投げていること() throws Exception {
    // Arrange
    int studentId = 99;
    Mockito.when(service.searchStudentDetail(studentId))
        .thenThrow(new InvalidIdException(studentId));

    // Act
    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + studentId))
        .andExpect(status().is(404));

    // Assert
    Mockito.verify(service, times(1)).searchStudentDetail(studentId);
  }

  @Test
  void 受講生詳細単一検索失敗_studentIdに数値以外を渡すと例外が投げられること() throws Exception {
    // Arrange
    String studentId = "test";

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + studentId)) // 文字列を渡す
        .andExpect(status().isBadRequest()); // 400 BAD_REQUESTが返ること
  }

  @Test
  void 受講生詳細単一検索失敗_studentIdに0以下の数値を渡すと渡すと例外が投げられること()
      throws Exception {
    int notPositiveStudentId = 0;

    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + notPositiveStudentId))
        .andExpect(status().isBadRequest());

  }

  @Test
  void 古いエンドポイントに対するアクティブ受講生詳細一覧検索リクエスト_例外が返されること()
      throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/studentAndCourses"))
        .andExpect(status().isNotFound());
  }

//  @Test
//  void 受講生詳細登録成功_サービスの処理を呼び出す際に適切なリクエストボディが渡されていること() {
//
//  }
//
//  @Test
//  void 受講生詳細登録失敗_入力チェックにかかること() {
//
//  }
//
//  @Test
//  void 受講生詳細更新成功_サービスの処理を呼び出す際に適切なリクエストボディが渡されていること() {
//
//  }
//
//  @Test
//  void 受講生詳細更新失敗_入力チェックにかかること() {
//
//  }
//
}