package raisetech.student.management.service;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.exception.TargetNotFoundException;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.testutil.TestDataFactory;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @InjectMocks
  private StudentService sut;// System Under Test テスト対象システム

  /**
   * serchStudentDetailList()のテスト
   */
  @Test
  void アクティブ受講生の一覧検索_リポジトリの処理を呼び出していること() {
    // Arrange
    Integer studentId1 = 1;
    Integer courseId1 = 1;
    Integer studentId2 = 2;
    Integer courseId2 = 2;

    StudentDetail studentDetail1 = TestDataFactory.makeCompletedStudentDetail(studentId1, courseId1);
    StudentDetail studentDetail2 = TestDataFactory.makeCompletedStudentDetail(studentId2, courseId2);
    List<StudentDetail> expected = List.of(studentDetail1,studentDetail2);

    List<Integer> studentIdList = List.of(studentId1,studentId2);
    List<StudentCourse> studentCourses1 = studentDetail1.getStudentCourses();
    List<StudentCourse> studentCourses2 = studentDetail2.getStudentCourses();

    // スタブ
    Mockito.when(repository.searchActiveStudentIdList()).thenReturn(studentIdList);
    Mockito.when(repository.searchStudent(studentId1)).thenReturn(studentDetail1.getStudent());
    Mockito.when(repository.searchStudent(studentId2)).thenReturn(studentDetail2.getStudent());
    Mockito.when(repository.searchStudentCourses(studentId1)).thenReturn(studentCourses1);
    Mockito.when(repository.searchStudentCourses(studentId2)).thenReturn(studentCourses2);

    // Act
    List<StudentDetail> actual = sut.serchStudentDetailList();

    // Assert
    verify(repository, times(1)).searchActiveStudentIdList();
    verify(repository, times(studentIdList.size())).searchStudent(Mockito.anyInt());
    verify(repository, times(studentIdList.size())).searchStudentCourses(Mockito.anyInt());
    Assertions.assertEquals(expected, actual);
  }

  /**
   * searchStudentDetail(int studentId)の正常系テスト
   */
  @Test
  void 受講生単一検索成功_リポジトリの処理を適切に呼び出していること() {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;

    StudentDetail expected = TestDataFactory.makeCompletedStudentDetail(studentId,courseId);
    Student student = expected.getStudent();
    List<StudentCourse> studentCourseList = expected.getStudentCourses();

    // スタブ
    Mockito.when(repository.searchStudent(studentId)).thenReturn(student);
    Mockito.when(repository.searchStudentCourses(studentId)).thenReturn(studentCourseList);

    // Act
    StudentDetail actual = sut.searchStudentDetail(studentId);

    // Assert
    verify(repository, times(1)).searchStudent(studentId);
    verify(repository, times(1)).searchStudentCourses(studentId);
    Assertions.assertEquals(expected, actual);
  }

  /**
   * searchStudentDetail(int studentId)の異常系テスト
   */
  @Test
  void 受講生単一検索失敗_リポジトリのsearchStudentメソッドの返り値がnullのとき例外を投げていること(){
    // Arrange
    Integer studentId = 999;

    Mockito.when(repository.searchStudent(studentId)).thenReturn(null);
    Mockito.when(repository.searchStudentCourses(studentId)).thenReturn(List.of());

    // Act & Assert
    Assertions.assertThrows(TargetNotFoundException.class, () -> {
      sut.searchStudentDetail(studentId);
    });
    verify(repository, times(1)).searchStudent(studentId);
  }

  /**
   * registerStudentDetail(StudentDetail studentDetail)のテスト
   */
  @Test
  void 受講生新規登録_StudentCourseのフィールドを初期化しリポジトリの処理を呼び出していること(){
    // Arrange
    int studentId = 1;

    Student student = TestDataFactory.makeCompletedStudent(studentId);
    StudentCourse rawCourse = new StudentCourse(null,null,"Javaコース",null,null);
    List<StudentCourse> rawCourseList = List.of(rawCourse);

    StudentDetail input = new StudentDetail(student, rawCourseList);

    LocalDate today = LocalDate.now();

    // Act
    sut.registerStudentDetail(input);

    // Assert
    ArgumentCaptor<StudentCourse> captor = ArgumentCaptor.forClass(StudentCourse.class);
    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(rawCourseList.size())).registerStudentCourse(captor.capture());

    StudentCourse registered = captor.getValue();
    Assertions.assertEquals(studentId, registered.getStudentId()); // studentIdをセットされている
    Assertions.assertEquals(today, registered.getCourseStartAt()); // courseStartAtに今日の日付をセットされている
    Assertions.assertEquals(today.plusYears(1), registered.getCourseEndAt()); // courseEndAtに今日の日付をセットされている
  }

  /**
   * updateStudentDetail(StudentDetail studentDetail)の正常系テスト
   */
  @Test
  void 受講生更新成功_リポジトリの処理を呼び出していること(){
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;

    StudentDetail input = TestDataFactory.makeCompletedStudentDetail(studentId,courseId);
    Student student = input.getStudent();
    List<StudentCourse> studentCourse = input.getStudentCourses();

    Mockito.when(repository.updateStudent(student)).thenReturn(1);
    Mockito.when(repository.updateStudentCourse(Mockito.any(StudentCourse.class))).thenReturn(1);

    // Act
    sut.updateStudentDetail(input);

    // Assert
    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(studentCourse.size())).updateStudentCourse(Mockito.any(StudentCourse.class));
  }

  /**
   * updateStudentDetail(StudentDetail studentDetail)の異常系テスト
   */
  @Test
  void 受講生更新失敗_リポジトリのupdateStudentの返り値が0なら例外を投げてupdateStudentCourseを呼ばないこと(){
    Integer studentId = 99;
    Integer courseId = 99;

    StudentDetail input = TestDataFactory.makeCompletedStudentDetail(studentId,courseId);
    Student student = input.getStudent();

    Mockito.when(repository.updateStudent(student)).thenReturn(0); // 更新件数が0件=更新失敗

    // Act & Assert
    Assertions.assertThrows(TargetNotFoundException.class, () -> {
      sut.updateStudentDetail(input);
    });
    verify(repository, never()).updateStudentCourse(Mockito.any(StudentCourse.class));

  }

  /**
   * updateStudentDetail(StudentDetail studentDetail)の異常系テスト
   */
  @Test
  void 受講生更新失敗_リポジトリのupdateStudentCourseの返り値が0なら例外を投げること(){
    Integer studentId = 99;
    Integer courseId = 99;

    StudentDetail input = TestDataFactory.makeCompletedStudentDetail(studentId,courseId);
    Student student = input.getStudent();

    Mockito.when(repository.updateStudent(student)).thenReturn(1); // 更新件数が1件=更新成功
    Mockito.when(repository.updateStudentCourse(Mockito.any(StudentCourse.class))).thenReturn(0);

    // Act & Assert
    Assertions.assertThrows(TargetNotFoundException.class, () -> {
      sut.updateStudentDetail(input);
    });
    verify(repository, times(1)).updateStudent(student);
    verify(repository, atLeastOnce()).updateStudentCourse(Mockito.any(StudentCourse.class));


  }

}