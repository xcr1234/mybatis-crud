package com.mybatis.crud.session;

import com.mybatis.crud.config.CrudConfiguration;
import org.apache.ibatis.session.*;

import java.sql.Connection;
import java.sql.SQLException;

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
        boolean autoCommit;
        try {
            autoCommit = connection.getAutoCommit();
        } catch (SQLException e) {
            // Failover to true, as most poor drivers
            // or databases won't support transactions
            autoCommit = true;
        }
        CrudSqlSession sqlSession = wrap(sqlSessionFactory.openSession(connection));
        sqlSession.getCrudConfiguration().setAutoCommit(autoCommit);
        return sqlSession;
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
    public SqlSession openSession(ExecutorType executorType, boolean autoCommit) {
        CrudSqlSession sqlSession = wrap(sqlSessionFactory.openSession(executorType, autoCommit));
        sqlSession.getCrudConfiguration().setAutoCommit(autoCommit);
        return sqlSession;
    }

    @Override
    public SqlSession openSession(ExecutorType executorType, TransactionIsolationLevel transactionIsolationLevel) {
        CrudSqlSession sqlSession = wrap(sqlSessionFactory.openSession(executorType, transactionIsolationLevel));
        sqlSession.getCrudConfiguration().setAutoCommit(false);
        return sqlSession;
    }

    @Override
    public SqlSession openSession(ExecutorType executorType, Connection connection) {
        boolean autoCommit;
        try {
            autoCommit = connection.getAutoCommit();
        } catch (SQLException e) {
            // Failover to true, as most poor drivers
            // or databases won't support transactions
            autoCommit = true;
        }
        CrudSqlSession sqlSession = wrap(sqlSessionFactory.openSession(executorType, connection));
        sqlSession.getCrudConfiguration().setAutoCommit(autoCommit);
        return sqlSession;
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
