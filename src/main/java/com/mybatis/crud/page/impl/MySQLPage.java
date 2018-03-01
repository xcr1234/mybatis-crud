package com.mybatis.crud.page.impl;


import com.mybatis.crud.page.Page;
import com.mybatis.crud.page.PageSql;

public class MySQLPage implements PageSql {
    @Override
    public String buildSql(Page page, String sql) {
        return sql + " limit "+ page.offset() + "," + page.limit() ;
    }

    @Override
    public String database() {
        return "mysql";
    }
}
