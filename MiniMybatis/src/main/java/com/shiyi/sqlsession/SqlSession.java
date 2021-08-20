package com.shiyi.sqlsession;

import java.util.List;

public interface SqlSession {

    //查询所有
    <E> List<E> selectList(String statementId, Object... params) throws Exception;

    //查询单个
    <T> T selectOne(String statementId, Object... params) throws Exception;

    //为dao层接口生成代理实现类
    <T> T getMapper(Class<?> mapperClass);

    //更新操作
   Integer update(String statementId, Object... params) throws Exception;

    //删除操作
    Integer delete(String statemenetId, Object... params) throws Exception;

    //添加操作
    Integer insert(String statemenetId, Object... params) throws Exception;

}
