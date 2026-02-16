package raisetech.student.management.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    Mockito.when(repository.searchActiveStudentIdList()).thenReturn(studentIdList);
    Mockito.when(repository.searchStudentCourses(studentId1)).thenReturn(studentCourses1);
    Mockito.when(repository.searchStudentCourses(studentId2)).thenReturn(studentCourses2);

    // Act
    List<StudentDetail> actual = sut.serchStudentDetailList();

    // Assert
    verify(repository, times(1)).searchActiveStudentIdList();
    verify(repository, times(studentIdList.size())).searchStudent(Mockito.anyInt());
    verify(repository, times(studentIdList.size())).searchStudentCourses(Mockito.anyInt());
    Assertions.assertInstanceOf(List.class, actual);
    Assertions.assertEquals(expected.size(), actual.size());
  }

  /**
   * searchStudentDetail(int studentId)の正常系テスト
   */
  @Test
  void 受講生単一検索成功_リポジトリの処理を適切に呼び出していること() {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;

    Student student = TestDataFactory.makeCompletedStudent(studentId);
    List<StudentCourse> studentCourseList = List.of(TestDataFactory.makeCompletedStudentCourse(studentId, courseId));
    Mockito.when(repository.searchStudent(studentId)).thenReturn(student);
    Mockito.when(repository.searchStudentCourses(studentId)).thenReturn(studentCourseList);

    // Act
    StudentDetail actual = sut.searchStudentDetail(studentId);

    // Assert
    verify(repository, times(1)).searchStudent(studentId);
    verify(repository, times(1)).searchStudentCourses(studentId);
    Assertions.assertInstanceOf(StudentDetail.class, actual);
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

}