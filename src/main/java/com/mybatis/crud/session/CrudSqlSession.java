package com.mybatis.crud.session;

import com.mybatis.crud.config.CrudConfiguration;
import com.mybatis.crud.mapper.CrudMapper;
import com.mybatis.crud.mapper.CrudMapperInterceptor;

import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Mybatis框架默认SqlSession的代理类，分离出基本CRUD的Mapper.
 * @see #getMapper(Class)
 */
public class CrudSqlSession implements SqlSession{

    private SqlSession sqlSession;
    private CrudConfiguration configuration;


    public CrudSqlSession(SqlSession sqlSession, CrudConfiguration configuration) {
        this.sqlSession = sqlSession;
        this.configuration = configuration;
    }


    @Override
    public <T> T selectOne(String s) {
        return sqlSession.selectOne(s);
    }

    @Override
    public <T> T selectOne(String s, Object o) {
        return sqlSession.selectOne(s, o);
    }

    @Override
    public <E> List<E> selectList(String s) {
        return sqlSession.selectList(s);
    }

    @Override
    public <E> List<E> selectList(String s, Object o) {
        return sqlSession.selectList(s, o);
    }

    @Override
    public <E> List<E> selectList(String s, Object o, RowBounds rowBounds) {
        return sqlSession.selectList(s, o, rowBounds);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String s, String s1) {
        return sqlSession.selectMap(s,s1);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String s, Object o, String s1) {
        return sqlSession.selectMap(s,o,s1);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String s, Object o, String s1, RowBounds rowBounds) {
        return sqlSession.selectMap(s,o,s1,rowBounds);
    }

    @Override
    public void select(String s, Object o, ResultHandler resultHandler) {
        sqlSession.select(s, o, resultHandler);
    }

    @Override
    public void select(String s, ResultHandler resultHandler) {
        sqlSession.select(s, resultHandler);
    }

    @Override
    public void select(String s, Object o, RowBounds rowBounds, ResultHandler resultHandler) {
        sqlSession.select(s, o, rowBounds, resultHandler);
    }

    @Override
    public int insert(String s) {
        return sqlSession.insert(s);
    }

    @Override
    public int insert(String s, Object o) {
        return sqlSession.insert(s, o);
    }

    @Override
    public int update(String s) {
        return sqlSession.update(s);
    }

    @Override
    public int update(String s, Object o) {
        return sqlSession.update(s, o);
    }

    @Override
    public int delete(String s) {
        return sqlSession.delete(s);
    }

    @Override
    public int delete(String s, Object o) {
        return sqlSession.delete(s, o);
    }

    @Override
    public void commit() {
        sqlSession.commit();
    }

    @Override
    public void commit(boolean b) {
        sqlSession.commit(b);
    }

    @Override
    public void rollback() {
        sqlSession.rollback();
    }

    @Override
    public void rollback(boolean b) {
        sqlSession.rollback(b);
    }

    @Override
    public List<BatchResult> flushStatements() {
        return sqlSession.flushStatements();
    }

    @Override
    public void close() {
        sqlSession.close();
    }

    @Override
    public void clearCache() {
        sqlSession.clearCache();
    }

    @Override
    public Configuration getConfiguration() {
        return sqlSession.getConfiguration();
    }

    @Override
    public <T> T getMapper(Class<T> aClass) {
        T mapper = null;
        if(sqlSession.getConfiguration().hasMapper(aClass)){
            mapper = sqlSession.getMapper(aClass);
        }
        if(!CrudMapper.class.isAssignableFrom(aClass)){
            if(mapper == null){
                return sqlSession.getMapper(aClass);
            }else{
                return mapper;
            }
        }
        CrudMapperInterceptor interceptor = null;
        try {
            interceptor = new CrudMapperInterceptor(sqlSession,configuration,mapper,aClass);
        } catch (Exception e) {
            ErrorContext.instance().resource("create crud mapper:" +aClass.getName());
            throw ExceptionFactory.wrapException("can't create mapper interface interceptor",e);
        }
        return interceptor.build(aClass);
    }

    @Override
    public Connection getConnection() {
        return sqlSession.getConnection();
    }
}
