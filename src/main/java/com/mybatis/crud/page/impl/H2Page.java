package com.mybatis.crud.page.impl;

import com.mybatis.crud.page.Page;
import com.mybatis.crud.page.PageSql;

public class H2Page implements PageSql {
    @Override
    public String buildSql(Page page, String sql) {
        return sql + " limit "+ page.limit() +" offset " + page.offset();
    }


    @Override
    public String database() {
        return "h2";
    }
}
