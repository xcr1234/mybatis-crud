package com.mybatis.crud.config;

import freemarker.template.TemplateException;

import java.io.IOException;

public interface MapperXmlBuilder {
    String build(CrudConfiguration configuration,Class<?> c,String name) throws IOException, TemplateException;
}
