package raisetech.student.management.service;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.exception.InvalidIdException;
import raisetech.student.management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  private StudentService sut;// System Under Test テスト対象システム

  @BeforeEach
  void before() {
    int studentId;
    sut = new StudentService(repository);
  }

  //  @Test
//  void アクティブ受講生の一覧検索_リポジトリの処理とbuildStudentDetailを適切に呼び出し受講生詳細を組み上げていること() {
//    // 事前準備
//    List<Student> studentList = new ArrayList();
//    Mockito.when(repository.searchActiveStudentList()).thenReturn(studentList);
//    Mockito.when(repository.searchStudent(1)).thenReturn(student);
//    Mockito.when(repository.searchCourses(1)).thenReturn(List.of());
//
//    // 実際の実行結果
//    List<StudentDetail> actual = sut.searchActiveStudentDetailList();
//
//    // 検証
//    Mockito.verify(repository, times(1)).searchActiveStudentList();
//    Mockito.verify(repository, times(1)).searchStudent(1);
//    Mockito.verify(repository, times(1)).searchCourses(1);
//  }
//
//  @Test
//  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
//    StudentService sut = new StudentService(repository);
//    List<Student> studentList = new ArrayList<>();
//    List<StudentCourse> courseList = new ArrayList<>();
//    Mockito.when(repository.searchStudent()).thenReturn(studentList);
//    Mockito.when(repository.searchCourses()).thenReturn(courseList);
//
//    sut.searchStudentList();
//
//    verify(repository, times(1)).searchStudent();
//    verify(repository, times(1)).searchCourses();
//    verify(converter, times(1)).convertStudentDetails(studentList, courseList);
//
//
//  }
//
//  @Test
//  void searchStudentDetail_validId_shouldReturnStudentDetail() throws Exception {
//    // Arrange
//    int studentId = 1;
//    Student student = new Student();
//    student.setStudentId(studentId);
//    Mockito.when(repository.searchStudent(studentId)).thenReturn(student);
//    Mockito.when(repository.searchCourses(studentId)).thenReturn(List.of());
//
//    // Act
//    StudentDetail result = sut.searchStudentDetail(studentId);
//
//    // Assert
//    Mockito.verify(repository).searchStudent(studentId);
//    Mockito.verify(repository).searchCourses(studentId);
//  }
//
//  @Test
//  void searchStudentDetail_invalidId_shouldThrowException() {
//    // Arrange
//    int studentId = 99;
//    Mockito.when(repository.searchStudent(studentId)).thenReturn(null);
//
//    // Act & Assert
//    org.junit.jupiter.api.Assertions.assertThrows(
//        raisetech.student.management.exception.InvalidIdException.class,
//        () -> sut.searchStudentDetail(studentId)
//    );
//  }
//
//  @Test
//  void registerStudent_shouldRegisterStudentAndCourses() {
//    // Arrange
//    Student student = new Student();
//    student.setStudentId(1);
//    StudentCourse course = new StudentCourse("Java", 1);
//    StudentDetail studentDetail = new StudentDetail(student, List.of(course));
//
//    Mockito.when(repository.searchStudent(1)).thenReturn(student);
//    Mockito.when(repository.searchCourses(1)).thenReturn(List.of(course));
//
//    // Act
//    StudentDetail result = sut.registerStudent(studentDetail);
//
//    // Assert
//    Mockito.verify(repository).registerStudent(student);
//    Mockito.verify(repository).registerCourse(Mockito.any(StudentCourse.class));
//    Mockito.verify(repository).searchStudent(1);
//    Mockito.verify(repository).searchCourses(1);
//  }
//
//  @Test
//  void updateStudent_shouldUpdateStudentAndCourses() throws Exception {
//    // Arrange
//    Student student = new Student();
//    student.setStudentId(1);
//    StudentCourse course = new StudentCourse();
//    course.setCourseId(10);
//    course.setStudentId(1);
//    StudentDetail studentDetail = new StudentDetail(student, List.of(course));
//
//    Mockito.when(repository.searchCourseIdListLinkedStudentId(1)).thenReturn(List.of(10));
//    Mockito.when(repository.searchStudent(1)).thenReturn(student);
//    Mockito.when(repository.searchCourses(1)).thenReturn(List.of(course));
//
//    // Act
//    StudentDetail result = sut.updateStudent(studentDetail);
//
//    // Assert
//    Mockito.verify(repository).updateCourse(course);
//    Mockito.verify(repository).updateStudent(student);
//    Mockito.verify(repository).searchStudent(1);
//    Mockito.verify(repository).searchCourses(1);
//  }
//

  @Test
  void 受講生組み上げ_リポジトリのメソッドを適切に呼び出してStudentDetailを生成できているか() {
    //前提
    int studentId = 1;

    //事前準備
    Student student = new Student(studentId, "山田太郎", "やまだたろう", "タロー", "taro@email.com",
        "東京都練馬区", "090-0000-0000", 20, "男", "特になし", false);
    List<StudentCourse> courseList = List.of(new StudentCourse("Javaコース", studentId));

    Mockito.when(repository.searchStudent(studentId)).thenReturn(student);
    Mockito.when(repository.searchCourses(studentId)).thenReturn(courseList);

    // 実際の実行結果
    StudentDetail actual = sut.buildStudentDetail(studentId);

    // 検証
    Mockito.verify(repository, times(1)).searchStudent(studentId);
    Mockito.verify(repository, times(1)).searchCourses(studentId);
    Assertions.assertEquals(student, actual.getStudent());
    Assertions.assertEquals(courseList, actual.getStudentCourseList());
    //TODO:StudentDetail studentDetail = new StudentDetail(student, courses);の検証として妥当？
  }

  @Test
  void コースIDが受講生IDに紐づいていると判断する_リポジトリのメソッドを適切に呼び出して返却されたリストにコースIDが含まれていると判断しtrueを返しているか()
      throws InvalidIdException {
    //前提
    int studentId = 1;
    int courseId = 1;

    //事前準備
    StudentCourse course = newDummyStudentCourse(courseId, studentId);
    List<Integer> notContainCourseIdList = List.of(1);
    when(repository.searchCourseIdListLinkedStudentId(studentId)).thenReturn(
        notContainCourseIdList);

    //検証
    Assertions.assertTrue(sut.isLinkedCourseIdWithStudentId(course));
    Mockito.verify(repository, times(1)).searchCourseIdListLinkedStudentId(studentId);

  }

  @Test
  void コースIDが受講生IDに紐づいていないと判断する_リポジトリのメソッドを適切に呼び出して返却されたリストにコースIDが含まれていないと判断しfalseを返しているか()
      throws InvalidIdException {
    //前提
    int studentId = 30;
    int courseId = 1;

    //事前準備
    StudentCourse course = newDummyStudentCourse(courseId, studentId);
    List<Integer> notContainCourseIdList = List.of(30);
    when(repository.searchCourseIdListLinkedStudentId(studentId)).thenReturn(
        notContainCourseIdList);

    //検証
    Assertions.assertFalse(sut.isLinkedCourseIdWithStudentId(course));
    Mockito.verify(repository, times(1)).searchCourseIdListLinkedStudentId(studentId);

  }

  @Test
  void 受講生IDがテーブルに存在しないと判断する_リポジトリのメソッドを適切に呼び出して返却されたリストが空と判断し例外を投げているか() {
    // 前提
    int studentId = 99;

    // 事前準備
    StudentCourse course = newDummyStudentCourse(studentId);
    List<Integer> emptyIntList = new ArrayList<>();
    when(repository.searchCourseIdListLinkedStudentId(studentId)).thenReturn(emptyIntList);

    // 検証
    Assertions.assertThrows(InvalidIdException.class, () -> {
      sut.isLinkedCourseIdWithStudentId(course);
    });
    Mockito.verify(repository, times(1)).searchCourseIdListLinkedStudentId(studentId);
  }

  private Student newDummyStudent(int studentId) {
    return new Student(studentId, "山田太郎", "やまだたろう", "タロー", "taro@email.com",
        "東京都練馬区", "090-0000-0000", 20, "男", "特になし", false);
  }

  private StudentCourse newDummyStudentCourse(int courseId, int studentId) {
    LocalDate now = LocalDate.now();
    return new StudentCourse(courseId, "Javaコース", studentId, now, now.plusMonths(6));
  }

  private StudentCourse newDummyStudentCourse(int studentId) {
    return new StudentCourse("Javaコース", studentId);
  }


}