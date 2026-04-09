package raisetech.student.management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.exception.InvalidSearchCriteriaException;
import raisetech.student.management.exception.TargetNotFoundException;
import raisetech.student.management.exception.handler.ErrorDetailsBuilder;
import raisetech.student.management.repository.CourseRepository;
import raisetech.student.management.search.request.SearchableField;
import raisetech.student.management.search.request.StudentSimpleSearchRequest;
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

  @MockitoBean
  private CourseRepository courseRepository;

  @Test
  void 受講生詳細簡易検索成功_条件未指定で200OKが返り空のリクエストがサービスに渡されること() throws Exception {
    // Act
    mockMvc.perform(MockMvcRequestBuilders.get("/students"))
        .andExpect(status().isOk());

    // Assert
    ArgumentCaptor<StudentSimpleSearchRequest> captor =
        ArgumentCaptor.forClass(StudentSimpleSearchRequest.class);
    Mockito.verify(service, times(1)).searchStudentDetailsSimple(captor.capture());

    StudentSimpleSearchRequest actual = captor.getValue();
    Assertions.assertNull(actual.getFullNameContains());
    Assertions.assertNull(actual.getKanaNameContains());
    Assertions.assertNull(actual.getAreaContains());
    Assertions.assertNull(actual.getAgeMin());
    Assertions.assertNull(actual.getAgeMax());
    Assertions.assertNull(actual.getSexEq());
    Assertions.assertNull(actual.getCourseCode());
    Assertions.assertNull(actual.getStatusId());
    Assertions.assertNull(actual.getApplyFrom());
    Assertions.assertNull(actual.getApplyTo());
    Assertions.assertNull(actual.getStartFrom());
    Assertions.assertNull(actual.getStartTo());
    Assertions.assertNull(actual.getIsDeleted());
  }

  @Test
  void 受講生詳細簡易検索成功_クエリパラメータがリクエストオブジェクトにバインドされサービスに渡されること()
      throws Exception {
    // Act
    mockMvc.perform(MockMvcRequestBuilders.get("/students")
            .param("fullNameContains", "田中")
            .param("kanaNameContains", "たなか")
            .param("areaContains", "東京")
            .param("ageMin", "30")
            .param("ageMax", "40")
            .param("sexEq", "女")
            .param("courseCode", "JA")
            .param("statusId", "1", "3")
            .param("applyFrom", "2026-01-01")
            .param("applyTo", "2026-03-31")
            .param("startFrom", "2026-02-01")
            .param("startTo", "2026-04-30")
            .param("isDeleted", "true"))
        .andExpect(status().isOk());

    // Assert
    ArgumentCaptor<StudentSimpleSearchRequest> captor =
        ArgumentCaptor.forClass(StudentSimpleSearchRequest.class);
    Mockito.verify(service, times(1)).searchStudentDetailsSimple(captor.capture());

    StudentSimpleSearchRequest actual = captor.getValue();
    Assertions.assertEquals("田中", actual.getFullNameContains());
    Assertions.assertEquals("たなか", actual.getKanaNameContains());
    Assertions.assertEquals("東京", actual.getAreaContains());
    Assertions.assertEquals(30, actual.getAgeMin());
    Assertions.assertEquals(40, actual.getAgeMax());
    Assertions.assertEquals("女", actual.getSexEq());
    Assertions.assertEquals("JA", actual.getCourseCode());
    Assertions.assertEquals(List.of(1, 3), actual.getStatusId());
    Assertions.assertEquals(LocalDate.of(2026, 1, 1), actual.getApplyFrom());
    Assertions.assertEquals(LocalDate.of(2026, 3, 31), actual.getApplyTo());
    Assertions.assertEquals(LocalDate.of(2026, 2, 1), actual.getStartFrom());
    Assertions.assertEquals(LocalDate.of(2026, 4, 30), actual.getStartTo());
    Assertions.assertTrue(actual.getIsDeleted());
  }

  @Test
  void 受講生詳細ID単一検索成功_存在するstudentIdを指定すると200OKが返りサービスが呼び出されること()
      throws Exception {
    // Arrange
    Integer studentId = 1;
    Integer scId = 1;
    StudentDetail studentDetail = TestDataFactory.makeCompletedStudentDetail(studentId, scId);
    Mockito.when(service.searchStudentDetail(studentId)).thenReturn(studentDetail);

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + studentId))
        .andExpect(status().isOk());
    Mockito.verify(service, times(1)).searchStudentDetail(studentId);
  }

  @Test
  void 受講生詳細ID単一検索失敗_サービスから例外を受け取り404エラーを返していること() throws Exception {
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
  void 受講生詳細ID単一検索失敗_studentIdに数値以外を渡すと400エラーが返されサービスが呼び出されないこと()
      throws Exception {
    // Arrange
    String studentId = "test";

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + studentId)) // 文字列を渡す
        .andExpect(status().isBadRequest()); // 400 BAD_REQUESTが返ること
    Mockito.verify(service, never()).searchStudentDetail(Mockito.anyInt());
  }

  @Test
  void 受講生詳細ID単一検索失敗_studentIdに0以下の数値を渡すと400エラーが返されサービスが呼び出されないこと()
      throws Exception {
    int notPositiveStudentId = 0;

    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + notPositiveStudentId))
        .andExpect(status().isBadRequest());
    Mockito.verify(service, never()).searchStudentDetail(Mockito.anyInt());
  }

  @Test
  void 受講生詳細高度検索成功_妥当なJSONリクエストで200OKが返りサービスが呼び出されること()
      throws Exception {
    // Act
    mockMvc.perform(MockMvcRequestBuilders.post("/students/search")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "filters": [
                        {
                            "field": "fullName",
                            "operator": "EQ",
                            "value": "田中太郎"
                        }
                    ]
                }
                """
            ))
        .andExpect(status().isOk());

    // Assert
    Mockito.verify(service, times(1)).searchStudentDetailsAdvanced(any());
  }

  @Test
  void 受講生詳細高度検索失敗_不正なJSONリクエストを受け取ると400エラーが返されサービスが呼び出されないこと()
      throws Exception {
    // Act
    // HttpMassageNotReadableExceptionが発生して400エラーになることを検証
    mockMvc.perform(MockMvcRequestBuilders.post("/students/search")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isBadRequest());

    // Assert
    Mockito.verify(service, never()).searchStudentDetailsAdvanced(any());
  }

  @Test
  void 受講生詳細高度検索失敗_不正な検索フィルタを受け取ると400エラーが返されサービスが呼び出されないこと()
      throws Exception {
    // Act
    // @ValidSearchFilterにかかってバリデーションエラーになると400エラーになることを検証
    mockMvc.perform(MockMvcRequestBuilders.post("/students/search")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "filters": [
                        {
                            "field": "unknownField",
                            "operator": "EQ",
                            "value": "test"
                        }
                    ]
                }
                """
            ))
        .andExpect(status().isBadRequest());

    // Assert
    Mockito.verify(service, never()).searchStudentDetailsAdvanced(any());
  }

  @Test
  void 受講生詳細高度検索失敗_サービス層でcriteriaへの変換時に例外が投げられると400エラーが返されること()
      throws Exception {
    // Arrange
    Mockito.when(service.searchStudentDetailsAdvanced(any()))
        .thenThrow(new InvalidSearchCriteriaException(SearchableField.COURSE_CODE,"許可されていない演算子です"));
    // Act
    mockMvc.perform(MockMvcRequestBuilders.post("/students/search")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "filters": [
                        {
                            "field": "courseCode",
                            "operator": "STARTS_WITH",
                            "value": "JA"
                        }
                    ]
                }
                """
            ))
        .andExpect(status().isBadRequest());

    // Assert
    Mockito.verify(service, times(1)).searchStudentDetailsAdvanced(any());
  }

  @Test void 受講生詳細登録成功_妥当なJSONリクエストで201Createdが返りサービスが呼び出されること() throws Exception {
    // Arrange
    Mockito.when(courseRepository.existsByCourseCode("JA")).thenReturn(true);

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
                         "courseCode": "JA"
                    }
                ]
            }
            
            """
        ))
        .andExpect(status().isCreated());

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
    Mockito.verify(service, never()).registerStudentDetail(any(StudentDetail.class));
  }

  @Test
  void 受講生更新成功_妥当なJSONリクエストで200OKが返りサービスが呼び出されること()
      throws Exception {
    // Arrange
    Mockito.when(service.updateStudent(any(Student.class)))
        .thenReturn(TestDataFactory.makeCompletedStudent(1));

    // Act
    mockMvc.perform(MockMvcRequestBuilders.put("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
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
                }
                """
            ))
        .andExpect(status().isOk());

    // Assert
    Mockito.verify(service, times(1)).updateStudent(any(Student.class));
  }

  @Test
  void 受講生更新失敗_不正なJSONリクエストを受け取ると400エラーが返されサービスが呼び出されないこと()
      throws Exception {
    // Act
    mockMvc.perform(MockMvcRequestBuilders.put("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isBadRequest());

    // Assert
    Mockito.verify(service, never()).updateStudent(any(Student.class));
  }

  @Test
  void 受講生更新失敗_存在しない受講生を更新しようとすると404エラーが返ること()
      throws Exception {
    // Arrange
    Mockito.when(service.updateStudent(any(Student.class)))
        .thenThrow(new TargetNotFoundException("Student.studentId", "更新対象のインスタンスが見つかりませんでした"));

    // Act
    mockMvc.perform(MockMvcRequestBuilders.put("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
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
                }
                """
            ))
        .andExpect(status().isNotFound());

    // Assert
    Mockito.verify(service, times(1)).updateStudent(any(Student.class));
  }

  @Test
  void 受講生コース追加成功_妥当なリクエストで201Createdが返りサービスが呼び出されること() throws Exception {
    // Arrange
    Integer studentId = 1;
    Mockito.when(courseRepository.existsByCourseCode("JA")).thenReturn(true);

    // Act
    mockMvc.perform(MockMvcRequestBuilders.post("/students/" + studentId + "/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                     "courseCode": "JA"
                }
                """
            ))
        .andExpect(status().isCreated());

    // Assert
    Mockito.verify(service, times(1)).registerStudentCourse(any(StudentCourse.class),eq(studentId));
  }

  @Test
  void 受講生コース追加失敗_登録されていない受講生IDを指定すると404が返ること() throws Exception {
    // Arrange
    Integer studentId = 99;
    Mockito.when(courseRepository.existsByCourseCode("JA")).thenReturn(true);
    Mockito.when(service.registerStudentCourse(any(StudentCourse.class),eq(studentId)))
        .thenThrow(TargetNotFoundException.class);

    // Act
    mockMvc.perform(MockMvcRequestBuilders.post("/students/" + studentId + "/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                     "courseCode": "JA"
                }
                """
            ))
        .andExpect(status().isNotFound());

    // Assert
    Mockito.verify(service, times(1)).registerStudentCourse(any(StudentCourse.class),eq(studentId));
  }

  @Test
  void 受講生コース追加失敗_不正なJSONリクエストを受け取ると400エラーが返されサービスが呼び出されないこと()
      throws Exception {
    // Arrange
    Integer studentId = 1;

    // Act
    mockMvc.perform(MockMvcRequestBuilders.post("/students/" + studentId + "/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isBadRequest());

    // Assert
    Mockito.verify(service, never()).registerStudentCourse(any(),eq(studentId));
  }

  @Test
  void 受講生コース更新成功_妥当なJSONリクエストで200OKが返りサービスが呼び出されること()
      throws Exception {
    // Arrange
    Integer studentId = 1;
    Integer scId = 1;
    Mockito.when(service.updateStudentCourse(any(StudentCourse.class),eq(studentId)))
        .thenReturn(TestDataFactory.makeCompletedStudentCourse(studentId,scId));

    // Act
    mockMvc.perform(MockMvcRequestBuilders.put("/students/" + studentId +"/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """               
                {
                    "studentCourseId": 1,
                    "statusId": 2
                }
                """
            ))
        .andExpect(status().isOk());

    // Assert
    Mockito.verify(service, times(1)).updateStudentCourse(any(StudentCourse.class),eq(studentId));
  }

  @Test
  void 受講生コース更新失敗_不正なJSONリクエストを受け取ると400エラーが返されサービスが呼び出されないこと()
      throws Exception {
    // Arrange
    Integer studentId = 1;
    // Act
    mockMvc.perform(MockMvcRequestBuilders.put("/students/" + studentId +"/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isBadRequest());

    // Assert
    Mockito.verify(service, never()).updateStudentCourse(any(StudentCourse.class),eq(studentId));
  }

  @Test
  void 受講生コース更新失敗_登録されていない受講生IDを指定すると404が返ること() throws Exception {
    // Arrange
    Integer studentId = 99;
    Mockito.when(service.updateStudentCourse(any(StudentCourse.class),eq(studentId)))
        .thenThrow(TargetNotFoundException.class);

    // Act
    mockMvc.perform(MockMvcRequestBuilders.put("/students/" + studentId +"/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """               
                {
                    "studentCourseId":1,
                    "statusId": 2
                }
                """
            ))
        .andExpect(status().isNotFound());

    // Assert
    Mockito.verify(service, times(1)).updateStudentCourse(any(StudentCourse.class),eq(studentId));

  }

}