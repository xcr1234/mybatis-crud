package com.mybatis.crud.anno;


import org.apache.ibatis.type.TypeHandler;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface Converter {

    Class<? extends TypeHandler> value();



}
