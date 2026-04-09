package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.exception.InvalidStatusTransitionException;
import raisetech.student.management.exception.TargetNotFoundException;
import raisetech.student.management.repository.CourseStatusRepository;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.testutil.TestDataFactory;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository studentRepository;

  @Mock
  private CourseStatusRepository statusRepository;

  @InjectMocks
  private StudentService sut;// System Under Test テスト対象システム

  @Test
  void アクティブ受講生の一覧検索_リポジトリの処理を呼び出していること() {
    // Arrange
    Integer studentId1 = 1;
    Integer scId1 = 1;
    Integer studentId2 = 2;
    Integer scId2 = 2;

    StudentDetail studentDetail1 = TestDataFactory.makeCompletedStudentDetail(studentId1, scId1);
    StudentDetail studentDetail2 = TestDataFactory.makeCompletedStudentDetail(studentId2, scId2);
    List<StudentDetail> expected = List.of(studentDetail1,studentDetail2);

    List<Integer> studentIdList = List.of(studentId1,studentId2);
    List<StudentCourse> studentCourses1 = studentDetail1.getStudentCourses();
    List<StudentCourse> studentCourses2 = studentDetail2.getStudentCourses();

    // スタブ
    Mockito.when(studentRepository.searchStudentIdList()).thenReturn(studentIdList);
    Mockito.when(studentRepository.searchStudent(studentId1)).thenReturn(studentDetail1.getStudent());
    Mockito.when(studentRepository.searchStudent(studentId2)).thenReturn(studentDetail2.getStudent());
    Mockito.when(studentRepository.searchStudentCourses(studentId1)).thenReturn(studentCourses1);
    Mockito.when(studentRepository.searchStudentCourses(studentId2)).thenReturn(studentCourses2);

    // Act
    List<StudentDetail> actual = sut.searchStudentDetailList();

    // Assert
    verify(studentRepository, times(1)).searchStudentIdList();
    verify(studentRepository, times(studentIdList.size())).searchStudent(Mockito.anyInt());
    verify(studentRepository, times(studentIdList.size())).searchStudentCourses(Mockito.anyInt());
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void 受講生単一検索成功_リポジトリの処理を適切に呼び出していること() {
    // Arrange
    Integer studentId = 1;
    Integer scId = 1;

    StudentDetail expected = TestDataFactory.makeCompletedStudentDetail(studentId,scId);
    Student student = expected.getStudent();
    List<StudentCourse> studentCourseList = expected.getStudentCourses();

    // スタブ
    Mockito.when(studentRepository.searchStudent(studentId)).thenReturn(student);
    Mockito.when(studentRepository.searchStudentCourses(studentId)).thenReturn(studentCourseList);

    // Act
    StudentDetail actual = sut.searchStudentDetail(studentId);

    // Assert
    verify(studentRepository, times(1)).searchStudent(studentId);
    verify(studentRepository, times(1)).searchStudentCourses(studentId);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void 受講生単一検索失敗_リポジトリのsearchStudentメソッドの返り値がnullのとき例外を投げていること(){
    // Arrange
    Integer studentId = 999;

    Mockito.when(studentRepository.searchStudent(studentId)).thenReturn(null);
    Mockito.when(studentRepository.searchStudentCourses(studentId)).thenReturn(List.of());

    // Act & Assert
    Assertions.assertThrows(TargetNotFoundException.class, () -> {
      sut.searchStudentDetail(studentId);
    });
    verify(studentRepository, times(1)).searchStudent(studentId);
  }

  @Test
  void 受講生詳細登録成功_受講生を登録し各受講生コースを初期化してリポジトリに渡していること() {
    // Arrange
    Integer studentId = 1;

    Student student = TestDataFactory.makeCompletedStudent(studentId);
    StudentCourse rawCourse1 =
        new StudentCourse(null, null, "JA", null, null, null, null,null);
    StudentCourse rawCourse2 =
        new StudentCourse(null, null, "PY", null, null, null, null,null);

    List<StudentCourse> rawCourseList = List.of(rawCourse1, rawCourse2);
    StudentDetail input = new StudentDetail(student, rawCourseList);

    LocalDate today = LocalDate.now();

    // Act
    StudentDetail result = sut.registerStudentDetail(input);

    // Assert
    verify(studentRepository, times(1)).registerStudent(student);

    ArgumentCaptor<StudentCourse> captor = ArgumentCaptor.forClass(StudentCourse.class);
    verify(studentRepository, times(rawCourseList.size())).registerStudentCourse(captor.capture());

    List<StudentCourse> registeredCourses = captor.getAllValues();

    Assertions.assertEquals(2, registeredCourses.size());

    Assertions.assertEquals(studentId, registeredCourses.get(0).getStudentId());
    Assertions.assertEquals("JA", registeredCourses.get(0).getCourseCode());
    Assertions.assertEquals(1, registeredCourses.get(0).getStatusId());
    Assertions.assertEquals(today, registeredCourses.get(0).getCourseApplyAt());
    Assertions.assertNull(registeredCourses.get(0).getCourseStartAt());
    Assertions.assertNull(registeredCourses.get(0).getCoursePlannedEndAt());

    Assertions.assertEquals(studentId, registeredCourses.get(1).getStudentId());
    Assertions.assertEquals("PY", registeredCourses.get(1).getCourseCode());
    Assertions.assertEquals(1, registeredCourses.get(1).getStatusId());
    Assertions.assertEquals(today, registeredCourses.get(1).getCourseApplyAt());
    Assertions.assertNull(registeredCourses.get(1).getCourseStartAt());
    Assertions.assertNull(registeredCourses.get(1).getCoursePlannedEndAt());

    Assertions.assertEquals(student, result.getStudent());
    Assertions.assertEquals(registeredCourses, result.getStudentCourses());
  }

  @Test
  void 受講生詳細登録失敗_受講生登録時に例外が発生したら例外をそのまま送出すること() {
    // Arrange
    Student student = TestDataFactory.makeCompletedStudent(1);
    StudentCourse rawCourse =
        new StudentCourse(null, null, "JA", null, null, null, null,null);
    StudentDetail input = new StudentDetail(student, List.of(rawCourse));

    doThrow(new RuntimeException("DB error"))
        .when(studentRepository).registerStudent(any(Student.class));

    // Act & Assert
    assertThrows(RuntimeException.class,
        () -> sut.registerStudentDetail(input));

    verify(studentRepository, times(1)).registerStudent(student);
    verify(studentRepository, never()).registerStudentCourse(any(StudentCourse.class));
  }

  @Test
  void 受講生詳細登録失敗_受講生コース登録の途中で例外が発生したら例外を送出すること() {
    // Arrange
    Student student = TestDataFactory.makeCompletedStudent(1);
    StudentCourse course1 =
        new StudentCourse(null, null, "JA", null, null, null, null,null);
    StudentCourse course2 =
        new StudentCourse(null, null, "PY", null, null, null, null,null);

    StudentDetail input = new StudentDetail(student, List.of(course1, course2));

    doNothing()
        .doThrow(new RuntimeException("DB error"))
        .when(studentRepository).registerStudentCourse(any(StudentCourse.class));

    // Act & Assert
    assertThrows(RuntimeException.class,
        () -> sut.registerStudentDetail(input));

    verify(studentRepository, times(1)).registerStudent(student);
    verify(studentRepository, times(2)).registerStudentCourse(any(StudentCourse.class));
  }

  @Test
  void 受講生コース新規登録成功_StudentCourseのフィールドを初期化しリポジトリに渡していること() {
    // Arrange
    Integer studentId = 1;
    StudentCourse rawCourse =
        new StudentCourse(null, null, "JA", null, null, null, null,null);
    LocalDate today = LocalDate.now();
    Mockito.when(studentRepository.existsActiveStudentById(studentId)).thenReturn(true);

    // Act
    StudentCourse result = sut.registerStudentCourse(rawCourse,studentId);

    // Assert
    ArgumentCaptor<StudentCourse> captor = ArgumentCaptor.forClass(StudentCourse.class);
    verify(studentRepository, times(1)).registerStudentCourse(captor.capture());

    StudentCourse registered = captor.getValue();
    Assertions.assertEquals(studentId, registered.getStudentId()); // studentIdをセットされている
    Assertions.assertEquals("JA", registered.getCourseCode());// courseCodeは渡されたまま
    Assertions.assertEquals(1, registered.getStatusId()); // statusIdに1をセットされている
    Assertions.assertEquals(today, registered.getCourseApplyAt()); // courseApplyAtに今日の日付をセットされている
    Assertions.assertNull(registered.getCourseStartAt()); // courseStarAtがnullになっている
    Assertions.assertNull(registered.getCoursePlannedEndAt()); // courseEndAtがnullになっている

    Assertions.assertEquals(registered, result);
  }

  @Test
  void 受講生コース新規登録失敗_受講生IDが存在し登録処理で例外が発生したら例外をそのまま送出すること() {
    // Arrange
    Integer studentId = 1;
    StudentCourse rawCourse =
        new StudentCourse(null, null, "JA", null, null, null, null, null);

    when(studentRepository.existsActiveStudentById(studentId)).thenReturn(true);
    doThrow(new RuntimeException("DB error"))
        .when(studentRepository).registerStudentCourse(any(StudentCourse.class));

    // Act & Assert
    assertThrows(RuntimeException.class,
        () -> sut.registerStudentCourse(rawCourse, studentId));
  }

  @Test
  void 受講生コース新規登録失敗_存在しない受講生IDならTargetNotFoundExceptionを投げ登録処理を行わないこと() {
    // Arrange
    Integer studentId = 999;
    StudentCourse rawCourse =
        new StudentCourse(null, null, "JA", null, null, null, null,null);

    when(studentRepository.existsActiveStudentById(studentId)).thenReturn(false);

    // Act & Assert
    assertThrows(TargetNotFoundException.class,
        () -> sut.registerStudentCourse(rawCourse, studentId));

    verify(studentRepository, never()).registerStudentCourse(any(StudentCourse.class));
  }

  @Test
  void 受講生更新成功_リポジトリの処理を呼び出していること(){
    // Arrange
    Integer studentId = 1;
    Student student = TestDataFactory.makeCompletedStudent(studentId);
    Mockito.when(studentRepository.updateStudent(student)).thenReturn(1);

    // Act
    sut.updateStudent(student);

    // Assert
    verify(studentRepository, times(1)).updateStudent(student);
  }

  @Test
  void 受講生更新失敗_リポジトリのupdateStudentの返り値が0なら例外をそのまま送出すること(){
    // Arrange
    Integer studentId = 99;
    Student student = TestDataFactory.makeCompletedStudent(studentId);
    Mockito.when(studentRepository.updateStudent(student)).thenReturn(0); // 更新件数が0件=更新失敗

    // Act & Assert
    assertThrows(TargetNotFoundException.class, () -> {
      sut.updateStudent(student);
    });
  }

  @ParameterizedTest(name = "[{index}] statusIdを{0}に更新するとき、受講開始日、受講終了日、受講終了実績日に適切な値をセットすること")
  @CsvSource({
      "1,true,true,true",//実際にはcanTransitionで通る組み合わせはない
      "2,true,true,true",
      "3,false,false,true",
      "4,true,true,false",
      "5,true,true,true"
  })
  void 受講生コース更新成功_リポジトリの処理を呼び出していること_ステータス遷移に合わせて適切に日付フィールドがセットされた受講生コースを渡していること(
      int toStatusId, boolean isStartAtNull, boolean isPlannedEndAtNull, boolean isFinishedAtNull ){
    // Arrange
    Integer studentId = 1;
    Integer scId = 1;
    LocalDate now = LocalDate.now();
    StudentCourse studentCourse = new StudentCourse(
        scId,
        studentId,
        null,
        toStatusId,
        null,
        isStartAtNull ? null : now,
        isPlannedEndAtNull ? null : now.plusYears(1),
        isFinishedAtNull ? null : now
    );
    Mockito.when(studentRepository.findStatusId(any(StudentCourse.class))).thenReturn(1);//返り値はnullでなければ良い
    Mockito.when(statusRepository.canTransition(anyInt(),anyInt())).thenReturn(true);
    Mockito.when(studentRepository.updateStudentCourseStatus(any(StudentCourse.class))).thenReturn(1);

    // Act
    sut.updateStudentCourse(studentCourse,studentId);

    // Assert
    verify(studentRepository,times(1)).findStatusId(any(StudentCourse.class));
    verify(statusRepository,times(1)).canTransition(anyInt(),anyInt());
    ArgumentCaptor<StudentCourse> captor = ArgumentCaptor.forClass(StudentCourse.class);
    verify(studentRepository, times(1)).updateStudentCourseStatus(captor.capture());

    StudentCourse updated = captor.getValue();
    Assertions.assertEquals(studentId, updated.getStudentId());
    Assertions.assertEquals(scId, updated.getStudentCourseId());
    Assertions.assertNull(updated.getCourseCode());
    Assertions.assertEquals(toStatusId, updated.getStatusId());
    Assertions.assertNull(updated.getCourseApplyAt());
    Assertions.assertEquals(isStartAtNull ? null : now, updated.getCourseStartAt());
    Assertions.assertEquals(isPlannedEndAtNull ? null : now.plusYears(1), updated.getCoursePlannedEndAt());
    Assertions.assertEquals(isFinishedAtNull ? null : now, updated.getCourseFinishedAt());

  }

  @Test
  void 受講生コース更新失敗_更新前ステータスが取得できないならTargetNotFoundExceptionを送出してのちの処理を呼ばないこと() {
    // Arrange
    Integer studentId = 99;
    Integer scId = 1;
    StudentCourse studentCourse = TestDataFactory.makeCompletedStudentCourse(studentId, scId);
    Mockito.when(studentRepository.findStatusId(any(StudentCourse.class))).thenReturn(null);

    // Act & Assert
    assertThrows(TargetNotFoundException.class, () -> {
      sut.updateStudentCourse(studentCourse, studentId);
    });
    verify(studentRepository, times(1)).findStatusId(any(StudentCourse.class));
    verify(statusRepository, never()).canTransition(anyInt(), anyInt());
    verify(studentRepository, never()).updateStudentCourseStatus(any(StudentCourse.class));
  }

  @Test
  void 受講生コース更新失敗_ステータス遷移が不可能ならInvalidStatusTransitionExceptionを送出しリポジトリの更新処理を呼ばないこと(){
    // Arrange
    Integer studentId = 1;
    Integer scId = 1;
    StudentCourse studentCourse = TestDataFactory.makeCompletedStudentCourse(studentId,scId);
    Mockito.when(studentRepository.findStatusId(any(StudentCourse.class))).thenReturn(1);
    Mockito.when(statusRepository.canTransition(anyInt(),anyInt())).thenReturn(false);

    // Act & Assert
    assertThrows(InvalidStatusTransitionException.class, () -> {
      sut.updateStudentCourse(studentCourse,studentId);
    });
    verify(studentRepository,times(1)).findStatusId(any(StudentCourse.class));
    verify(statusRepository,times(1)).canTransition(anyInt(),anyInt());
    verify(studentRepository, never()).updateStudentCourseStatus(any(StudentCourse.class));
  }

  @Test
  void 受講生コース更新失敗_update件数が0ならTargetNotFoundExceptionを送出すること(){
    // Arrange
    Integer studentId = 99;
    Integer scId = 1;
    StudentCourse studentCourse = TestDataFactory.makeCompletedStudentCourse(studentId,scId);
    Mockito.when(studentRepository.findStatusId(any(StudentCourse.class))).thenReturn(4);
    Mockito.when(statusRepository.canTransition(anyInt(),anyInt())).thenReturn(true);
    Mockito.when(studentRepository.updateStudentCourseStatus(any(StudentCourse.class))).thenReturn(0); // 更新件数が0件=更新失敗

    // Act & Assert
    assertThrows(TargetNotFoundException.class, () -> {
      sut.updateStudentCourse(studentCourse,studentId);
    });
    verify(studentRepository,times(1)).findStatusId(any(StudentCourse.class));
    verify(statusRepository,times(1)).canTransition(anyInt(),anyInt());
    verify(studentRepository, times(1)).updateStudentCourseStatus(any(StudentCourse.class));
  }

}