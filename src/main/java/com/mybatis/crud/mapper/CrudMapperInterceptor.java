package com.mybatis.crud.mapper;

import com.mybatis.crud.config.CrudConfiguration;
import freemarker.template.TemplateException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态代理CrudSqlSessionMapper，
 * 如果是Mybatis原生mapper中的方法，则由原生mapper执行，否则由crud框架执行。
 *
 * @see CrudSqlSessionMapper
 */
public class CrudMapperInterceptor implements MethodInterceptor{


    private Object mapper;
    private CrudSqlSessionMapper crudSqlSessionMapper;
    private static Map<Class,CrudSqlSessionMapper> mapperMap = new ConcurrentHashMap<Class, CrudSqlSessionMapper>();


    public CrudMapperInterceptor(SqlSession sqlSession, CrudConfiguration configuration
            , Object mapper, Class<?> type) throws IOException, TemplateException {
        this.mapper = mapper;
        CrudSqlSessionMapper sqlSessionMapper = mapperMap.get(type);
        if(sqlSessionMapper == null){
            mapperMap.put(type,sqlSessionMapper = new CrudSqlSessionMapper(sqlSession,configuration,getIdType(type)));
        }
        this.crudSqlSessionMapper = sqlSessionMapper;
    }

    private Class<?> getIdType(Class<?> c){
        if(!c.isInterface()){
            throw new IllegalArgumentException("mapper class '" + c.getName() + "' should be an interface.");
        }
        Type[] types = c.getGenericInterfaces();
        for(Type t:types){
            if(t instanceof ParameterizedType){
                ParameterizedType pt = (ParameterizedType) t;
                Type[] arguments = pt.getActualTypeArguments();
                if(arguments.length == 2){
                    Type t0 = arguments[0];
                    if(t0 instanceof Class){
                        return (Class)t0;
                    }
                }
            }
        }
        throw new IllegalArgumentException("illegal crud mapper class: '" + c.getName() + "',mapper interface class should extends CrudMapper<T,ID>");
    }

    public <T> T build(Class<T> tClass){
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{tClass, MapperAware.class});
        enhancer.setCallback(this);
        return tClass.cast(enhancer.create());
    }


    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if("getOriginMapper".equals(method.getName()) && method.getParameterTypes().length == 0){
            return mapper;
        }
        Object base = proxyBaseMethods(o,method,args,methodProxy);
        if(base != null){
            return base;
        }
        Method m = findMethod(CrudMapper.class,method.getName(),method.getParameterTypes());
        if(m != null){
            return process(m,crudSqlSessionMapper,args);
        }
        Method mybatisMethod = mapper.getClass().getDeclaredMethod(method.getName(),method.getParameterTypes());
        return process(mybatisMethod,mapper,args);
    }

    private Object proxyBaseMethods(Object o, Method method, Object[] args, MethodProxy proxy) throws  Throwable{
        if("toString".equals(method.getName()) && method.getParameterTypes().length == 0){
            return proxy.invokeSuper(o,args);
        }
        if("getClass".equals(method.getName()) && method.getParameterTypes().length == 0){
            return proxy.invokeSuper(o,args);
        }
        if("equals".equals(method.getName()) && method.getParameterTypes().length == 1){
            return proxy.invokeSuper(o,args);
        }
        if("hashCode".equals(method.getName()) && method.getParameterTypes().length == 0){
            return proxy.invokeSuper(o,args);
        }
        return null;
    }


    private static Object process(Method method,Object self,Object [] args) throws Throwable{
        try {
            return method.invoke(self,args);
        }catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }


    private static Method findMethod(Class<?> c, String method, Class[] argTypes){
        try {
            return c.getDeclaredMethod(method,argTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
