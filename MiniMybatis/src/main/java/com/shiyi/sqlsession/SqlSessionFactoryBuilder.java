package com.shiyi.sqlsession;

import com.shiyi.config.XMLConfigBuilder;
import com.shiyi.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @ClassName SqlSessionFactoryBuilder
 * @Description
 * @Author FengL
 * @Date 2021/07/02 17:05
 * @Version V1.0
 */
public class SqlSessionFactoryBuilder {


    public SqlSessionFactory  build(InputStream inputStream) throws PropertyVetoException, DocumentException {

        // 第一：使用dom4j解析配置文件，将解析出来的内容封装到Configuration中

        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(inputStream);

        // 第二：创建sqlSessionFactory对象：工厂类：生产sqlSession:会话对象
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }
}
