package raisetech.student.management.service;

import java.time.LocalDate;
import java.util.ArrayList;
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

  private StudentRepository studentRepository;

  @Autowired
  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  /**
   * アクティブな受講生詳細の一覧検索を行います。
   * @return 受講生詳細の一覧
   */
  public List<StudentDetail> searchStudentDetailList(){
    List<Integer> studentIdList = studentRepository.searchActiveStudentIdList();

    return studentIdList.stream()
        .map(studentId -> buildStudentDetail(studentId))
        .collect(Collectors.toList());
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
   * 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値と申込状況と受講申込日をセットします。
   * @param studentDetail 受講生詳細
   * @return 登録後の受講生詳細
   */
  @Transactional
  public StudentDetail registerStudentDetail(StudentDetail studentDetail){
    Student student = studentDetail.getStudent();
    studentRepository.registerStudent(student);

    List<StudentCourse> initedStudentCourseList = new ArrayList<>();
    for(StudentCourse studentCourse : studentDetail.getStudentCourses()){
      StudentCourse initedStudentCourse = initStudentCourse(studentCourse, student.getStudentId());
      studentRepository.registerStudentCourse(initedStudentCourse);
      initedStudentCourseList.add(initedStudentCourse);
    }
    return new StudentDetail(student,initedStudentCourseList);
  }

  /**
   * 受講生の更新を行います
   * @param student 受講生
   * @return 引数で受け取った受講生
   */
  @Transactional
  public Student updateStudent(Student student){
    int updatedStudent = studentRepository.updateStudent(student);
    if(updatedStudent == 0){
      throw new TargetNotFoundException("studentId","更新対象の受講生が見つかりませんでした");
    }
    return student;
  }

  /**
   * 受講生コースの登録(追加)を行います。
   * @param studentCourse 未初期化の受講生コース
   * @param studentId コースを紐づける受講生のID
   * @return 初期化された受講生コース
   */
  @Transactional
  public StudentCourse registerStudentCourse(StudentCourse studentCourse, int studentId) {
    if(!studentRepository.existsActiveStudentById(studentId)){
      throw new TargetNotFoundException("studentId","対象の受講生が見つかりませんでした");
    }
    StudentCourse initedStudentCourse = initStudentCourse(studentCourse,studentId);
    studentRepository.registerStudentCourse(initedStudentCourse);
    return initedStudentCourse;
  }

  /**
   * 受講生コースの更新を行います。受講生コースIDと受講生IDで指定した受講生コースのコースコードのみを更新できます。
   * @param studentCourse 受講生コース
   * @param studentId 受講生ID
   * @return 引数で受け取った受講生コースに受講生IDだけセットしなおしたもの
   */
  @Transactional
  public StudentCourse updateStudentCourse(StudentCourse studentCourse, int studentId){
    StudentCourse adjustedStudentCourse = new StudentCourse(
        studentCourse.getStudentCourseId(),
        studentId,
        studentCourse.getCourseCode(),
        studentCourse.getStatusId(),
        studentCourse.getCourseApplyAt(),
        studentCourse.getCourseStartAt(),
        studentCourse.getCourseEndAt()
    );

    int updatedRows = studentRepository.updateStudentCourse(adjustedStudentCourse);

    if (updatedRows == 0) {
      throw new TargetNotFoundException(
          "studentCourseId",
          "指定した受講生IDと受講生コースIDが紐づきません"
      );
    }
    return adjustedStudentCourse;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   * @param studentCourse 受講生コース情報
   * @param studentId 受講生ID
   * @return StudentCourse 初期情報を設定された受講生コース情報
   */
  private StudentCourse initStudentCourse(StudentCourse studentCourse, Integer studentId) {
    LocalDate now = LocalDate.now();

    return new StudentCourse(
        null,
        studentId,
        studentCourse.getCourseCode(),
        1,//登録時は仮登録
        now,// 登録時は受講申込日のみセット
        null,
        null
    );
  }

  /**
   * 受講生IDに紐づく受講生と受講生コース情報を検索し、受講生詳細情報として組み上げます。
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  private StudentDetail buildStudentDetail(int studentId){
    Student student = studentRepository.searchStudent(studentId);
    List<StudentCourse> studentCourses = studentRepository.searchStudentCourses(studentId);

    return new StudentDetail(student, studentCourses);
  }

}
