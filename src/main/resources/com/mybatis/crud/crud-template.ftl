<#-- @ftlvariable name="meta" type="com.mybatis.crud.misc.Meta" -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${namespace}">


<#-- Where 子句宏-->
<#macro where>
    <where>
        <#list meta.columns as column>
            <#if column.select()>
                <if test="${column.fieldName} != null">
                    AND ${column.name} = <@columnValue column></@columnValue>
                </if>
            </#if>
        </#list>
    </where>
</#macro>

<#--列值宏，包括了jdbcType、typeHandler-->
<#macro columnValue column>
    <#compress >
    ${r'#{'}${column.fieldName}<#if column.jdbcType??>,jdbcType=${column.jdbcType.name()}</#if><#if column.typeHandler??>,typeHandler=${column.typeHandler}</#if>${r'}'}
    </#compress>
</#macro>

<#--Order By子句宏 -->
<#macro orderBy> <#compress >
<#if meta.orderBy??>
    ORDER BY ${meta.orderBy}
</#if>
</#compress></#macro>


    <insert id="save">
        INSERT INTO ${meta.tableName} (
        <trim suffixOverrides=",">
        <#list meta.columns as column>
            <#if column.insert()>
                ${column.name} ,
            </#if>
        </#list>
        </trim>
        ) VALUES (
        <trim suffixOverrides=",">
        <#list meta.columns as column>
            <#if column.insert()>
                <#if column.idValue??>
                ${column.idValue} ,
                <#else >
                <if test="${column.fieldName} != null">
                   <@columnValue column></@columnValue> ,
                </if>
                </#if>
            </#if>
        </#list>
        </trim>
        )
    </insert>


    <delete id="delete">
        DELETE FROM ${meta.tableName}
        WHERE
    ${meta.idColumn.name} = <@columnValue meta.idColumn></@columnValue>
    </delete>

    <delete id="deleteByID">
        DELETE FROM ${meta.tableName}
        WHERE
        ${meta.idColumn.name} = ${r'#{0}'}
    </delete>

    <delete id="deleteAll">
        DELETE FROM ${meta.tableName}
    </delete>

    <update id="update">
        UPDATE ${meta.tableName}
        <set>
        <#list meta.columns as column>
            <if test="${column.fieldName} != null">
            ${column.name} = <@columnValue column></@columnValue> ,
            </if>
        </#list>
        </set>
        WHERE
        ${meta.idColumn.name} = <@columnValue meta.idColumn></@columnValue>
    </update>


    <select id="exists" resultType="int">
        SELECT 1 FROM ${meta.tableName}
        <@where></@where>
    </select>


    <select id="count" resultType="int">
        SELECT count(1) FROM ${meta.tableName}
        <@where></@where>
    </select>

    <resultMap id="map" type="${meta.clazz.name}">
    <#list meta.columns as column>
        <result property="${column.fieldName}"
                column="${column.name}"
                <#if column.jdbcType??>jdbcType="${column.jdbcType.name()}"</#if>
                <#if column.typeHandler??>typeHandler="${column.typeHandler}"</#if>
        />
    </#list>
    </resultMap>

    <select id="find" resultMap="map">
        SELECT
        <trim suffixOverrides=",">
        <#list meta.columns as column>
            <#if column.select()>
            ${column.name}  ,
            </#if>
        </#list>
        </trim>
        FROM ${meta.tableName}
        <@where></@where>
        <@orderBy></@orderBy>
    </select>


</mapper>