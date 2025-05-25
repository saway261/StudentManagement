package raisetech.student.management.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.value.Id;
import raisetech.student.management.exception.InvalidIdException;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.web.form.StudentCourseForm;
import raisetech.student.management.web.form.StudentDetailForm;
import raisetech.student.management.web.form.StudentForm;
import raisetech.student.management.web.response.StudentDetailResponse;

/**
 * 受講生情報を取り扱うServiceです。受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  /**
   * アクティブ受講生詳細一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return アクティブ受講生詳細一覧（レスポンスオブジェクト）
   */
  public List<StudentDetailResponse> searchActiveStudentDetailList() {
    List<StudentDetailResponse> activeStudentDetailList = new ArrayList<>();
    List<Student> activeStudents = repository.searchActiveStudentList();

    for (Student student : activeStudents) {
      StudentDetail studentDetail = buildStudentDetail(student.getStudentId());
      activeStudentDetailList.add(StudentDetailResponse.fromDomain(studentDetail));
    }
    return activeStudentDetailList;
  }

  /**
   * 受講生検索です。 IDに基づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して受講生詳細を返します。
   * アクティブ・非アクティブにかかわらず、すべての受講生から検索します。該当する受講生IDが登録されていない場合は例外を投げます。
   *
   * @param studentIdNumber 受講生ID
   * @return 受講生詳細（レスポンスオブジェクト）
   */
  public StudentDetailResponse searchStudentDetail(int studentIdNumber) throws InvalidIdException {
    Id studentId = new Id(studentIdNumber);

    StudentDetail studentDetail = buildStudentDetail(studentId);
    if (studentDetail.getStudent() == null) {
      throw new InvalidIdException(studentId);
    }
    return StudentDetailResponse.fromDomain(studentDetail);
  }


  /**
   * 受講生詳細の登録を行います。受講生と受講生コースを個別に登録し、受講生コース情報には受講生情報を紐づける値と、コース開始日、コース終了予定日を設定します。
   * コース開始日にはリクエストが実行された日付、コース終了日にはコース開始日の6か月後の日付が設定されます。
   *
   * @param detailForm 受講生詳細（フォームオブジェクト）
   * @return 登録された受講生詳細（レスポンスオブジェクト）
   */
  @Transactional
  public StudentDetailResponse registerStudentDetail(StudentDetailForm detailForm) {
    Student student = StudentForm.toDomain(detailForm.getStudent());
    repository.registerStudent(student);

    List<StudentCourseForm> courseList = detailForm.getStudentCourseList();

    for (StudentCourseForm course : courseList) {
      repository.registerCourse(StudentCourseForm.toDomain(course, student.getStudentId()));
    }

    return StudentDetailResponse.fromDomain(buildStudentDetail(student.getStudentId()));
  }

  /**
   * 受講生詳細の更新を行います。受講生と受講生コースを個別に登録します。 受講生のキャンセルフラグの更新もここでおこないます。(論理削除)
   *
   * @param detailForm 更新後の受講生詳細（フォームオブジェクト）
   * @return 更新された受講生詳細（レスポンスオブジェクト）
   */
  @Transactional
  public StudentDetailResponse updateStudentDetail(StudentDetailForm detailForm)
      throws InvalidIdException {
    Student requestStudent = StudentForm.toDomain(detailForm.getStudent());
    List<StudentCourseForm> requestCourses = detailForm.getStudentCourseList();

    // リクエストボディの受講生コースリストをループで回す
    for (StudentCourseForm course : requestCourses) {
      StudentCourse complementedCourse = StudentCourseForm.toDomain(course,
          requestStudent.getStudentId());
      if (isLinkedCourseIdWithStudentId(complementedCourse)) {//受講生コースのコースIDが受講生IDと紐づくか判定
        repository.updateCourse(complementedCourse);//紐づくなら更新処理を行う
      } else {
        throw new InvalidIdException(complementedCourse);//紐づかないなら例外を投げる
      }
    }
    //受講生の更新処理を行う
    repository.updateStudent(requestStudent);
    return StudentDetailResponse.fromDomain(buildStudentDetail(requestStudent.getStudentId()));
  }

  /**
   * テーブルから受講生IDに紐づく受講生と受講生コースを取得し、受講生詳細として組み上げます。このメソッドは自クラスからのみ呼び出されます。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細（ドメインオブジェクト）
   */
  StudentDetail buildStudentDetail(Id studentId) {
    Student student = repository.searchStudent(studentId);
    List<StudentCourse> courses = repository.searchCourses(studentId);
    StudentDetail studentDetail = new StudentDetail(student, courses);
    return studentDetail;
  }

  /**
   * 受講生コーステーブルから、引数で渡された受講生IDと一致する値をもつレコードを検索してコースIDリストを取得し、引数で渡されたコースIDがテーブルにおいて受講生IDと紐づいているかを判定します。
   * テーブルから取得したコースIDリストが空の場合は、受講生IDが存在しないと判断し、例外を投げます。このメソッドは自クラスからのみ呼び出されます。
   *
   * @param course 受講生コース（ドメインオブジェクト）
   * @return 受講生コースIDが紐づいている場合はtrue
   */
  boolean isLinkedCourseIdWithStudentId(StudentCourse course) throws InvalidIdException {
    List<Id> existCourseIdListLinkedStudentId = repository.searchCourseIdListLinkedStudentId(
        course.getStudentId());
    if (existCourseIdListLinkedStudentId.isEmpty()) {
      //一件もコースIDが返らない場合、受講生IDが存在しないと判断する。
      throw new InvalidIdException(course.getStudentId());
    }
    return existCourseIdListLinkedStudentId.contains(course.getCourseId());
  }
}

