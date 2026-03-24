package raisetech.student.management.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.repository.CourseMasterRepository; // 仮のRepository/Mapper

public class CourseCodeValidator implements ConstraintValidator<CourseCodeExists, String> {

  private final CourseMasterRepository repository;

  @Autowired
  public CourseCodeValidator(CourseMasterRepository repository) {
    this.repository = repository;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // nullの場合は@NotNullなどで別途ハンドリングするため、ここではtrueを返す
    if (value == null || value.isEmpty()) {
      return true;
    }

    // DBに存在するかチェック（存在すればtrue、しなければfalseを返すメソッドを呼ぶ）
    return repository.existsByCourseCode(value);
  }
}