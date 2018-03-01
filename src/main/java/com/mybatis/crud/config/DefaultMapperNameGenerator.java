package com.mybatis.crud.config;

public class DefaultMapperNameGenerator implements MapperNameGenerator {

    @Override
    public String getMapperName(Class<?> type) {
        return "crud$$" + type.getName() + "#generated";
    }
}
