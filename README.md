# Mybatis增强——基本增删改查框架

## Mybatis - Crud

MyBatis增强工具，它可以根据一些方法约定，自动生成SQL，自动完成增删改查操作

### 原理：

用Freemarker动态生成Mapper Xml，然后加载到Mybatis环境去。

### 为什么不造一个Orm框架的轮子？

MyBatis 是一款优秀的持久层框架，它提供了强大的结果集映射、动态SQL、缓存等功能。  
虽然MyBatis官方提供了MyBatis Generator，但是这些工具都是一次性生成DAO以及SQL，后期维护成本依然比较高，每次增减字段都需要手动改，如果有手写的SQL还要手动DIFF，也比较麻烦。  
Mybatis - Crud，每次运行都是动态生成SQL，省去了这些复杂的维护步骤；同时，也能够使用到结果集映射、缓存等这些功能

Why not Hibernate ？

## 安装

框架依赖于`freemarker`、`cglib`、`asm`，使用时确保引入了这三个jar包

jar包：

[mybatis-crud.jar](https://github.com/xcr1234/mybatis-crud/blob/master/mybatis-crud.jar?raw=true)

如果使用Maven，推荐使用源码依赖的方式：

pom中引入：

```xml
<dependencies>
    <dependency>
            <groupId>cglib</groupId>
            <artifactId>cglib</artifactId>
            <version>2.2</version>
    </dependency>
    <dependency>
            <groupId>org.freemarker</groupId>
            <artifactId>freemarker</artifactId>
            <version>2.3.26-incubating</version>
    </dependency>
</dependencies>
```

克隆本项目，把`com.mybatis.crud`包下所有的代码，引入到自己项目中

## 如何使用

### 创建实体类

```java
@Table("tb_user")    //使用 Table 标识后，表名为@Table的值，如果没有 Table 标识，默认是类名）。
public class User {
    @Id
    private Integer id;
    @Column("username")  //使用 Column 标识后的属性使用标识的值来对应表中的列
    private String name;
    public User(){
        //必须有无参数默认构造函数
    }
    //省略get、set、toString方法
}
```


**实体类的@Id和无参数默认构造函数是必须有的，且不可为final，否则会抛出`BeanException`。**

### Mapper接口类

```java
public interface UserMapper extends CrudMapper<User,Integer>{

}
```


### 查询

```java
class Test{
    public static void main(String[] args) {
        SqlSessionFactory sessionFactory = xxx ; //通过Mybatis方式得到SqlSessionFactory

        CrudConfiguration configuration = new CrudConfiguration();//用于自定义配置，事务提交等
        SqlSessionFactory sqlSessionFactory = new CrudSqlSessionFactory(sessionFactory,configuration);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try{
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);

            System.out.println(mapper.findAll());

        }finally {
            sqlSession.close();
        }
    }
}
```

### 事务

```java
configuration.setAutoCommit(false);
mapper.update(user);
sqlSession.commit();
```

### 在Spring环境使用

```xml
    <bean id="ssf" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource" />
    </bean>

    <bean id="sqlSessionFactory" class="com.mybatis.crud.session.CrudSqlSessionFactory">
        <constructor-arg ref="ssf"></constructor-arg>
    </bean>
```
