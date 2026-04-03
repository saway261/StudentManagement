package raisetech.student.management.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SearchFilterValidator.class)
public @interface ValidSearchFilter {
  String message() default "検索フィルタの設定が正しくありません。";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}