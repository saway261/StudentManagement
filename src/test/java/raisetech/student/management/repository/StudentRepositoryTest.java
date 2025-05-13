package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static raisetech.student.management.testutil.TestDataFactory.makeCompletedStudent;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Student;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void アクティブな受講生の全件検索が行えること() {
    List<Student> actual = sut.searchActiveStudentList();
    assertThat(actual.size()).isEqualTo(5);
  }


  @Test
  void 受講生登録が行えること() {
    sut.registerStudent(makeCompletedStudent(null));

    List<Student> actual = sut.searchActiveStudentList();
    assertThat(actual.size()).isEqualTo(6);

  }


}