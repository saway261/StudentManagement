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
 * 受講生情報を取り扱うサービスです。受講生の検索や登録・更新処理を行います。
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
   * 受講生一覧（キャンセル除く）検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生一覧（キャンセル除く全件）
   */
  public List<StudentDetail> searchActiveStudentDetailList() {
    List<Student> students = repository.searchActiveStudentList();
    return converter.convertToStudentDetailList(students);
  }

  /**
   * 受講生検索です。 IDに基づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して受講生詳細を返します。
   *
   * @param studentId 受講生ID
   * @return　受講生詳細
   */
  public StudentDetail searchstudentDetail(int studentId) {
    Student student = repository.serchStudent(studentId);
    StudentDetail studentDetail = converter.convertToStudentDetail(student);
    return studentDetail;
  }


  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    List<StudentCourse> courseList = studentDetail.getStudentsCourses();

    repository.registerStudent(studentDetail.getStudent());
    for (StudentCourse course : courseList) {
      repository.registerCourse(converter.complementCourse(student, course));
    }
    return studentDetail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourse studentCourse : studentDetail.getStudentsCourses()) {
      repository.updateCourse(studentCourse);
    }
  }
}

