package com.mybatis.crud.anno;

import java.lang.annotation.*;

/**
 * 表示是一个表 表名与类名不同时 使用
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * 该类在数据库中数据表的表名
     * @return 表名
     */
    String value() default "";

    String schema() default "";
}
