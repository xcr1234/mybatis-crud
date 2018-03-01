package com.mybatis.crud.mapper;

import java.util.List;


/**
 * <pre>
 * 数据库基本的增删改查的接口，所有查询接口（dao接口）都继承该接口。
 * </pre>
 */
public interface CrudMapper<T,ID> {

    /**
     * 将实体对象保存（insert）到数据库中
     * @param t 待保存的对象，不可为null
     * @return 是否保存成功
     */
    boolean save(T t);

    /**
     * 删除存在的java实体（根据id删除）
     * @param t 存在的java实体，不可为null
     * @return 是否删除成功
     */
    boolean delete(T t);

    /**
     * 删除存在的java实体（根据id删除）
     * @param id 存在的java实体的id，不可为null
     * @return 是否删除成功
     */
    boolean deleteByID(ID id);

    /**
     * 删除数据表的全部内容
     * @return 删除成功的数量，失败返回0
     */
    int deleteAll();


    /**
     * 更新（update）一个实体对象
     * @param t 待更新的对象，不可为null
     */
    boolean update(T t);

    /**
     * 判断实体id是否在数据表中
     * @param id 实体对象的id
     * @return id是否在数据表
     */
    boolean exists(ID id);

    /**
     * 计算数据表中的总数
     * @return 总数量
     */
    int count();

    /**
     * 计算数据表中的总数
     * @return 总数量
     */
    int count(T o);

    /**
     * 根据id的值查询实体
     * @param id 实体对象的id
     * @return 返回查询到的实体，如果id不存在则返回null
     */
    T findOne(ID id);

    /**
     * 返回所有的实体List，返回值其实是一个{@link java.util.ArrayList}
     * @return 所有的实体List
     */
    List<T> findAll();


    /**
     * 返回所有的实体List，支持分页查询，参数做为查询条件
     */
    List<T> findAll(T t);


}
