package youness.automotive.internal.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static youness.automotive.internal.validator.UUIDConstraint.UUID_REGEX;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * UUID validator
 * Based on https://stackoverflow.com/questions/37320870/is-there-a-uuid-validator-annotation
 */
@Target(ElementType.FIELD)
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Pattern(regexp = UUID_REGEX)
public @interface UUIDConstraint {
    String UUID_REGEX = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    String message() default "{invalid.uuid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
