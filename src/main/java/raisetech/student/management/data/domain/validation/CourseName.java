package raisetech.student.management.data.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CourseNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CourseName {

  String message() default "コース名は「Javaコース」, 「AWSコース」,「デザインコース」,「Webマーケティングコース」,「フロントエンドコース」のいずれかを入力してください";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

