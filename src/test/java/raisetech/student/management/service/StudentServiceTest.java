package raisetech.student.management.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.value.Id;
import raisetech.student.management.exception.InvalidIdException;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.testutil.TestDataFactory;
import raisetech.student.management.web.form.StudentCourseForm;
import raisetech.student.management.web.form.StudentDetailForm;
import raisetech.student.management.web.form.StudentForm;
import raisetech.student.management.web.response.StudentDetailResponse;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Spy
  @InjectMocks
  private StudentService sut;// System Under Test テスト対象システム

  @Test
  void アクティブ受講生の一覧検索_リポジトリの処理とbuildStudentDetailを呼び出していること() {
    // Arrange
    Id studentId = new Id(1);
    Id courseId = new Id(1);

    StudentDetail studentDetail = TestDataFactory.makeCompletedStudentDetail(studentId, courseId);
    List<Student> studentList = List.of(studentDetail.getStudent());

    when(repository.searchActiveStudentList()).thenReturn(studentList);
    doReturn(studentDetail).when(sut).buildStudentDetail(studentId);

    // Act
    List<StudentDetailResponse> actual = sut.searchActiveStudentDetailList();

    // Assert
    verify(repository, times(1)).searchActiveStudentList();
    verify(sut, times(studentList.size())).buildStudentDetail(studentId);
    Assertions.assertInstanceOf(List.class, actual);
    Assertions.assertEquals(studentList.size(), actual.size());
  }

  @Test
  void 受講生単一検索成功_buildStudentDetailを適切に呼び出していること() throws Exception {
    // Arrange
    Id studentId = new Id(1);
    Id courseId = new Id(1);

    StudentDetail studentDetail = TestDataFactory.makeCompletedStudentDetail(studentId, courseId);
    doReturn(studentDetail).when(sut).buildStudentDetail(studentId);

    // Act
    StudentDetailResponse actual = sut.searchStudentDetail(studentId);

    // Assert
    verify(sut, times(1)).buildStudentDetail(studentId);
  }

  @Test
  void 受講生単一検索失敗_buildStudentDetailを適切に呼び出し戻り値がnullの場合は例外を投げていること()
      throws Exception {
    // Arrange
    Id studentId = new Id(99);

    StudentDetail studentDetail = new StudentDetail(null, null);
    doReturn(studentDetail).when(sut).buildStudentDetail(studentId);

    // Act&Assert
    Assertions.assertThrows(InvalidIdException.class, () -> {
      sut.searchStudentDetail(studentId);
    });
    verify(sut, times(1)).buildStudentDetail(studentId);
  }

  @Test
  void 受講生詳細登録_リポジトリのメソッドを適切に呼び出され_その際StudentCourseインスタンスのstudentIdとcourseStartAtとcourseEndAtフィールドが補完されていること() {
    // Arrange
    Id expectedStudentId = new Id(1);

    LocalDate now = LocalDate.now();

    // 入力データ
    StudentDetailForm form = new StudentDetailForm(
        new StudentForm(
            expectedStudentId.getValue(),
            "山田太郎", "やまだたろう", "タロー",
            "taro@email.com", "東京都練馬区", "090-0000-0000",
            20, "男", "特になし", false
        ),
        List.of(
            new StudentCourseForm(null, "Javaコース", null)
        )
    );

    // ドメイン変換後の Student
    Student expectedStudent = StudentForm.toDomain(form.getStudent());

    // buildStudentDetail() から返すモック値（レスポンス用）
    StudentDetail dummyDetail = new StudentDetail(
        expectedStudent,
        List.of() // 本検証では使用しない
    );
    when(repository.searchStudent(expectedStudentId)).thenReturn(expectedStudent);
    when(repository.searchCourses(expectedStudentId)).thenReturn(List.of());

    // Act
    StudentDetailResponse actual = sut.registerStudentDetail(form);

    // Assert
    // 1. registerStudent が1回呼ばれたこと
    verify(repository, times(1)).registerStudent(expectedStudent);

    // 2. registerCourse が1回呼ばれたこと（コース数と一致）
    ArgumentCaptor<StudentCourse> captor = ArgumentCaptor.forClass(StudentCourse.class);
    verify(repository, times(form.getStudentCourseList().size())).registerCourse(captor.capture());

    // 3. 各引数の中身をチェック（補完されているか）
    List<StudentCourse> registeredCourses = captor.getAllValues();
    for (StudentCourse course : registeredCourses) {
      // courseIdの同値検証は行わない（自動採番された値をMyBatis側がインスタンスにセットする動きを再現できないため。また、本検証の目的から反れるため）
      assertThat(course.getCourseName()).isEqualTo("Javaコース");
      assertThat(course.getStudentId()).isEqualTo(expectedStudentId);
      assertThat(course.getCourseStartAt()).isEqualTo(now);
      assertThat(course.getCourseEndAt()).isEqualTo(now.plusMonths(6));
    }
  }


  @Test
  void 受講生詳細更新成功_リポジトリのメソッドとisLinkedCourseIdWithStudentIdを呼び出していること()
      throws Exception {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 1;

    StudentDetailForm form = TestDataFactory.makeDummyStudentDetailFormOnUpdate(studentId,
        courseId);

    Student domainStudent = StudentForm.toDomain(form.getStudent());
    List<StudentCourse> domainCourseList =
        form.getStudentCourseList().stream()
            .map(formCourse -> StudentCourseForm.toDomain(formCourse, new Id(studentId)))
            .toList();
    StudentDetail domainDetail = new StudentDetail(domainStudent, domainCourseList);

    doReturn(true).when(sut).isLinkedCourseIdWithStudentId(any(StudentCourse.class));
    doReturn(domainDetail).when(sut).buildStudentDetail(new Id(studentId));

    // Act
    StudentDetailResponse actual = sut.updateStudentDetail(form);

    // Assert
    verify(sut, times(domainCourseList.size()))
        .isLinkedCourseIdWithStudentId(any(StudentCourse.class));
    verify(repository, times(domainCourseList.size()))
        .updateCourse(any(StudentCourse.class));
    verify(repository, times(1)).updateStudent(domainStudent);
  }

  @Test
  void 受講生詳細更新失敗_isLinkedCourseIdWithStudentIdを呼び出していること() throws Exception {
    // Arrange
    Integer studentId = 1;
    Integer courseId = 99;

    StudentDetailForm form = TestDataFactory.makeDummyStudentDetailFormOnUpdate(studentId,
        courseId);
    doReturn(false).when(sut).isLinkedCourseIdWithStudentId(any(StudentCourse.class));

    // 実行と検証
    Assertions.assertThrows(InvalidIdException.class, () -> {
      sut.updateStudentDetail(form);
    });
  }

  @Test
  void 受講生組み上げ_リポジトリのメソッドを適切に呼び出してStudentDetailを生成できていること() {
    //前提
    Id studentId = new Id(1);
    Id courseId = new Id(1);

    //事前準備
    Student student = TestDataFactory.makeCompletedStudent(studentId);
    List<StudentCourse> courseList = List.of(
        TestDataFactory.makeCompletedStudentCourse(studentId, courseId));

    when(repository.searchStudent(studentId)).thenReturn(student);
    when(repository.searchCourses(studentId)).thenReturn(courseList);

    // 実際の実行結果
    StudentDetail actual = sut.buildStudentDetail(studentId);

    // 検証
    verify(repository, times(1)).searchStudent(studentId);
    verify(repository, times(1)).searchCourses(studentId);
    Assertions.assertEquals(student, actual.getStudent());
    Assertions.assertEquals(courseList, actual.getStudentCourseList());
    Assertions.assertInstanceOf(StudentDetail.class, actual);
  }

  @Test
  void コースIDが受講生IDに紐づいていると判断する_リポジトリのメソッドを適切に呼び出して返却されたリストにコースIDが含まれていると判断しtrueを返していこと()
      throws InvalidIdException {
    //前提
    Id studentId = new Id(1);
    Id courseId = new Id(1);

    //事前準備
    StudentCourse course = TestDataFactory.makeCompletedStudentCourse(studentId, courseId);
    List<Id> containCourseIdList = List.of(new Id(1));
    when(repository.searchCourseIdListLinkedStudentId(studentId)).thenReturn(
        containCourseIdList);

    //検証
    Assertions.assertTrue(sut.isLinkedCourseIdWithStudentId(course));
    verify(repository, times(1)).searchCourseIdListLinkedStudentId(studentId);

  }

  @Test
  void コースIDが受講生IDに紐づいていないと判断する_リポジトリのメソッドを適切に呼び出して返却されたリストにコースIDが含まれていないと判断しfalseを返していること()
      throws InvalidIdException {
    //前提
    Id studentId = new Id(1);
    Id courseId = new Id(99);

    //事前準備
    StudentCourse course = TestDataFactory.makeCompletedStudentCourse(studentId, courseId);
    List<Id> notContainCourseIdList = List.of(new Id(30));
    when(repository.searchCourseIdListLinkedStudentId(studentId)).thenReturn(
        notContainCourseIdList);

    //検証
    Assertions.assertFalse(sut.isLinkedCourseIdWithStudentId(course));
    verify(repository, times(1)).searchCourseIdListLinkedStudentId(studentId);

  }

  @Test
  void 受講生IDがテーブルに存在しないと判断する_リポジトリのメソッドを適切に呼び出して返却されたリストが空と判断し例外を投げていること() {
    // 前提
    Id studentId = new Id(99);
    Id courseId = new Id(1);

    // 事前準備
    StudentCourse course = TestDataFactory.makeCompletedStudentCourse(studentId, courseId);
    List<Id> emptyIntList = new ArrayList<>();
    when(repository.searchCourseIdListLinkedStudentId(studentId)).thenReturn(emptyIntList);

    // 検証
    Assertions.assertThrows(InvalidIdException.class, () -> {
      sut.isLinkedCourseIdWithStudentId(course);
    });
    verify(repository, times(1)).searchCourseIdListLinkedStudentId(studentId);
  }

}