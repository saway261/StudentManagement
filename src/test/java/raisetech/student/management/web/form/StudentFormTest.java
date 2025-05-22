package raisetech.student.management.web.form;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.value.Id;
import raisetech.student.management.testutil.TestDataFactory;

class StudentFormTest {

  @Test
  void 登録時_StudentFormがStudentに変換されstudentIdがnullでもエラーにならないこと() {
    // Arrange
    StudentForm form = TestDataFactory.makeCompletedStudentForm(null);

    // Act
    Student domain = StudentForm.toDomain(form);

    // Assert
    assertThat(domain.getStudentId()).isNull();
    assertThat(domain.getFullname()).isEqualTo(form.getFullname());
    assertThat(domain.getKanaName()).isEqualTo(form.getKanaName());
    assertThat(domain.getNickname()).isEqualTo(form.getNickname());
    assertThat(domain.getEmail()).isEqualTo(form.getEmail());
    assertThat(domain.getArea()).isEqualTo(form.getArea());
    assertThat(domain.getTelephone()).isEqualTo(form.getTelephone());
    assertThat(domain.getSex()).isEqualTo(form.getSex());
    assertThat(domain.getRemark()).isEqualTo(form.getRemark());
    assertThat(domain.isDeleted()).isEqualTo(form.isDeleted());

  }

  @Test
  void 更新時_StudentFormがStudentに変換されstudentIdがInteger型からIdに変換されること() {
    // Arrange
    Integer studentId = 1;
    StudentForm form = TestDataFactory.makeCompletedStudentForm(studentId);

    // Act
    Student domain = StudentForm.toDomain(form);

    // Assert
    assertThat(domain.getStudentId()).isEqualTo(new Id(studentId));
    assertThat(domain.getFullname()).isEqualTo(form.getFullname());
    assertThat(domain.getKanaName()).isEqualTo(form.getKanaName());
    assertThat(domain.getNickname()).isEqualTo(form.getNickname());
    assertThat(domain.getEmail()).isEqualTo(form.getEmail());
    assertThat(domain.getArea()).isEqualTo(form.getArea());
    assertThat(domain.getTelephone()).isEqualTo(form.getTelephone());
    assertThat(domain.getSex()).isEqualTo(form.getSex());
    assertThat(domain.getRemark()).isEqualTo(form.getRemark());
    assertThat(domain.isDeleted()).isEqualTo(form.isDeleted());

  }


}