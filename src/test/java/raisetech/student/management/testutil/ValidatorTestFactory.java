package raisetech.student.management.testutil;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import raisetech.student.management.repository.CourseRepository;
import raisetech.student.management.validation.CourseCodeExistsValidator;

/**
 * カスタムバリデータの依存をモックで注入したValidatorを手動で構築するためのクラスです
 */
public class ValidatorTestFactory {

  /**
   * インスタンス化を防ぐprivateコンストラクタ
   */
  private ValidatorTestFactory() {
  }

  public static Validator createValidator(CourseRepository courseRepository) {
    // 全てのフィールドが未初期化のLocalValidatorFactoryBeanインスタンスを生成
    LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();

    // CourseCodeValidator に Mock の Repository を渡して生成できるようにするため、
    // ConstraintValidatorFactory を実装した無名クラスで Validator の生成処理を差し替えたインスタンスをset
    factoryBean.setConstraintValidatorFactory(new ConstraintValidatorFactory() {
      @Override
      public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        if (key == CourseCodeExistsValidator.class) {
          return key.cast(new CourseCodeExistsValidator(courseRepository));
        }
        try {
          return key.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
          throw new RuntimeException("バリデーターの生成に失敗しました: " + key.getName(), e);
        }
      }

      @Override
      public void releaseInstance(ConstraintValidator<?, ?> instance) {
      }
    });

    // constraintValidatorFactory以外のフィールドも初期化してreturn
    factoryBean.afterPropertiesSet();
    return factoryBean;
  }
}