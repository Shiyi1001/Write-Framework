package com.shiyi.sqlsession;

import com.shiyi.pojo.Configuration;
import com.shiyi.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

public interface Executor {

    //查询操作
    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws  Exception;

    //增删改操作
    Integer execute(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, Exception;

}
