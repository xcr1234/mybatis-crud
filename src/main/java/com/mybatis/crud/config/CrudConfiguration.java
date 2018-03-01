package com.mybatis.crud.config;

import com.mybatis.crud.naming.DefaultTableNameStrategy;
import com.mybatis.crud.naming.NameStrategy;

public class CrudConfiguration {

    private NameStrategy tableNameStrategy = DefaultTableNameStrategy.DEFAULT;
    private NameStrategy columnNameStrategy = DefaultTableNameStrategy.DEFAULT;
    private MapperNameGenerator mapperNameGenerator = new DefaultMapperNameGenerator();
    private MapperXmlBuilder mapperXmlBuilder = new DefaultMapperXmlBuilder();

    private boolean autoCommit = true;

    private String schema;

    private String database;

    private boolean collectGenerateId = false;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public NameStrategy getTableNameStrategy() {
        return tableNameStrategy;
    }

    public void setTableNameStrategy(NameStrategy tableNameStrategy) {
        this.tableNameStrategy = tableNameStrategy;
    }

    public NameStrategy getColumnNameStrategy() {
        return columnNameStrategy;
    }

    public void setColumnNameStrategy(NameStrategy columnNameStrategy) {
        this.columnNameStrategy = columnNameStrategy;
    }

    public boolean isCollectGenerateId() {
        return collectGenerateId;
    }

    public void setCollectGenerateId(boolean collectGenerateId) {
        this.collectGenerateId = collectGenerateId;
    }

    public MapperNameGenerator getMapperNameGenerator() {
        return mapperNameGenerator;
    }

    public void setMapperNameGenerator(MapperNameGenerator mapperNameGenerator) {
        this.mapperNameGenerator = mapperNameGenerator;
    }

    public MapperXmlBuilder getMapperXmlBuilder() {
        return mapperXmlBuilder;
    }

    public void setMapperXmlBuilder(MapperXmlBuilder mapperXmlBuilder) {
        this.mapperXmlBuilder = mapperXmlBuilder;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
}
