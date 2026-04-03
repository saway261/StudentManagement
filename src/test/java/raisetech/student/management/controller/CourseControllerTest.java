package raisetech.student.management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.student.management.data.master.Course;
import raisetech.student.management.exception.TargetNotFoundException;
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

  @Test
  void コース登録成功_妥当なJSONリクエストで201Createdが返りサービスが呼び出されること() throws Exception{
    // Act
    mockMvc.perform(MockMvcRequestBuilders.post("/courses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
            """
            {
                "courseCode": "JA",
                "courseName": "Javaコース"
            }
            """
        ))
        .andExpect(status().isCreated());

    // Assert
    Mockito.verify(service, times(1)).registerCourse(any());

  }

  @Test
  void コース登録失敗_不正なJSONリクエストを受け取ると400エラーが返されサービスが呼び出されないこと() throws Exception{
    // Act
    mockMvc.perform(MockMvcRequestBuilders.post("/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isBadRequest());

    // Assert
    Mockito.verify(service, never()).registerCourse(any());

  }

  @Test
  void コース更新成功_妥当なJSONリクエストで200OKが返りサービスが呼び出されること() throws Exception{
    // Act
    mockMvc.perform(MockMvcRequestBuilders.put("/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "courseCode": "JA",
                    "courseName": "Javaコース"
                }
                """
            ))
        .andExpect(status().isOk());

    // Assert
    Mockito.verify(service, times(1)).updateCourse(any());

  }

  @Test
  void コース更新失敗_不正なJSONリクエストを受け取ると400エラーが返されサービスが呼び出されないこと() throws Exception{
    // Act
    mockMvc.perform(MockMvcRequestBuilders.put("/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isBadRequest());

    // Assert
    Mockito.verify(service, never()).updateCourse(any());
  }

  @Test
  void コース更新失敗_存在しないコースコードを指定してサービスから例外が返ると404エラーを返すこと() throws Exception{
    // Arrange
    Mockito.doThrow(new TargetNotFoundException("courseCode", "更新対象のコースが見つかりませんでした"))
        .when(service).updateCourse(any(Course.class));

    // Act
    mockMvc.perform(MockMvcRequestBuilders.put("/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "courseCode": "NOT",
                    "courseName": "存在しないコース"
                }
                """
            ))
        .andExpect(status().isNotFound());

    // Assert
    Mockito.verify(service, times(1)).updateCourse(any(Course.class));
  }

}