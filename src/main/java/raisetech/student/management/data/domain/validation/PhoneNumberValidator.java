package raisetech.student.management.data.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

  private static final String PHONE_PATTERN = "^(0\\d{1,4}-\\d{1,4}-\\d{4})$";

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    return value.matches(PHONE_PATTERN);
  }
}

