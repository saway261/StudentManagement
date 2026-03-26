package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.master.Course;

@MybatisTest
class CourseRepositoryTest {

  @Autowired
  private CourseRepository sut;

  @ParameterizedTest(name="[{index}] courseCodeが{0}の場合{1}が返る")
  @CsvSource({
      "JA,true",
      "AW,true",
      "DE,true",
      "WM,true",
      "FR,true",
      "WP,false",
      "Javaコース,false"
  })
  void 存在するコースコードを指定した場合にtrueが返り存在しないコースコードを指定した場合にfalseが返ること(String courseCode,boolean expectResult) {
    // data.sql で定義されている 'JA' を使用
    boolean result = sut.existsByCourseCode(courseCode);

    if(expectResult){
      assertThat(result).isTrue();
    }else{
      assertThat(result).isFalse();
    }
  }

  @Test
  void コースコードにnullを指定した場合にfalseが返ること() {
    boolean result = sut.existsByCourseCode(null);

    assertThat(result).isFalse();
  }

  @Test
  void コースの全件検索が行えること(){
    List<Course> expected = MyBatisTestDataFactory.makeDummyCourseList();

    List<Course> actual = sut.searchCourseList();

    AssertionsForInterfaceTypes.assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 提供コースの登録が行えること(){
    // Arrange
    List<Course> existing = MyBatisTestDataFactory.makeDummyCourseList();
    int existingSize = existing.size();
    Course newCourse = new Course("AI","AI開発コース");

    List<Course> expected = new ArrayList<>(existing);
    expected.add(newCourse);

    // Act
    sut.registerCourse(newCourse);
    List<Course> actual = sut.searchCourseList();

    // Assert
    AssertionsForClassTypes.assertThat(actual.size()).isEqualTo(existingSize + 1);
    AssertionsForInterfaceTypes.assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 必須項目がnullのコースを登録しようとすると例外が発生すること(){
    // Arrange
    Course invalidNewCourse = new Course("AI",null);

    // Act & Assert
    assertThatThrownBy(() -> sut.registerCourse(invalidNewCourse))
        .isInstanceOf(Exception.class);
  }

  @Test
  void 重複したコース名を登録しようとすると例外が発生すること(){
    // Arrange
    Course invalidNewCourse = new Course(
        "AI",// 識別子は新規
        "Javaコース"//コース名は既存
    );

    // Act & Assert
    assertThatThrownBy(() -> sut.registerCourse(invalidNewCourse))
        .isInstanceOf(Exception.class);
  }

  @Test
  void 提供コースのコース名の更新が行えること(){
    // Arrange
    String targetCourseCode = "JA";
    Course expected = new Course(
        targetCourseCode,"名称変更コース"// もとはJavaコース
    );

    // Act
    int updated = sut.updateCourse(expected);
    Course actual = sut.searchCourseList().stream()
        .filter(c -> c.getCourseCode().equals(targetCourseCode))
        .findFirst()
        .orElseThrow();

    // Assert
    assertThat(updated).isEqualTo(1);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void 存在しないコースコードを指定して更新しようとしたとき更新件数が0件であること(){
    Course course = new Course(
        "NOT","存在しないコース"
    );

    int actual = sut.updateCourse(course);

    assertThat(actual).isZero();
  }


}