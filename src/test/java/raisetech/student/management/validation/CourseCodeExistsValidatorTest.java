package raisetech.student.management.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.repository.CourseRepository;

@ExtendWith(MockitoExtension.class)
class CourseCodeExistsValidatorTest {

  private CourseCodeExistsValidator sut;

  @Mock
  private CourseRepository repository;

  @Mock
  private ConstraintValidatorContext context;

  @BeforeEach
  void setUp() {
    sut = new CourseCodeExistsValidator(repository);
  }

  @Test
  void DBにコースコードが存在する場合trueを返すこと() {
    // Arrange
    when(repository.existsByCourseCode("JA")).thenReturn(true);

    // Act
    boolean result = sut.isValid("JA", context);

    // Assert
    assertThat(result).isTrue();
  }

  @Test
  void DBにコースコードが存在しない場合falseを返すこと() {
    // Arrange
    when(repository.existsByCourseCode("INVALID")).thenReturn(false);

    // Act
    boolean result = sut.isValid("INVALID", context);

    // Assert
    assertThat(result).isFalse();
  }

  @Test
  void 入力値がnullの場合trueを返すこと() {//NotNullは別のアノテーションで担保するため
    // Act
    boolean result = sut.isValid(null, context);

    // Assert
    assertThat(result).isTrue();
  }

  @Test
  void 入力値が空文字の場合trueを返すこと() {
    // Act
    boolean result = sut.isValid("", context);

    // Assert
    assertThat(result).isTrue();
  }
}