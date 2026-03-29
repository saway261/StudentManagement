package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

@MybatisTest
class CourseStatusRepositoryTest {

  @Autowired
  private CourseStatusRepository sut;

  @ParameterizedTest(name = "[{index}] fromStatusId={0}, toStatusId={1} のとき {2} が返る")
  @CsvSource({
      // data.sql で定義された存在する組み合わせ
      "1,2,true",
      "1,5,true",
      "2,3,true",
      "2,5,true",
      "3,4,true",
      "3,5,true",

      // 遷移テーブルに存在しない組み合わせ
      "1,3,false",
      "1,4,false",
      "2,4,false",
      "3,2,false",

      // 終端ステータスからの遷移
      "4,1,false",
      "4,5,false",
      "5,1,false",
      "5,3,false",

      // 存在しないステータスIDを含む場合
      "99,1,false",
      "1,99,false",
      "99,99,false"
  })
  void ステータス遷移可否を判定できること(int fromStatusId, int toStatusId, boolean expected) {
    boolean actual = sut.canTransition(fromStatusId, toStatusId);

    assertThat(actual).isEqualTo(expected);
  }
}