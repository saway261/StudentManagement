package raisetech.student.management.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
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
      activeStudentDetailList.add(searchstudentDetail(student.getStudentId()));
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
  public StudentDetail searchstudentDetail(int studentId) {
    Student student = repository.searchStudent(studentId);
    List<StudentCourse> courses = repository.searchCourses(studentId);
    StudentDetail studentDetail = new StudentDetail(student, courses);
    return studentDetail;
  }

  /**
   * 受講生詳細の登録を行います。受講生と受講生コースを個別に登録し、受講生コース情報には受講生情報を紐づける値と、コース開始日、コース終了予定日を設定します。
   * コース開始日にはリクエストが実行された日付、コース終了日にはコース開始日の6か月後の日付が設定されます。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    List<StudentCourse> courseList = studentDetail.getStudentCourseList();

    repository.registerStudent(studentDetail.getStudent());
    for (StudentCourse course : courseList) {
      repository.registerCourse(new StudentCourse(course.getCourseName(), student.getStudentId()));
    }
    return studentDetail;
  }

  /**
   * 受講生詳細の更新を行います。受講生と受講生コースを個別に登録します。 受講生のキャンセルフラグの更新もここでおこないます。(論理削除)
   *
   * @param studentDetail 更新後の受講生詳細
   * @return 実行結果
   */
  @Transactional
  public StudentDetail updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourse studentCourse : studentDetail.getStudentCourseList()) {
      repository.updateCourse(studentCourse);
    }
    return studentDetail;
  }

  /**
   * 引数で渡された受講生IDが受講生テーブルに存在するかを判定します。
   *
   * @param studentId 受講生ID
   * @return 存在する場合はtrue
   */
  public boolean isExistStudentId(int studentId) {
    for (int existId : repository.searchStudentIdList()) {
      if (existId == studentId) {
        return true;
      }
    }
    return false;
  }

  /**
   * 引数で渡された受講生コースIDが、データベースにおいて受講生IDと紐づいているかを判定します。
   *
   * @param courseId 受講生コースID
   * @return 受講生コースIDが紐づいている場合はtrue
   */
  public boolean isLinkedCourseIdWithStudentId(int studentId, int courseId) {
    List<Integer> existCourseIdListLinkedStudentId = repository.searchCourseIdListLinkedStudentId(
        studentId);
    if (existCourseIdListLinkedStudentId.contains(courseId)) {
      return true;
    } else {
      return false;
    }
  }
}

