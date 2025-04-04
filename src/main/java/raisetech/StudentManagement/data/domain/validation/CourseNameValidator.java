package raisetech.StudentManagement.data.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class CourseNameValidator implements ConstraintValidator<CourseName, String> {

  private static final List<String> ALLOWED_COURSES = Arrays.asList("Javaコース", "AWSコース",
      "デザインコース", "Webマーケティングコース", "フロントエンドコース");

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }
    return ALLOWED_COURSES.contains(value);
  }
}

