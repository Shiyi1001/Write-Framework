package com.shiyi.sqlsession;

import com.shiyi.pojo.Configuration;

/**
 * @ClassName DefaultSqlSessionFactory
 * @Description
 * @Author FengL
 * @Date 2021/07/13 17:22
 * @Version V1.0
 */
public class DefaultSqlSessionFactory  implements  SqlSessionFactory{

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
