package raisetech.student.management.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.exception.InvalidIdException;
import raisetech.student.management.repository.StudentRepository;

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
   * @return アクティブ受講生詳細一覧
   */
  public List<StudentDetail> searchActiveStudentDetailList() {
    List<StudentDetail> activeStudentDetailList = new ArrayList<>();
    List<Student> activeStudents = repository.searchActiveStudentList();

    for (Student student : activeStudents) {
      StudentDetail studentDetail = buildStudentDetail(student.getStudentId());
      activeStudentDetailList.add(studentDetail);
    }
    return activeStudentDetailList;
  }

  /**
   * 受講生検索です。 IDに基づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して受講生詳細を返します。
   * アクティブ・非アクティブにかかわらず、すべての受講生から検索します。該当する受講生IDが登録されていない場合は例外を投げます。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudentDetail(int studentId) throws InvalidIdException {
    StudentDetail studentDetail = buildStudentDetail(studentId);
    if (studentDetail.getStudent() == null) {
      throw new InvalidIdException(studentId);
    }
    return studentDetail;
  }


  /**
   * 受講生詳細の登録を行います。受講生と受講生コースを個別に登録し、受講生コース情報には受講生情報を紐づける値と、コース開始日、コース終了予定日を設定します。
   * コース開始日にはリクエストが実行された日付、コース終了日にはコース開始日の6か月後の日付が設定されます。
   *
   * @param studentDetail 受講生詳細
   * @return 登録された受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    List<StudentCourse> courseList = studentDetail.getStudentCourseList();

    repository.registerStudent(studentDetail.getStudent());
    for (StudentCourse course : courseList) {
      repository.registerCourse(new StudentCourse(course.getCourseName(), student.getStudentId()));
    }

    return buildStudentDetail(student.getStudentId());
  }

  /**
   * 受講生詳細の更新を行います。受講生と受講生コースを個別に登録します。 受講生のキャンセルフラグの更新もここでおこないます。(論理削除)
   *
   * @param studentDetail 更新後の受講生詳細
   * @return 更新された受講生詳細
   */
  @Transactional
  public StudentDetail updateStudent(StudentDetail studentDetail) throws InvalidIdException {
    Student requestStudent = studentDetail.getStudent();
    List<StudentCourse> requestCourses = studentDetail.getStudentCourseList();

    // リクエストボディの受講生コースリストをループで回す
    for (StudentCourse course : requestCourses) {
      course.setStudentId(requestStudent.getStudentId());
      if (isLinkedCourseIdWithStudentId(course)) {//受講生コースのコースIDが受講生IDと紐づくか判定
        repository.updateCourse(course);//紐づくなら更新処理を行う
      } else {
        throw new InvalidIdException(course);//紐づかないなら例外を投げる
      }
    }
    //受講生の更新処理を行う
    repository.updateStudent(requestStudent);
    StudentDetail responseStudentDetail = buildStudentDetail(requestStudent.getStudentId());
    return responseStudentDetail;
  }

  /**
   * テーブルから受講生IDに紐づく受講生と受講生コースを取得し、受講生詳細として組み上げます。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  private StudentDetail buildStudentDetail(int studentId) {
    Student student = repository.searchStudent(studentId);
    List<StudentCourse> courses = repository.searchCourses(studentId);
    StudentDetail studentDetail = new StudentDetail(student, courses);
    return studentDetail;
  }

  /**
   * 受講生コーステーブルから、引数で渡された受講生IDと一致する値をもつレコードを検索してコースIDリストを取得し、引数で渡されたコースIDがテーブルにおいて受講生IDと紐づいているかを判定します。
   * テーブルから取得したコースIDリストが空の場合は、受講生IDが存在しないと判断し、例外を投げます。
   *
   * @param course 受講生コース
   * @return 受講生コースIDが紐づいている場合はtrue
   */
  private boolean isLinkedCourseIdWithStudentId(StudentCourse course) throws InvalidIdException {
    List<Integer> existCourseIdListLinkedStudentId = repository.searchCourseIdListLinkedStudentId(
        course.getStudentId());
    if (existCourseIdListLinkedStudentId.isEmpty()) {
      //一件もコースIDが返らない場合、受講生IDが存在しないと判断する。
      throw new InvalidIdException(course.getStudentId());
    }
    return existCourseIdListLinkedStudentId.contains(course.getCourseId());
  }
}

