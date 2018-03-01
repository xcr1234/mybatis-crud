package com.mybatis.crud.config;

public interface MapperNameGenerator {
    String getMapperName(Class<?> type);
}
