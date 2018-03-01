package com.mybatis.crud.anno;

import java.lang.annotation.*;

/**
 * 表示该列不参与数据库持久化过程
 */
@Documented
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transient {
}
