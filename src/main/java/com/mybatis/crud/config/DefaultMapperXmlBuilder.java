package com.mybatis.crud.config;

import com.mybatis.crud.mapper.CrudMapper;
import com.mybatis.crud.misc.Meta;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class DefaultMapperXmlBuilder implements MapperXmlBuilder {

    private static final Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
    private static Template TEMPLATE;
    static {
        configuration.setTemplateLoader(new ClassTemplateLoader(CrudMapper.class,""));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        try {
            TEMPLATE = configuration.getTemplate("crud-template.ftl");
        } catch (IOException e) {
            throw new RuntimeException("can't load template",e);
        }
    }

    public static Template getTemplate() {
        return TEMPLATE;
    }

    public static void setTemplate(Template TEMPLATE) {
        DefaultMapperXmlBuilder.TEMPLATE = TEMPLATE;
    }

    @Override
    public String build(CrudConfiguration configuration,Class<?> c, String name) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        Meta meta = Meta.parse(c,configuration);
        Map<String,Object> context = new HashMap<String, Object>();
        context.put("meta",meta);
        context.put("namespace",name);
        TEMPLATE.process(context,writer);
        return writer.toString();
    }
}
