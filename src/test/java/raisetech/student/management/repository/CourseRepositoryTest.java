package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

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
}