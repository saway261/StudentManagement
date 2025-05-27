package raisetech.student.management.web.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.StudentDetail;
import raisetech.student.management.data.value.Id;
import raisetech.student.management.testutil.TestDataFactory;

class StudentDetailResponseTest {

  @Test
  void すべてのフィールドが埋まっているとき_正常にStudentDetailがStudentDetailResponseに変換されること() {

    Id studentId = new Id(1);
    Id courseId = new Id(1);

    // domain
    StudentDetail detailDomain = TestDataFactory.makeCompletedStudentDetail(studentId, courseId);
    Student studentDomain = detailDomain.getStudent();
    StudentCourse courseDomain = detailDomain.getStudentCourseList().get(0);

    // response
    StudentDetailResponse detailResponse = StudentDetailResponse.fromDomain(detailDomain);// Act
    StudentResponse studentResponse = detailResponse.getStudent();
    StudentCourseResponse courseResponse = detailResponse.getStudentCourseList().get(0);

    // Assert
    assertThat(isResponseEqualToDomain(studentResponse, studentDomain)).isTrue();
    assertThat(isResponseEqualToDomain(courseResponse, courseDomain)).isTrue();

  }

  @ParameterizedTest
  @ValueSource(strings = {"nickname", "area", "telephone", "age", "sex", "remark"})
  void DBで必須項目でないフィールドがnullのとき_正常にStudentDetailがStudentDetailResponseに変換されること(
      String fieldName) {
    // Arrange
    Id studentId = new Id(2);
    Id courseId = new Id(2);

    Student studentDomain = new Student(
        studentId,
        "山田太郎",
        "やまだたろう",
        fieldName.equals("nickname") ? null : "タロー",
        "taro@email.com",
        fieldName.equals("area") ? null : "東京都練馬区",
        fieldName.equals("telephone") ? null : "090-0000-0000",
        fieldName.equals("age") ? null : 20,
        fieldName.equals("sex") ? null : "男",
        fieldName.equals("remark") ? null : "特になし",
        false
    );
    StudentCourse courseDomain = TestDataFactory.makeCompletedStudentCourse(studentId, courseId);

    StudentDetail detailDomain = new StudentDetail(studentDomain, List.of(courseDomain));

    StudentDetailResponse detailResponse = StudentDetailResponse.fromDomain(detailDomain); //Act
    StudentResponse studentResponse = detailResponse.getStudent();
    StudentCourseResponse courseResponse = detailResponse.getStudentCourseList().get(0);

    // Assert
    assertThat(isResponseEqualToDomain(studentResponse, studentDomain)).isTrue();
    assertThat(isResponseEqualToDomain(courseResponse, courseDomain)).isTrue();

  }

  @ParameterizedTest
  @MethodSource("provideInvalidIdCombinations")
  void idフィールドがnullのとき_StudentDetailがStudentDetailResponseに変換されずNullPointerExceptionが投げられること(
      Id studentId, Id courseId) {

    StudentDetail domain = TestDataFactory.makeCompletedStudentDetail(studentId, courseId);

    assertThrows(NullPointerException.class, () ->
        StudentDetailResponse.fromDomain(domain)
    );
  }

  private static Stream<Arguments> provideInvalidIdCombinations() {
    return Stream.of(
        org.junit.jupiter.params.provider.Arguments.of(null, new Id(3)),   // studentIdがnull
        org.junit.jupiter.params.provider.Arguments.of(new Id(3), null),   // courseIdがnull
        org.junit.jupiter.params.provider.Arguments.of(null, null)         // 両方null
    );
  }

  private boolean isResponseEqualToDomain(StudentResponse response, Student domain) {
    return
        Objects.equals(response.getStudentId(), domain.getStudentId().getValue()) &&
            Objects.equals(response.getFullname(), domain.getFullname()) &&
            Objects.equals(response.getKanaName(), domain.getKanaName()) &&
            Objects.equals(response.getNickname(), domain.getNickname()) &&
            Objects.equals(response.getEmail(), domain.getEmail()) &&
            Objects.equals(response.getTelephone(), domain.getTelephone()) &&
            Objects.equals(response.getAge(), domain.getAge()) &&
            Objects.equals(response.getSex(), domain.getSex()) &&
            Objects.equals(response.getRemark(), domain.getRemark()) &&
            response.isDeleted() == domain.isDeleted();
  }

  private boolean isResponseEqualToDomain(StudentCourseResponse response, StudentCourse domain) {
    return
        Objects.equals(response.getCourseId(), domain.getCourseId().getValue()) &&
            Objects.equals(response.getCourseName(), domain.getCourseName()) &&
            Objects.equals(response.getCourseStartAt(), domain.getCourseStartAt()) &&
            Objects.equals(response.getCourseEndAt(), domain.getCourseEndAt());
  }

}