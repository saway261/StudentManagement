package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static raisetech.student.management.testutil.TestDataFactory.newDummyStudent;
import static raisetech.student.management.testutil.TestDataFactory.newDummyStudentCourseOnRegister;
import static raisetech.student.management.testutil.TestDataFactory.newDummyStudentDetail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.domain.validation.OnUpdate;
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

  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  ArgumentCaptor<StudentDetail> captor = ArgumentCaptor.forClass(StudentDetail.class);

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

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + studentId))
        .andExpect(status().isOk());
    Mockito.verify(service, times(1)).searchStudentDetail(studentId);
  }

  @Test
  void 受講生詳細単一検索失敗_サービスから例外を受け取り404エラーを返していること()
      throws Exception {
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
  void 古いエンドポイントに対するアクティブ受講生詳細一覧検索リクエスト_404エラーが返されること()
      throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/studentAndCourses"))
        .andExpect(status().isNotFound());
  }

  @Test
  void 受講生詳細登録成功_サービスの処理を呼び出す際に適切なリクエストボディが渡されていること()
      throws Exception {
    // Arrange
    int studentId = 0;//リクエスト時のIDは未入力のため0
    int courseId = 0;//リクエスト時のIDは未入力のため0
    StudentDetail requestStudentDetail = new StudentDetail(
        newDummyStudent(studentId),
        List.of(newDummyStudentCourseOnRegister(studentId, courseId))
    );
    StudentDetail responseStudentDetail = newDummyStudentDetail(1, 1);
    Mockito.when(service.registerStudent(requestStudentDetail)).thenReturn(responseStudentDetail);

    // Act & Assertion
    mockMvc.perform(MockMvcRequestBuilders.post("/students")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(requestStudentDetail))
    ).andExpect(status().isOk());
    Mockito.verify(service, times(1)).registerStudent(captor.capture());
    assertThat(captor.getValue())
        .usingRecursiveComparison()
        .isEqualTo(requestStudentDetail);
  }

  @Test
  void 受講生詳細更新成功_サービスの処理を呼び出す際に適切なリクエストボディが渡されていること()
      throws Exception {
    // Arrange
    int studentId = 1;
    int courseId = 1;
    StudentDetail requestStudentDetail = newDummyStudentDetail(studentId, courseId);
    Mockito.when(service.registerStudent(requestStudentDetail)).thenReturn(requestStudentDetail);

    // Act & Assertion
    mockMvc.perform(MockMvcRequestBuilders.put("/students")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(requestStudentDetail))
    ).andExpect(status().isOk());
    Mockito.verify(service, times(1)).updateStudent(captor.capture());
    assertThat(captor.getValue())
        .usingRecursiveComparison()
        .isEqualTo(requestStudentDetail);
  }

  @Test
  void 受講生詳細リクエストボディ検証_受講生の必須項目がnullのとき入力チェックにかかること()
      throws Exception {
    // Arrange: fullnameがnull
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        new Student(
            studentId,
            null, // fullnameがnull
            "やまだたろう",
            "タロー",
            "taro@email.com",
            "東京都練馬区",
            "090-0000-0000",
            20,
            "男",
            "特になし",
            false
        ),
        List.of(newDummyStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student.fullname"))).isTrue();
  }

  @Test
  void 受講生詳細リクエストボディ検証_文字数が超過するとき入力チェックにかかること()
      throws Exception {
    // Arrange: かなが51文字
    StringBuilder tooLongKanaName = new StringBuilder();
    tooLongKanaName.append("テスト");
    tooLongKanaName.setLength(51);

    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        new Student(
            studentId,
            "山田太郎",
            tooLongKanaName.toString(),
            "タロー",
            "taro@email.com",
            "東京都練馬区",
            "090-0000-0000",
            20,
            "男",
            "特になし",
            false
        ),
        List.of(newDummyStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student.kanaName"))).isTrue();
  }

  @Test
  void 受講生詳細リクエストボディ検証_emailの形式が不正のとき入力チェックにかかること()
      throws Exception {
    // Arrange: emailの形式が不正
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        new Student(
            studentId,
            "山田太郎",
            "やまだたろう",
            "タロー",
            "taroemailcom",// @がなく形式が不正
            "東京都練馬区",
            "090-0000-0000",
            20,
            "男",
            "特になし",
            false
        ),
        List.of(newDummyStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student.email"))).isTrue();
  }

  @Test
  void 受講生詳細リクエストボディ検証_電話番号の形式が不正のとき入力チェックにかかること()
      throws Exception {
    // Arrange: 電話番号の形式が不正
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        new Student(
            studentId,
            "山田太郎",
            "やまだたろう",
            "タロー",
            "taro@email.com",
            "東京都練馬区",
            "09000000000",// -がなく形式が不正
            20,
            "男",
            "特になし",
            false
        ),
        List.of(newDummyStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student.telephone"))).isTrue();
  }

  @Test
  void 受講生詳細リクエストボディ検証_年齢が15未満のとき入力チェックにかかること()
      throws Exception {
    // Arrange: ageが15未満
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        new Student(
            studentId,
            "山田太郎",
            "やまだたろう",
            "タロー",
            "taro@email.com",
            "東京都練馬区",
            "090-0000-0000",
            14,
            "男",
            "特になし",
            false
        ),
        List.of(newDummyStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student.age"))).isTrue();
  }

  @Test
  void 受講生詳細リクエストボディ検証_性別がパターンにマッチしないとき入力チェックにかかること()
      throws Exception {
    // Arrange: 性別が”男性”
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        new Student(
            studentId,
            "山田太郎",
            "やまだたろう",
            "タロー",
            "taro@email.com",
            "東京都練馬区",
            "090-0000-0000",
            20,
            "男性",
            "特になし",
            false
        ),
        List.of(newDummyStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student.sex"))).isTrue();
  }

  @Test
  void 受講生詳細リクエストボディ検証_受講生コースの必須情報がnullのとき入力チェックにかかること()
      throws Exception {
    // Arrange: コース名がnull
    int studentId = 0;
    int courseId = 0;
    LocalDate now = LocalDate.now();
    StudentDetail invalidStudentDetail = new StudentDetail(
        newDummyStudent(studentId),
        List.of(new StudentCourse(
            courseId,
            null,// コース名がnull
            studentId,
            null,
            null)
        ));
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString()
            .matches("studentCourseList\\[\\d+\\]\\.courseName"))).isTrue();
  }

  @Test
  void 受講生詳細リクエストボディ検証_受講生コースのコース名が不正のとき入力チェックにかかること()
      throws Exception {
    // Arrange: コース名が不正
    int studentId = 0;
    int courseId = 0;
    LocalDate now = LocalDate.now();
    StudentDetail invalidStudentDetail = new StudentDetail(
        newDummyStudent(studentId),
        List.of(new StudentCourse(
            courseId,
            "Pythonコース",// 想定されないコース名
            studentId,
            null,
            null)
        ));
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString()
            .matches("studentCourseList\\[\\d+\\]\\.courseName"))).isTrue();
  }
  //TODO:どのくらいの粒度で網羅したらいいのか？

  @Test
  void 受講生詳細リクエストボディ検証_受講生がnullのとき入力チェックにかかること() {
    // Arrange
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        null, List.of(newDummyStudentCourseOnRegister(studentId, courseId))
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("student"))).isTrue();
  }

  @Test
  void 受講生詳細リクエストボディ検証_受講生コースが空のとき入力チェックにかかること() {
    // Arrange
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = new StudentDetail(
        newDummyStudent(studentId), new ArrayList<StudentCourse>()
    );
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail);

    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().equals("studentCourseList"))).isTrue();
  }

  @Test
  void 受講生詳細リクエストボディ検証_更新時_受講生IDと受講生コースIDが1未満のとき入力チェックにかかること() {
    // Arrange
    int studentId = 0;
    int courseId = 0;
    StudentDetail invalidStudentDetail = newDummyStudentDetail(studentId, courseId);
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail,
        OnUpdate.class);

    // Act & Assert
    assertThat(violations.size()).isEqualTo(2);
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().contains("Id"))).isTrue();
  }

  @Test
  void 受講生詳細リクエストボディ検証_更新時_受講終了予定日がnullのとき入力チェックにかかること() {
    // Arrange
    int studentId = 1;
    int courseId = 1;
    StudentDetail invalidStudentDetail = new StudentDetail(
        newDummyStudent(studentId),
        List.of(new StudentCourse(
            courseId,
            "Javaコース",
            studentId,
            LocalDate.now(),
            null // 受講終了予定日
        )));
    Set<ConstraintViolation<StudentDetail>> violations = validator.validate(invalidStudentDetail,
        OnUpdate.class);
    // Act & Assert
    assertThat(violations).isNotEmpty();
    assertThat(violations.stream()
        .allMatch(v -> v.getPropertyPath().toString().
            matches("studentCourseList\\[\\d+\\]\\.courseEndAt"))).isTrue();
  }

}