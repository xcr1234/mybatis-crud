package com.mybatis.crud.anno;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OrderBy {

    String value();

    Type type() default Type.ASC;

    enum Type{
        DEFAULT,DESC,ASC
    }

}
