package raisetech.student.management.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.exception.TargetNotFoundException;
import raisetech.student.management.repository.StudentRepository;


/**
 * 受講生情報を取り扱うサービスです。
 * 受講取得の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  /**
   * アクティブな受講生詳細の一覧検索を行います。
   * @return 受講生詳細の一覧
   */
  public List<StudentDetail> serchStudentDetailList(){
    List<Student> students = repository.searchActiveStudentList();

    return students.stream()
        .map(student -> buildStudentDetail(student.getStudentId())).collect(Collectors.toList());
  }

  /**
   * アクティブかどうかを問わず受講生IDに紐づく受講生詳細を検索します。
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudentDetail(int studentId){
    StudentDetail response = buildStudentDetail(studentId);
    if(response.getStudent() == null){
      throw new TargetNotFoundException("studentId","指定したIDの受講生は見つかりませんでした");
    }
    return response;
  }

  /**
   * 受講生詳細の登録を行います。
   * 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日とコース終了日をセットします。
   * @param studentDetail 受講生詳細
   * @return 登録後の受講生詳細
   */
  @Transactional
  public StudentDetail registerStudentDetail(StudentDetail studentDetail){
    Student student = studentDetail.getStudent();
    repository.registerStudent(student);

    for(StudentCourse studentCourse : studentDetail.getStudentCourses()){
      initStudentCourse(studentCourse, student);
      repository.registerStudentCourse(studentCourse);
    }
    return studentDetail;
  }

  /**
   * 受講生詳細の更新を行います。受講生と受講生コース情報を個別に更新します。
   * @param studentDetail 受講生詳細
   * @return 更新後の受講生詳細
   */
  @Transactional
  public StudentDetail updateStudentDetail(StudentDetail studentDetail){
    int updatedStudent = repository.updateStudent(studentDetail.getStudent());
    if(updatedStudent == 0){
      throw new TargetNotFoundException("Student.studentId","更新対象のインスタンスが見つかりませんでした");
    }
    for (StudentCourse studentCourse : studentDetail.getStudentCourses()){
      int updatedStudentCourse = repository.updateStudentCourse(studentCourse);
      if(updatedStudentCourse == 0){
        throw new TargetNotFoundException("StudentCourse.courseId","更新対象のインスタンスが見つかりませんでした");
      }
    }
    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   * @param studentCourse 受講生コース情報
   * @param student 受講生
   */
  private void initStudentCourse(StudentCourse studentCourse, Student student) {
    LocalDate now = LocalDate.now();

    studentCourse.setStudentId(student.getStudentId());
    studentCourse.setCourseStartAt(now);
    studentCourse.setCourseEndAt(now.plusYears(1));
  }

  /**
   * 受講生IDに紐づく受講生とコース情報を検索し、受講生詳細情報として組み上げます。
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  private StudentDetail buildStudentDetail(int studentId){
    Student student = repository.searchStudent(studentId);
    List<StudentCourse> studentCourses = repository.searchStudentCourses(studentId);

    return new StudentDetail(student, studentCourses);
  }

}
