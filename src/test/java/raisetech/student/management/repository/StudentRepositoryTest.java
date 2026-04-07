package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.search.criteria.StudentSearchCriteria;
import raisetech.student.management.search.request.SearchFilter;
import raisetech.student.management.search.request.SearchOperator;
import raisetech.student.management.testutil.TestDataFactory;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生IDの全件検索が行えること() {
    List<Integer> expected = List.of(1,2,3,4,5);

    List<Integer> actual = sut.searchStudentIdList();

    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 受講生の単一検索が行えること() {
    int studentId = 1;
    Student actual = sut.searchStudent(studentId);

    assertThat(actual).isEqualTo(MyBatisTestDataFactory.makeDummyStudentDetail1().getStudent());
  }

  @Test
  void 存在しない受講生IDを指定したときnullが返ること() {
    int studentId = 999;
    Student actual = sut.searchStudent(studentId);

    assertThat(actual).isNull();
  }

  @Test
  void 詳細検索_条件未指定のとき全受講生IDを返すこと() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();

    List<Integer> actual = sut.searchStudentIdListByCriteria(criteria);

    assertThat(actual).containsExactly(1, 2, 3, 4, 5);
  }

  @Test
  void 詳細検索_fullNameEqで完全一致検索できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.applyFullNameFilter(new SearchFilter(
        "fullName",
        SearchOperator.EQ,
        "田中太郎",
        null
    ));

    List<Integer> actual = sut.searchStudentIdListByCriteria(criteria);

    assertThat(actual).containsExactly(1);
  }

  @Test
  void 詳細検索_fullNameLikeで部分一致検索できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.applyFullNameFilter(new SearchFilter(
        "fullName",
        SearchOperator.CONTAINS,
        "藤",
        null
    ));

    List<Integer> actual = sut.searchStudentIdListByCriteria(criteria);

    assertThat(actual).containsExactly(2);
  }

  @Test
  void 詳細検索_courseCodeInで受講コースのIN検索ができること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.applyCourseCodeFilter(new SearchFilter(
        "courseCode",
        SearchOperator.IN,
        null,
        List.of("JA", "DE")
    ));

    List<Integer> actual = sut.searchStudentIdListByCriteria(criteria);

    assertThat(actual).containsExactly(1, 2, 3, 4);
  }

  @Test
  void 詳細検索_statusIdEqでステータス一致の受講生を検索できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.applyStatusIdFilter(new SearchFilter(
        "statusId",
        SearchOperator.EQ,
        "3",
        null
    ));

    List<Integer> actual = sut.searchStudentIdListByCriteria(criteria);

    assertThat(actual).containsExactly(1, 2, 4);
  }

  @Test
  void 詳細検索_age範囲検索ができること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.applyAgeFilter(new SearchFilter(
        "age",
        SearchOperator.BETWEEN,
        null,
        List.of("22", "32")
    ));

    List<Integer> actual = sut.searchStudentIdListByCriteria(criteria);

    assertThat(actual).containsExactly(1, 2, 3);
  }

  @Test
  void 詳細検索_courseApplyAtの範囲検索ができること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.applyCourseApplyAtFilter(new SearchFilter(
        "courseApplyAt",
        SearchOperator.BETWEEN,
        null,
        List.of("2024-07-01", "2024-07-31")
    ));

    List<Integer> actual = sut.searchStudentIdListByCriteria(criteria);

    assertThat(actual).containsExactly(1, 2, 4);
  }

  @Test
  void 詳細検索_isDeleted_trueで削除済み受講生のみ取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.applyIsDeletedFilter(new SearchFilter(
        "isDeleted",
        SearchOperator.EQ,
        "true",
        null
    ));

    List<Integer> actual = sut.searchStudentIdListByCriteria(criteria);

    assertThat(actual).containsExactly(5);
  }

  @Test
  void 詳細検索_受講生条件と受講コース条件を併用して検索できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.applySexFilter(new SearchFilter(
        "sex",
        SearchOperator.EQ,
        "女",
        null
    ));
    criteria.applyCourseCodeFilter(new SearchFilter(
        "courseCode",
        SearchOperator.EQ,
        "AW",
        null
    ));

    List<Integer> actual = sut.searchStudentIdListByCriteria(criteria);

    assertThat(actual).containsExactly(2);
  }

  @Test
  void 詳細検索_複数の受講コースが条件一致しても受講生IDは重複しないこと() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.applyStatusIdFilter(new SearchFilter(
        "statusId",
        SearchOperator.EQ,
        "3",
        null
    ));

    List<Integer> actual = sut.searchStudentIdListByCriteria(criteria);

    assertThat(actual).containsExactly(1, 2, 4);
    assertThat(actual).doesNotHaveDuplicates();
  }

  @Test
  void 詳細検索_一致するデータがないとき空リストを返すこと() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.applyFullNameFilter(new SearchFilter(
        "fullName",
        SearchOperator.EQ,
        "存在しない受講生",
        null
    ));

    List<Integer> actual = sut.searchStudentIdListByCriteria(criteria);

    assertThat(actual).isEmpty();
  }

  @Test
  void 受講生IDに紐づく受講生コースの一覧を検索できること() {
    int studentId = 2;

    List<StudentCourse> actual = sut.searchStudentCourses(studentId);
    List<StudentCourse> expected = MyBatisTestDataFactory.makeDummyStudentDetail2()
        .getStudentCourses();

    assertThat(actual.size()).isEqualTo(expected.size());
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 存在しない受講生IDを指定したとき空のリストが返ること() {
    int studentId = 999;
    List<StudentCourse> actual = sut.searchStudentCourses(studentId);

    assertThat(actual).isNotNull();
    assertThat(actual).isEmpty();
  }

  @Test
  void 受講生登録が行えること() {
    // Arrange
    List<Student> existing = MyBatisTestDataFactory.makeDummyStudentList();
    int existingSize = existing.size();
    Student beforeRegister = TestDataFactory.makeCompletedStudent(null);

    // Act
    sut.registerStudent(beforeRegister);

    // Assert
    List<Student> expected = new ArrayList<>(existing);
    expected.add(beforeRegister);

    List<Student> actual = sut.searchStudentIdList().stream()
        .map(sut::searchStudent)
        .toList();

    assertThat(beforeRegister.getStudentId()).isNotNull();
    assertThat(actual.size()).isEqualTo(existingSize + 1);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 必須項目がnullの受講生を登録しようとすると例外が発生すること() {
    // Arrange
    Student student = new Student(
        null, // studentId（自動採番）
        null, // fullName ← NOT NULL違反
        "テストカナ",
        "テスト",
        "test@example.com",
        "東京都",
        "090-0000-0000",
        20,
        "男",
        "",
        false
    );

    // Act & Assert
    assertThatThrownBy(() -> sut.registerStudent(student))
        .isInstanceOf(Exception.class);
  }

  @Test
  void 重複したメールアドレスの受講生を登録しようとすると例外が発生すること() {
    // Arrange
    Student student = new Student(
        null,
        "テスト太郎",
        "テストタロウ",
        "テスト",
        "tarotaro@gmail.com", // data.sql に存在
        "東京都",
        "090-0000-0000",
        20,
        "男",
        "",
        false
    );

    // Act & Assert
    assertThatThrownBy(() -> sut.registerStudent(student))
        .isInstanceOf(Exception.class);
  }

  @Test
  void 受講生コース登録を行うことができ_受講生IDとコースコードとステータスIDと受講申込日だけがセットされていること() {
    // Arrange
    Integer studentId = 1;
    List<StudentCourse> expected = new ArrayList<>(
        MyBatisTestDataFactory.makeDummyStudentDetail1().getStudentCourses()
    );
    int originalSize = expected.size();

    StudentCourse beforeRegister =
        TestDataFactory.makeCompletedStudentCourse(studentId, null);

    // Act
    sut.registerStudentCourse(beforeRegister);

    // Assert
    List<StudentCourse> actual = sut.searchStudentCourses(studentId);

    //studentCourseIdが登録前インスタンスに自動でマッピングされることを検証
    assertThat(beforeRegister.getStudentCourseId()).isNotNull();
    //登録前インスタンスにマッピングされたidを使って登録後インスタンスを取得
    StudentCourse afterRegister = actual.stream()
        .filter(sc -> sc.getStudentCourseId().equals(beforeRegister.getStudentCourseId()))
        .findFirst()
        .orElseThrow();
    //もともと受講生が持っていた受講生コースより+1になっていること
    assertThat(actual.size()).isEqualTo(originalSize + 1);
    // 受講生ID,コースコード,ステータスID、受講申込日がセットされていること
    assertThat(afterRegister.getStudentId()).isEqualTo(beforeRegister.getStudentId());
    assertThat(afterRegister.getCourseCode()).isEqualTo(beforeRegister.getCourseCode());
    assertThat(afterRegister.getStatusId()).isEqualTo(beforeRegister.getStatusId());
    assertThat(afterRegister.getCourseApplyAt()).isEqualTo(beforeRegister.getCourseApplyAt());
    // 受講開始日と受講終了日がnullであること
    assertThat(afterRegister.getCourseStartAt()).isNull();
    assertThat(afterRegister.getCoursePlannedEndAt()).isNull();
    assertThat(afterRegister.getCourseFinishedAt()).isNull();
  }

  @Test
  void コースコードがnullの受講生コースを登録しようとすると例外が発生すること() {
    Integer studentId = 1;
    LocalDate today = LocalDate.now();
    StudentCourse studentCourse = new StudentCourse(
        null, studentId, null,1,today, null, null,null
    );

    assertThatThrownBy(() -> sut.registerStudentCourse(studentCourse))
        .isInstanceOf(Exception.class);
  }

  @Test
  void 受講申込日がnullの受講生コースを登録しようとすると例外が発生すること() {
    Integer studentId = 1;
    StudentCourse studentCourse = new StudentCourse(
        null, studentId, "JA", 1, null, null, null,null
    );

    assertThatThrownBy(() -> sut.registerStudentCourse(studentCourse))
        .isInstanceOf(Exception.class);
  }

  @Test
  void 存在しない受講生IDを持つ受講生コースを登録しようとすると例外が発生すること() {
    // Arrange
    Integer studentId = 999;
    StudentCourse studentCourse = TestDataFactory.makeCompletedStudentCourse(studentId, null);

    // Act & Assert
    assertThatThrownBy(() -> sut.registerStudentCourse(studentCourse))
        .isInstanceOf(Exception.class);
  }

  @Test
  void 存在しないコースコードを持つ受講生コースを登録しようとすると例外が発生すること() {
    // Arrange
    Integer studentId = 1;
    LocalDate today = LocalDate.now();
    StudentCourse studentCourse = new StudentCourse(
        null,studentId,"NOT",1,today,null,null,null
    );

    // Act & Assert
    assertThatThrownBy(() -> sut.registerStudentCourse(studentCourse))
        .isInstanceOf(Exception.class);
  }

  @Test
  void 存在しないステータスIDを持つ受講生コースを登録しようとすると例外が発生すること() {
    // Arrange
    Integer studentId = 1;
    LocalDate today = LocalDate.now();
    StudentCourse studentCourse = new StudentCourse(
        null,studentId,"JA",999,today,null,null,null
    );

    // Act & Assert
    assertThatThrownBy(() -> sut.registerStudentCourse(studentCourse))
        .isInstanceOf(Exception.class);
  }

  @Test
  void 受講生の更新が行えること() {
    // Arrange
    Integer studentId = 1;
    Student expected = new Student(
        studentId,
        "高橋太郎",// もとは「田中太郎」
        "たかはしたろう",// もとは「たなかたろう」
        "タロウ",// 「タロー」
        "tarotarotaro@gmail.com",// もとは「tarotaro@gmail.com」
        "茨城県東茨城郡城里町",// もとは「茨城県かすみがうら市」
        "080-9876-5432",// もとは「080-1234-5678」
        35,// もとは「32」
        "その他",// もとは「男」
        "転職活動中",// もとは空文字
        true // もとはfalse
    );

    // Act
    Integer updated = sut.updateStudent(expected);
    Student actual = sut.searchStudent(studentId);

    // Assert
    assertThat(actual).isEqualTo(expected);
    assertThat(updated).isEqualTo(1);
  }

  @Test
  void 存在しない受講生IDを更新しようとすると更新件数が0件であること() {
    Integer studentId = 999;
    Student student = TestDataFactory.makeCompletedStudent(studentId);

    int actual = sut.updateStudent(student);

    assertThat(actual).isZero();
  }

  @Test
  void 受講生コース更新成功_ステータス更新を行うことができコースコードと受講申込日の更新はできないこと() {
    // data.sql存在するそのままのデータ
    Integer studentId = 1;
    Integer scId = 1;
    StudentCourse original = new StudentCourse(
        scId,
        studentId,
        "JA",
        3,
        LocalDate.of(2024, 7, 10),
        LocalDate.of(2024, 7, 15),
        LocalDate.of(2025, 4, 15),
        null
    );

    StudentCourse forUpdate = new StudentCourse(
        scId,
        studentId,
        "AW",
        4,
        LocalDate.of(2023, 10, 10),
        LocalDate.of(2023, 10, 15),
        LocalDate.of(2025, 12, 15),
        LocalDate.of(2025, 12, 10)
    );

    // Act
    Integer updated = sut.updateStudentCourseStatus(forUpdate);

    StudentCourse actual = sut.searchStudentCourses(studentId).stream()
        .filter(sc -> sc.getStudentCourseId().equals(scId))
        .findFirst()
        .orElseThrow();

    // Assert
    assertThat(updated).isEqualTo(1);
    assertThat(actual.getCourseCode()).isEqualTo(original.getCourseCode());
    assertThat(actual.getStatusId()).isEqualTo(forUpdate.getStatusId());
    assertThat(actual.getCourseApplyAt()).isEqualTo(original.getCourseApplyAt());

  }

  @ParameterizedTest(name = "[{index}] {0}がnullでその他の日付フィールドはnullでない場合、{0}は更新されずそれ以外は更新されること(※courseApplyAtは除く)")
  @ValueSource(strings = {"courseStartAt","coursePlannedEndAt","courseFinishedAt"})
  void 受講生コース更新成功_受講開始日と受講終了予定日と受講終了実績日はnullでない場合のみ更新できること(String fieldName){
    // data.sql存在するそのままのデータ
    Integer studentId = 1;
    Integer scId = 1;
    StudentCourse original = new StudentCourse(
        scId,
        studentId,
        "JA",
        3,
        LocalDate.of(2024, 7, 10),
        LocalDate.of(2024, 7, 15),
        LocalDate.of(2025, 4, 15),
        null
    );

    StudentCourse forUpdate = new StudentCourse(
        scId,
        studentId,
        "JA",
        3,
        LocalDate.of(2024, 7, 10),
        fieldName.equals("courseStartAt") ? null : LocalDate.of(2023, 10, 15),
        fieldName.equals("coursePlannedEndAt") ? null : LocalDate.of(2025, 12, 15),
        fieldName.equals("courseFinishedAt") ? null :LocalDate.of(2025, 12, 10)
    );

    // Act
    Integer updated = sut.updateStudentCourseStatus(forUpdate);

    StudentCourse actual = sut.searchStudentCourses(studentId).stream()
        .filter(sc -> sc.getStudentCourseId().equals(scId))
        .findFirst()
        .orElseThrow();

    // Assert
    assertThat(updated).isEqualTo(1);
    assertThat(actual.getCourseStartAt()).isEqualTo(
        fieldName.equals("courseStartAt") ? original.getCourseStartAt() : forUpdate.getCourseStartAt());
    assertThat(actual.getCoursePlannedEndAt()).isEqualTo(
        fieldName.equals("coursePlannedEndAt") ? original.getCoursePlannedEndAt() : forUpdate.getCoursePlannedEndAt());
    assertThat(actual.getCourseFinishedAt()).isEqualTo(
        fieldName.equals("courseFinishedAt") ? original.getCourseFinishedAt() : forUpdate.getCourseFinishedAt());
  }


  @Test
  void 受講生コース更新失敗_存在する受講生コースIDと異なる受講生IDの組み合わせでは更新件数が0件であること() {
    int scId = 1;
    int studentId = 99;
    StudentCourse course = TestDataFactory.makeCompletedStudentCourse(studentId, scId);

    int actual = sut.updateStudentCourseStatus(course);

    assertThat(actual).isZero();
  }

  @Test
  void 受講生コース更新失敗_存在しない受講生コースIDを更新しようとすると更新件数が0件であること() {
    int scId = 999;
    StudentCourse course = TestDataFactory.makeCompletedStudentCourse(1, scId);

    int actual = sut.updateStudentCourseStatus(course);

    assertThat(actual).isZero();
  }

  @Test
  void 受講生コースIDと受講生IDに紐づく現在のステータスIDを取得できること() {
    // student_course_id=1 のレコードは student_id=1, status_id=3
    Integer studentId = 1;
    Integer scId = 1;
    StudentCourse studentCourse =
        new StudentCourse(scId,studentId,null,null,null,null,null,null);
    Integer actual = sut.findStatusId(studentCourse);

    assertThat(actual).isEqualTo(3);
  }

  @Test
  void 存在する受講生コースIDでも受講生IDが一致しないとnullが返ること() {
    // student_course_id=1 は student_id=1 に紐づくので、2を渡すと不一致
    Integer studentId = 1;
    Integer scId = 2;
    StudentCourse studentCourse =
        new StudentCourse(scId,studentId,null,null,null,null,null,null);
    Integer actual = sut.findStatusId(studentCourse);

    assertThat(actual).isNull();
  }

  @Test
  void 存在しない受講生コースIDを指定したときnullが返ること() {
    Integer studentId = 1;
    Integer scId = 999;
    StudentCourse studentCourse =
        new StudentCourse(scId,studentId,null,null,null,null,null,null);
    Integer actual = sut.findStatusId(studentCourse);


    assertThat(actual).isNull();
  }


  @Test
  void アクティブな受講生IDが存在するときtrueを返すこと() {
    // Act
    // 初期データ5件が1~5で採番されるという前提
    boolean actual = sut.existsActiveStudentById(1);

    // Assert
    assertThat(actual).isTrue();
  }

  @Test
  void 存在しない受講生IDのときfalseを返すこと() {
    // Act
    boolean actual = sut.existsActiveStudentById(999);

    // Assert
    assertThat(actual).isFalse();
  }
}