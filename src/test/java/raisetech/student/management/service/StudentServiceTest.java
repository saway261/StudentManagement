package raisetech.student.management.service;


import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static raisetech.student.management.testutil.TestDataFactory.completedStudent;
import static raisetech.student.management.testutil.TestDataFactory.completedStudentCourse;
import static raisetech.student.management.testutil.TestDataFactory.completedStudentDetail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
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

  @Spy
  @InjectMocks
  private StudentService sut;// System Under Test テスト対象システム

  @Test
  void アクティブ受講生の一覧検索_リポジトリの処理とbuildStudentDetailを適切に呼び出していること() {
    //前提
    int studentId = 1;
    int courseId = 1;
    // 事前準備
    StudentDetail studentDetail = completedStudentDetail(studentId, courseId);
    List<Student> studentList = List.of(studentDetail.getStudent());
    Mockito.when(repository.searchActiveStudentList()).thenReturn(studentList);
    doReturn(studentDetail).when(sut).buildStudentDetail(studentId);

    // 実際の実行結果
    List<StudentDetail> actual = sut.searchActiveStudentDetailList();

    // 検証
    Mockito.verify(repository, times(1)).searchActiveStudentList();
    Mockito.verify(sut, times(studentList.size())).buildStudentDetail(studentId);
    Assertions.assertInstanceOf(List.class, actual);
    Assertions.assertEquals(studentList.size(), actual.size());
  }

  @Test
  void 受講生単一検索成功_buildStudentDetailを適切に呼び出していること() throws Exception {
    // 前提
    int studentId = 1;
    int courseId = 1;
    //事前準備
    StudentDetail studentDetail = completedStudentDetail(studentId, courseId);
    doReturn(studentDetail).when(sut).buildStudentDetail(studentId);

    // 実際の実行結果
    StudentDetail actual = sut.searchStudentDetail(studentId);

    // 検証
    Mockito.verify(sut, times(1)).buildStudentDetail(studentId);
  }

  @Test
  void 受講生単一検索失敗_buildStudentDetailを適切に呼び出し戻り値がnullの場合は例外を投げていること()
      throws Exception {
    // 前提
    int studentId = 99;
    // 事前準備
    StudentDetail studentDetail = new StudentDetail(null, null);
    doReturn(studentDetail).when(sut).buildStudentDetail(studentId);

    //検証
    Assertions.assertThrows(InvalidIdException.class, () -> {
      sut.searchStudentDetail(studentId);
    });
    Mockito.verify(sut, times(1)).buildStudentDetail(studentId);
  }

  @Test
  void 受講生詳細登録_リポジトリのメソッドを適切に呼び出していること() {
    // 事前準備
    int studentId = 1;
    int courseId = 0;
    Student student = completedStudent(studentId);
    //コンストラクタでの初期化が行われているか判断するため、あえてstudentIdは0,courseStartAt, courseEndAtはnullとする。
    StudentCourse course = new StudentCourse(courseId, "Javaコース", 0, null, null);
    List<StudentCourse> courseList = List.of(course);
    StudentDetail studentDetail = new StudentDetail(student, courseList);
    LocalDate now = LocalDate.now();

    // 実際の実行結果
    sut.registerStudent(studentDetail);

    // 検証
    Mockito.verify(repository, times(1)).registerStudent(student);
    Mockito.verify(repository, times(courseList.size()))
        .registerCourse(Mockito.argThat(sc ->
            sc != null &&
                course.getCourseName().equals(sc.getCourseName()) &&
                sc.getStudentId() == studentId &&
                now.equals(sc.getCourseStartAt()) &&
                now.plusMonths(6).equals(sc.getCourseEndAt())
        ));
  }

  @Test
  void 受講生詳細更新成功_リポジトリのメソッドとisLinkedCourseIdWithStudentIdを呼び出していること()
      throws Exception {
    // 前提
    int studentId = 1;
    int courseId = 1;
    // 事前準備
    StudentDetail studentDetail = completedStudentDetail(studentId, courseId);
    Student student = studentDetail.getStudent();
    List<StudentCourse> courseList = studentDetail.getStudentCourseList();
    doReturn(true).when(sut).isLinkedCourseIdWithStudentId(Mockito.any(StudentCourse.class));

    // 実際の実行
    sut.updateStudent(studentDetail);

    // 検証
    Mockito.verify(sut, times(courseList.size()))
        .isLinkedCourseIdWithStudentId(Mockito.any(StudentCourse.class));
    Mockito.verify(repository, times(courseList.size()))
        .updateCourse(Mockito.any(StudentCourse.class));
    Mockito.verify(repository, times(1)).updateStudent(student);
  }

  @Test
  void 受講生詳細更新失敗_isLinkedCourseIdWithStudentIdを呼び出していること() throws Exception {
    // 前提
    int studentId = 1;
    int courseId = 99;
    // 事前準備
    StudentDetail studentDetail = completedStudentDetail(studentId, courseId);
    doReturn(false).when(sut).isLinkedCourseIdWithStudentId(Mockito.any(StudentCourse.class));

    // 実行と検証
    Assertions.assertThrows(InvalidIdException.class, () -> {
      sut.updateStudent(studentDetail);
    });
  }

  @Test
  void 受講生組み上げ_リポジトリのメソッドを適切に呼び出してStudentDetailを生成できていること() {
    //前提
    int studentId = 1;
    int courseId = 1;

    //事前準備
    Student student = completedStudent(studentId);
    List<StudentCourse> courseList = List.of(completedStudentCourse(studentId, courseId));

    Mockito.when(repository.searchStudent(studentId)).thenReturn(student);
    Mockito.when(repository.searchCourses(studentId)).thenReturn(courseList);

    // 実際の実行結果
    StudentDetail actual = sut.buildStudentDetail(studentId);

    // 検証
    Mockito.verify(repository, times(1)).searchStudent(studentId);
    Mockito.verify(repository, times(1)).searchCourses(studentId);
    Assertions.assertEquals(student, actual.getStudent());
    Assertions.assertEquals(courseList, actual.getStudentCourseList());
    Assertions.assertInstanceOf(StudentDetail.class, actual);
  }

  @Test
  void コースIDが受講生IDに紐づいていると判断する_リポジトリのメソッドを適切に呼び出して返却されたリストにコースIDが含まれていると判断しtrueを返していこと()
      throws InvalidIdException {
    //前提
    int studentId = 1;
    int courseId = 1;

    //事前準備
    StudentCourse course = completedStudentCourse(studentId, courseId);
    List<Integer> notContainCourseIdList = List.of(1);
    when(repository.searchCourseIdListLinkedStudentId(studentId)).thenReturn(
        notContainCourseIdList);

    //検証
    Assertions.assertTrue(sut.isLinkedCourseIdWithStudentId(course));
    Mockito.verify(repository, times(1)).searchCourseIdListLinkedStudentId(studentId);

  }

  @Test
  void コースIDが受講生IDに紐づいていないと判断する_リポジトリのメソッドを適切に呼び出して返却されたリストにコースIDが含まれていないと判断しfalseを返していること()
      throws InvalidIdException {
    //前提
    int studentId = 1;
    int courseId = 99;

    //事前準備
    StudentCourse course = completedStudentCourse(studentId, courseId);
    List<Integer> notContainCourseIdList = List.of(30);
    when(repository.searchCourseIdListLinkedStudentId(studentId)).thenReturn(
        notContainCourseIdList);

    //検証
    Assertions.assertFalse(sut.isLinkedCourseIdWithStudentId(course));
    Mockito.verify(repository, times(1)).searchCourseIdListLinkedStudentId(studentId);

  }

  @Test
  void 受講生IDがテーブルに存在しないと判断する_リポジトリのメソッドを適切に呼び出して返却されたリストが空と判断し例外を投げていること() {
    // 前提
    int studentId = 99;
    int courseId = 1;

    // 事前準備
    StudentCourse course = completedStudentCourse(studentId, courseId);
    List<Integer> emptyIntList = new ArrayList<>();
    when(repository.searchCourseIdListLinkedStudentId(studentId)).thenReturn(emptyIntList);

    // 検証
    Assertions.assertThrows(InvalidIdException.class, () -> {
      sut.isLinkedCourseIdWithStudentId(course);
    });
    Mockito.verify(repository, times(1)).searchCourseIdListLinkedStudentId(studentId);
  }

}