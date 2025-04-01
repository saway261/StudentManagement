package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.service.converter.converter.StudentConverter;

/**
 * 受講生情報を取り扱うServiceです。受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * アクティブ受講生詳細一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return アクティブ受講生詳細一覧
   */
  public List<StudentDetail> searchActiveStudentDetailList() {
    List<Student> students = repository.searchActiveStudentList();
    return converter.convertToStudentDetailList(students);
  }

  /**
   * 受講生検索です。 IDに基づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して受講生詳細を返します。
   * アクティブ・非アクティブにかかわらず、すべての受講生から検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchstudentDetail(int studentId) {
    Student student = repository.serchStudent(studentId);
    StudentDetail studentDetail = converter.convertToStudentDetail(student);
    return studentDetail;
  }

  /**
   * 受講生詳細の登録を行います。受講生と受講生コースを個別に登録し、受講生コース情報には受講生情報を紐づける値と、コース開始日、コース終了予定日を設定します。
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
      repository.registerCourse(converter.complementCourse(student, course));
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
}

