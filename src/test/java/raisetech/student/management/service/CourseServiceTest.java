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
  

}