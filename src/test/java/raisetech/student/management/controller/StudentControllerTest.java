package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.exception.TargetNotFoundException;
import raisetech.student.management.exception.handler.ErrorDetailsBuilder;
import raisetech.student.management.service.StudentService;
import raisetech.student.management.testutil.TestDataFactory;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  @MockitoBean
  private ErrorDetailsBuilder errorDetailsBuilder;

  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  ArgumentCaptor<StudentDetail> captor = ArgumentCaptor.forClass(StudentDetail.class);

  @Test
  void アクティブ受講生一覧検索_サービスの処理が呼び出せていること() throws Exception {
    // Act
    mockMvc.perform(MockMvcRequestBuilders.get("/students"))
        .andExpect(status().isOk());

    // Assert
    Mockito.verify(service, times(1)).searchStudentDetailList();
  }

  @Test
  void 受講生詳細単一検索成功_サービスの処理が呼び出せていること()
      throws Exception {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;
    StudentDetail studentDetail = TestDataFactory.makeCompletedStudentDetail(studentId, courseId);
    Mockito.when(service.searchStudentDetail(studentId)).thenReturn(studentDetail);

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + studentId))
        .andExpect(status().isOk());
    Mockito.verify(service, times(1)).searchStudentDetail(studentId);
  }

  @Test
  void 受講生詳細単一検索失敗_サービスから例外を受け取り404エラーを返していること() throws Exception {
    // Arrange
    int studentId = 99;
    Mockito.when(service.searchStudentDetail(studentId))
        .thenThrow(new TargetNotFoundException("studentId", "指定したIDの受講生は見つかりませんでした"));

    // Act
    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + studentId))
        .andExpect(status().isNotFound());

    // Assert
    Mockito.verify(service, times(1)).searchStudentDetail(studentId);
  }

  @Test
  void 受講生詳細単一検索失敗_studentIdに数値以外を渡すと400エラーが返されること()
      throws Exception {
    // Arrange
    String studentId = "test";

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + studentId)) // 文字列を渡す
        .andExpect(status().isBadRequest()); // 400 BAD_REQUESTが返ること
  }

  @Test
  void 受講生詳細単一検索失敗_studentIdに0以下の数値を渡すと渡すと400エラーが返されること()
      throws Exception {
    int notPositiveStudentId = 0;

    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + notPositiveStudentId))
        .andExpect(status().isBadRequest());

  }

  @Test
  void 受講生詳細登録成功_サービスにリクエストボディが正しく渡されていること()
      throws Exception {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;

    StudentDetail studentDetail = TestDataFactory.makeCompletedStudentDetail(studentId, courseId);
    Mockito.when(service.registerStudentDetail(Mockito.any(StudentDetail.class)))
        .thenReturn(studentDetail);

    // Act
    mockMvc.perform(MockMvcRequestBuilders.post("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isOk());

    // Assert
    Mockito.verify(service, times(1)).registerStudentDetail(captor.capture());
    assertThat(captor.getValue())
        .usingRecursiveComparison()
        .isEqualTo(studentDetail);
  }

  @Test
  void 受講生詳細更新成功_サービスにリクエストボディが正しく渡されていること()
      throws Exception {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;

    StudentDetail studentDetail = TestDataFactory.makeCompletedStudentDetail(studentId, courseId);
    Mockito.when(service.updateStudentDetail(Mockito.any(StudentDetail.class)))
        .thenReturn(studentDetail);

    // Act
    mockMvc.perform(MockMvcRequestBuilders.put("/students")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(studentDetail))
    ).andExpect(status().isOk());

    // Assert
    Mockito.verify(service, times(1)).updateStudentDetail(captor.capture());
    assertThat(captor.getValue())
        .usingRecursiveComparison()
        .isEqualTo(studentDetail);
  }

}