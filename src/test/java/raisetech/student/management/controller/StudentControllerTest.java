package raisetech.student.management.controller;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
  }

  @Test
  void アクティブ受講生の一覧検索ができて空のリストが返ってくること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/students"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    Mockito.verify(service, times(1)).searchActiveStudentDetailList();
  }

}