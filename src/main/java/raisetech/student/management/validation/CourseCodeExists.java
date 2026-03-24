package raisetech.student.management.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CourseCodeValidator.class)
public @interface CourseCodeExists {
  String message() default "指定されたコースコードは存在しません。";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}