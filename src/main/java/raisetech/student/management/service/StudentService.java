package raisetech.student.management.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.search.request.StudentAdvancedSearchRequest;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.exception.InvalidStatusTransitionException;
import raisetech.student.management.exception.TargetNotFoundException;
import raisetech.student.management.repository.CourseStatusRepository;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.search.criteria.StudentSearchCriteria;
import raisetech.student.management.search.converter.StudentSearchCriteriaConverter;


/**
 * 受講生情報を取り扱うサービスです。
 * 受講取得の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository studentRepository;
  private CourseStatusRepository statusRepository;

  private StudentSearchCriteriaConverter converter;

  @Autowired
  public StudentService(StudentRepository studentRepository,
      CourseStatusRepository statusRepository,
      StudentSearchCriteriaConverter converter) {

    this.studentRepository = studentRepository;
    this.statusRepository = statusRepository;
    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索を行います。
   * @return 受講生詳細の一覧
   */
  public List<StudentDetail> searchStudentDetailList(){
    List<Integer> studentIdList = studentRepository.searchStudentIdList();

    return studentIdList.stream()
        .map(studentId -> buildStudentDetail(studentId))
        .collect(Collectors.toList());
  }

  /**
   * 受講生IDに紐づく受講生詳細を検索します。
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

  public List<StudentDetail> searchStudentDetailsAdvanced(StudentAdvancedSearchRequest request) {
    StudentSearchCriteria criteria = converter.toCriteria(request.getFilters());

    List<Integer> studentIdList = studentRepository.searchStudentIdListByCriteria(criteria);

    return studentIdList.stream()
        .map(this::buildStudentDetail)
        .collect(Collectors.toList());
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
   * 受講生コースの更新を行います。受講生コースIDと受講生IDで指定した受講生コースのステータスのみを更新できます。
   * @param studentCourse 受講生コース
   * @param studentId 受講生ID
   * @return 引数で受け取った受講生コースに受講生IDと必要な日付情報をセットしなおしたもの
   */
  @Transactional
  public StudentCourse updateStudentCourse(StudentCourse studentCourse, int studentId){
    // 更新後のステータスID
    int toStatusId = studentCourse.getStatusId();

    StudentCourse reflected = reflectStatusTransition(studentCourse,studentId);

    // 更新前（現在）のステータスID
    Integer fromStatusId = studentRepository.findStatusId(reflected);
    if (fromStatusId == null) {
      throw new TargetNotFoundException("studentCourse", "受講生IDと受講生コースIDで指定できる受講生コースが存在しません");
    }

    // 遷移不可能なら例外をスロー
    if(!statusRepository.canTransition(fromStatusId,toStatusId)){
      throw new InvalidStatusTransitionException(fromStatusId,toStatusId);
    }

    int updatedRows = studentRepository.updateStudentCourseStatus(reflected);
    if (updatedRows == 0) {
      throw new TargetNotFoundException("studentCourse", "受講生IDと受講生コースIDで指定できる受講生コースが存在しません");
    }
    return reflected;
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
        null,
        null
    );
  }

  /**
   * 受講生コースのステータス更新に付随して必要な日付情報をセットする。
   * 「受講中」への遷移なら受講開始日に今日を、受講終了予定日に今日から一年後をセットする。
   * 「受講終了」への遷移なら受講終了実績日に今日をセットする。
   * 更新時にリポジトリ層で使用しないコースコードと受講申込日は明示的にnullをセットする。
   * @param studentCourse 受講生コース
   * @param studentId 受講生ID
   * @return ステータス遷移に応じて日付情報をセットされた受講生コース
   */
  private StudentCourse reflectStatusTransition(StudentCourse studentCourse, Integer studentId){
    LocalDate now = LocalDate.now();
    Integer toStatusId = studentCourse.getStatusId();

    // statusId==3は受講中、statusId==4は受講終了
    return new StudentCourse(
      studentCourse.getStudentCourseId(),
        studentId,
        null,
        toStatusId,
        null,
        toStatusId == 3 ? now : null,
        toStatusId == 3 ? now.plusYears(1) : null,
        toStatusId == 4 ? now : null
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
