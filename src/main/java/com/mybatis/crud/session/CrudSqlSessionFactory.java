package com.mybatis.crud.session;

import com.mybatis.crud.config.CrudConfiguration;
import org.apache.ibatis.session.*;

import java.sql.Connection;

/**
 * 默认SqlSessionFactory的代理类，可以分离出{@link CrudSqlSession}
 */
public class CrudSqlSessionFactory implements SqlSessionFactory{


    private SqlSessionFactory sqlSessionFactory;
    private CrudConfiguration configuration;

    public CrudSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this(sqlSessionFactory,null);
    }

    public CrudSqlSessionFactory(SqlSessionFactory sqlSessionFactory, CrudConfiguration configuration) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return wrap(sqlSessionFactory.openSession());
    }

    @Override
    public SqlSession openSession(boolean autoCommit) {
        CrudSqlSession sqlSession = wrap(sqlSessionFactory.openSession(autoCommit));
        sqlSession.getCrudConfiguration().setAutoCommit(autoCommit);
        return sqlSession;
    }

    @Override
    public SqlSession openSession(Connection connection) {
        return wrap(sqlSessionFactory.openSession(connection));
    }

    @Override
    public SqlSession openSession(TransactionIsolationLevel transactionIsolationLevel) {
        CrudSqlSession sqlSession = wrap(sqlSessionFactory.openSession(transactionIsolationLevel));
        sqlSession.getCrudConfiguration().setAutoCommit(false);
        return sqlSession;
    }

    @Override
    public SqlSession openSession(ExecutorType executorType) {
        return wrap(sqlSessionFactory.openSession(executorType));
    }

    @Override
    public SqlSession openSession(ExecutorType executorType, boolean b) {
        return wrap(sqlSessionFactory.openSession(executorType, b));
    }

    @Override
    public SqlSession openSession(ExecutorType executorType, TransactionIsolationLevel transactionIsolationLevel) {
        return wrap(sqlSessionFactory.openSession(executorType, transactionIsolationLevel));
    }

    @Override
    public SqlSession openSession(ExecutorType executorType, Connection connection) {
        return wrap(sqlSessionFactory.openSession(executorType, connection));
    }

    @Override
    public Configuration getConfiguration() {
        return sqlSessionFactory.getConfiguration();
    }

    public SqlSessionFactory getOriginFactory() {
        return sqlSessionFactory;
    }

    private CrudSqlSession wrap(SqlSession sqlSession){
        return new CrudSqlSession(sqlSession,configuration);
    }
}
