package raisetech.student.management.controller;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.student.management.exception.handling.ErrorDetailsBuilder;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private StudentService service;

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
    mockMvc.perform(MockMvcRequestBuilders.get("/students"))
        .andExpect(status().isOk());

    Mockito.verify(service, times(1)).searchActiveStudentDetailList();
  }
//
//  @Test
//  void 受講生詳細単一検索バリデーションエラー_受講生IDに数値以外を渡したときに入力チェックにかかること() {
//    Student student = new Student();
//  }
//
//  @Test
//  void 受講生詳細単一検索成功_サービスの処理が適切に呼び出されStudentDetailが返ってくること()
//      throws Exception {
//    // 前提
//    int studentId = 1;
//
//    mockMvc.perform(MockMvcRequestBuilders.get("/students/{studentId}"))
//        .andExpect(status().isOk());
//    Mockito.verify(service, times(1)).searchStudentDetail(studentId);
//  }
//
//  @Test
//  void 受講生詳細単一検索失敗_サービスから例外を受け取りそのまま投げていること() {
//
//  }
//
//  @Test
//  void 古いエンドポイントに対するアクティブ受講生詳細一覧検索リクエスト_例外が返されること() {
//
//  }
//
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