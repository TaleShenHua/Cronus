package ink.ptms.cronus.internal.condition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author 坏黑
 * @Since 2019-05-11 14:34
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cond {

    String name();

    String pattern();

    String example() default "null";

}