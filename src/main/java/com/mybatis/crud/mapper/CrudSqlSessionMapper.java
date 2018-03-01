package com.mybatis.crud.mapper;


import com.mybatis.crud.config.CrudConfiguration;
import com.mybatis.crud.misc.Meta;
import freemarker.template.TemplateException;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 执行基本CRUD操作的Mapper
 */
public class  CrudSqlSessionMapper implements CrudMapper {

    private SqlSession sqlSession;
    private String namespace;

    private CrudConfiguration configuration;

    private static Log logger = LogFactory.getLog(CrudMapper.class);



    public CrudSqlSessionMapper(SqlSession sqlSession, CrudConfiguration configuration, Class<?> type) throws IOException, TemplateException {
        this.sqlSession = sqlSession;
        if(configuration == null){
            configuration = new CrudConfiguration();
        }
        this.configuration = configuration;
        this.namespace =  configuration.getMapperNameGenerator().getMapperName(type);
        String xmlContent = configuration.getMapperXmlBuilder().build(configuration,type, namespace);
        if(logger.isDebugEnabled()){
            logger.debug("crud mapper generated:" + namespace);
        }
        InputStream in = new ByteArrayInputStream(xmlContent.getBytes());
        XMLMapperBuilder builder = new XMLMapperBuilder(in,sqlSession.getConfiguration(), namespace,sqlSession.getConfiguration().getSqlFragments());
        builder.parse();
    }



    @Override
    public boolean save(Object o) {
        checkId(o);
        int r = sqlSession.insert(namespace + ".save",o);
        if(configuration.isAutoCommit()){
            sqlSession.commit();
        }
        return r > 0;
    }

    @Override
    public boolean delete(Object o) {
        checkId(o);
        int r = sqlSession.delete(namespace + ".delete",o);
        if(configuration.isAutoCommit()){
            sqlSession.commit();
        }
        return r > 0;
    }

    @Override
    public boolean deleteByID(Object o) {
        checkArg(o);
        int r = sqlSession.delete(namespace + ".deleteByID",o);
        if(configuration.isAutoCommit()){
            sqlSession.commit();
        }
        return r > 0;
    }

    @Override
    public int deleteAll() {
        int r =  sqlSession.delete(namespace + ".deleteAll");
        if(configuration.isAutoCommit()){
            sqlSession.commit();
        }
        return r;
    }

    @Override
    public boolean update(Object o) {
        checkId(o);
        int r = sqlSession.update(namespace + ".update",o);
        if(configuration.isAutoCommit()){
            sqlSession.commit();
        }
        return r > 0;
    }

    @Override
    public boolean exists(Object o) {
        checkArg(o);
        return sqlSession.selectOne(namespace + ".exists" , o) != null;
    }

    @Override
    public int count() {
        return (Integer)sqlSession.selectOne(namespace + ".count");
    }

    @Override
    public int count(Object o) {
        return (Integer)sqlSession.selectOne(namespace + ".count",o);
    }

    @Override
    public Object findOne(Object o) {
        checkArg(o);
        return sqlSession.selectOne(namespace + ".find",o);
    }

    @Override
    public List findAll() {
        return sqlSession.selectList(namespace + ".find");
    }


    @Override
    public List findAll(Object o) {
        checkArg(o);
        return sqlSession.selectList(namespace + ".find",o);
    }

    private void checkArg(Object arg){
        if(arg == null){
            Exception e = new IllegalArgumentException("the arg can't be null!");
            throw ExceptionFactory.wrapException(e.getMessage(),e);
        }
    }

    private void checkId(Object arg){
        checkArg(arg);
        Meta meta = Meta.parse(arg.getClass(),configuration);
        Object idValue = meta.getIdColumn().get(arg);
        if(idValue == null){
            Exception e = new IllegalArgumentException("the value of id column can't be null!");
            throw ExceptionFactory.wrapException("cannot update/delete while id is null!",e);
        }
    }

}
