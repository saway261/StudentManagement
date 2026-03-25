package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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
    Course course1 = new Course("JA","Javaコース");
    Course course2 = new Course("AW","AWSコース");
    Course course3 = new Course("DE","デザインコース");
    Course course4 = new Course("WM","Webマーケティングコース");
    Course course5 = new Course("FR","フロントエンド開発コース");

    List<Course> expected = List.of(course1,course2,course3,course4,course5);

    List<Course> actual = sut.searchCourseList();

    AssertionsForInterfaceTypes.assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

}