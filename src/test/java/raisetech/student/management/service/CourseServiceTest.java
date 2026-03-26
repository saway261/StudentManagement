package raisetech.student.management.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.data.master.Course;
import raisetech.student.management.exception.TargetNotFoundException;
import raisetech.student.management.repository.CourseRepository;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

  @Mock
  private CourseRepository repository;

  @InjectMocks
  private CourseService sut;

  @Test
  void コース一覧検索_リポジトリの処理を呼び出していること(){
    // Arrange
    Course course1 = new Course("JA","Javaコース");
    Course course2 = new Course("PY","Pythonコース");

    List<Course> expected = List.of(course1,course2);
    Mockito.when(repository.searchCourseList()).thenReturn(expected);

    // Act
    List<Course> actual = sut.searchCourseMasterList();

    // Assert
    verify(repository,times(1)).searchCourseList();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void コース新規登録_リポジトリの処理を呼び出していること(){
    // Arrange
    Course course = new Course("JA","Javaコース");

    // Act
    sut.registerCourse(course);

    // Assert
    verify(repository,times(1)).registerCourse(course);
  }

  @Test
  void コース更新成功_リポジトリの処理を呼び出し更新件数が1のとき例外を投げないこと(){
    // Arrange
    Course course = new Course("JA","Javaコース");
    Mockito.when(repository.updateCourse(course)).thenReturn(1);

    // Act & Assert
    Assertions.assertDoesNotThrow(() -> sut.updateCourse(course));
    verify(repository,times(1)).updateCourse(course);
  }

  @Test
  void コース更新失敗_リポジトリの処理を呼び出し更新件数が0のとき例外を投げること(){
    // Arrange
    Course course = new Course("NOT","存在しないコース");
    Mockito.when(repository.updateCourse(course)).thenReturn(0);

    // Act & Assert
    Assertions.assertThrows(TargetNotFoundException.class, () -> {
      sut.updateCourse(course);
    });
    verify(repository,times(1)).updateCourse(course);

  }
  

}