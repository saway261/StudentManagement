package raisetech.student.management.controller;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.student.management.exception.handler.ErrorDetailsBuilder;
import raisetech.student.management.service.CourseService;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CourseService service;

  @MockitoBean
  private ErrorDetailsBuilder errorDetailsBuilder;

  @Test
  void コース一覧検索_リクエスト時に200OKが返りサービスが呼び出されること() throws Exception {
    // Act
    mockMvc.perform(MockMvcRequestBuilders.get("/courses"))
        .andExpect(status().isOk());

    // Assert
    Mockito.verify(service, times(1)).searchCourseMasterList();
  }

}