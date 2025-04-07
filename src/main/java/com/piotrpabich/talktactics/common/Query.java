package com.piotrpabich.talktactics.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    String propName() default "";

    Type type() default Type.EQUAL;

    String joinName() default "";

    Join join() default Join.LEFT;

    String blurry() default "";

    enum Type {
        EQUAL, GREATER_THAN, LESS_THAN, INNER_LIKE, LEFT_LIKE, RIGHT_LIKE, LESS_THAN_NQ, IN, NOT_IN, NOT_EQUAL, BETWEEN, NOT_NULL, IS_NULL,
        FIND_IN_SET
    }

    enum Join {
        LEFT, RIGHT, INNER
    }

}
