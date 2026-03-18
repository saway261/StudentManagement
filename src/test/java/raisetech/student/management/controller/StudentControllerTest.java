package raisetech.student.management.controller;

import static org.mockito.ArgumentMatchers.any;
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

  @Test
  void アクティブ受講生一覧検索_リクエスト時に200OKが返りサービスが呼び出されること() throws Exception {
    // Act
    mockMvc.perform(MockMvcRequestBuilders.get("/students"))
        .andExpect(status().isOk());

    // Assert
    Mockito.verify(service, times(1)).searchStudentDetailList();
  }

  @Test
  void 受講生詳細単一検索成功_存在するstudentIdを指定すると200OKが返りサービスが呼び出されること()
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
  void 受講生詳細単一検索失敗_studentIdに数値以外を渡すと400エラーが返されサービスが呼び出されないこと()
      throws Exception {
    // Arrange
    String studentId = "test";

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + studentId)) // 文字列を渡す
        .andExpect(status().isBadRequest()); // 400 BAD_REQUESTが返ること
    Mockito.verify(service, times(0)).searchStudentDetail(Mockito.anyInt());
  }

  @Test
  void 受講生詳細単一検索失敗_studentIdに0以下の数値を渡すと400エラーが返されサービスが呼び出されないこと()
      throws Exception {
    int notPositiveStudentId = 0;

    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + notPositiveStudentId))
        .andExpect(status().isBadRequest());
    Mockito.verify(service, times(0)).searchStudentDetail(Mockito.anyInt());
  }

  @Test void 受講生詳細登録成功_妥当なJSONリクエストで200OKが返りサービスが呼び出されること() throws Exception {
    // Act
    mockMvc.perform(MockMvcRequestBuilders.post("/students")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
            """
            {
                "student": {
                    "fullName": "山田太郎",
                    "kanaName": "やまだたろう",
                    "nickname": "タロー",
                    "email": "taro@email.com",
                    "area": "東京都練馬区",
                    "telephone": "090-0000-0000",
                    "age": 20,
                    "sex": "男",
                    "remark": "特になし"
                },
                "studentCourses": [
                    {
                         "courseName": "Javaコース"
                    }
                ]
            }
            
            """
        ))
        .andExpect(status().isOk());
    // Assert
    Mockito.verify(service, times(1)).registerStudentDetail(any());
  }

  @Test
  void 受講生詳細登録失敗_不正なJSONリクエストを受け取ると400エラーが返されサービスが呼び出されないこと()
      throws Exception {
    // Act
    mockMvc.perform(MockMvcRequestBuilders.post("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "student": {},
                    "studentCourses": []
                }
                """
            ))
        .andExpect(status().isBadRequest());

    // Assert
    Mockito.verify(service, times(0)).registerStudentDetail(any(StudentDetail.class));
  }

  @Test
  void 受講生詳細更新成功_妥当なJSONリクエストで200OKが返りサービスが呼び出されること()
      throws Exception {
    // Arrange
    Mockito.when(service.updateStudentDetail(any(StudentDetail.class)))
        .thenReturn(TestDataFactory.makeCompletedStudentDetail(1, 1));

    // Act
    mockMvc.perform(MockMvcRequestBuilders.put("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "student": {
                        "studentId": 1,
                        "fullName": "山田太郎",
                        "kanaName": "やまだたろう",
                        "nickname": "タロー",
                        "email": "taro@email.com",
                        "area": "東京都練馬区",
                        "telephone": "090-0000-0000",
                        "age": 20,
                        "sex": "男",
                        "remark": "特になし",
                        "isDeleted": false
                    },
                    "studentCourses": [
                        {
                            "courseId": 1,
                            "studentId": 1,
                            "courseName": "Javaコース",
                            "courseStartAt": "2025-04-01",
                            "courseEndAt": "2025-10-01",
                            "deleted": false
                        }
                    ]
                }
                """
            ))
        .andExpect(status().isOk());

    // Assert
    Mockito.verify(service, times(1)).updateStudentDetail(any(StudentDetail.class));
  }

  @Test
  void 受講生詳細更新失敗_不正なJSONリクエストを受け取ると400エラーが返されサービスが呼び出されないこと()
      throws Exception {
    // Act
    mockMvc.perform(MockMvcRequestBuilders.put("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "student": {},
                    "studentCourses": []
                }
                """
            ))
        .andExpect(status().isBadRequest());

    // Assert
    Mockito.verify(service, times(0)).updateStudentDetail(any(StudentDetail.class));
  }

  @Test
  void 受講生詳細更新失敗_存在しない受講生を更新しようとすると404エラーが返ること()
      throws Exception {
    // Arrange
    Mockito.when(service.updateStudentDetail(any(StudentDetail.class)))
        .thenThrow(new TargetNotFoundException("Student.studentId", "更新対象のインスタンスが見つかりませんでした"));

    // Act
    mockMvc.perform(MockMvcRequestBuilders.put("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "student": {
                        "studentId": 99,
                        "fullName": "山田太郎",
                        "kanaName": "やまだたろう",
                        "nickname": "タロー",
                        "email": "taro@email.com",
                        "area": "東京都練馬区",
                        "telephone": "090-0000-0000",
                        "age": 20,
                        "sex": "男",
                        "remark": "特になし",
                        "isDeleted": false
                    },
                    "studentCourses": [
                        {
                            "courseId": 99,
                            "studentId": 99,
                            "courseName": "Javaコース",
                            "courseStartAt": "2025-04-01",
                            "courseEndAt": "2025-10-01",
                            "deleted": false
                        }
                    ]
                }
                """
            ))
        .andExpect(status().isNotFound());

    // Assert
    Mockito.verify(service, times(1)).updateStudentDetail(any(StudentDetail.class));
  }

}