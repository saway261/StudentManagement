package raisetech.student.management.web.form;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.value.Id;
import raisetech.student.management.testutil.TestDataFactory;

class StudentFormTest {

  @Test
  void 登録時_StudentFormがStudentに変換されstudentIdがnullでもエラーにならないこと()
      throws NoSuchFieldException, IllegalAccessException {
    // Arrange
    StudentForm form = TestDataFactory.makeCompletedStudentForm(null);

    // Act
    Student domain = StudentForm.toDomain(form);

    // Assert
    assertThat(isDomainEqualToForm(domain, form)).isTrue();
  }

  @Test
  void 更新時_StudentFormがStudentに変換されstudentIdがInteger型からIdに変換されること()
      throws NoSuchFieldException, IllegalAccessException {
    // Arrange
    Integer studentId = 1;
    StudentForm form = TestDataFactory.makeCompletedStudentForm(studentId);

    // Act
    Student domain = StudentForm.toDomain(form);

    // Assert
    assertThat(isDomainEqualToForm(domain, form)).isTrue();
  }

  private boolean isDomainEqualToForm(Student domain, StudentForm form)
      throws NoSuchFieldException, IllegalAccessException {

    // domainのstudentIdの中身をアクセサを通さずに確認する準備
    Field studentIdField = Student.class.getDeclaredField("studentId");
    studentIdField.setAccessible(true);

    return
        Objects.equals(studentIdField.get(domain),
            form.getStudentId() == null ? null : new Id(form.getStudentId())) &&
            Objects.equals(domain.getFullname(), form.getFullname()) &&
            Objects.equals(domain.getKanaName(), form.getKanaName()) &&
            Objects.equals(domain.getNickname(), form.getNickname()) &&
            Objects.equals(domain.getEmail(), form.getEmail()) &&
            Objects.equals(domain.getTelephone(), form.getTelephone()) &&
            Objects.equals(domain.getAge(), form.getAge()) &&
            Objects.equals(domain.getSex(), form.getSex()) &&
            Objects.equals(domain.getRemark(), form.getRemark()) &&
            domain.isDeleted() == form.isDeleted();

  }


}