package com.shiyi.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.shiyi.io.Resources;
import com.shiyi.pojo.Configuration;
import com.shiyi.pojo.MappedStatement;
import com.shiyi.utils.Constants;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName XMLConfigBuilder
 * @Description
 * @Author FengL
 * @Date 2021/07/02 17:10
 * @Version V1.0
 */
public class XMLConfigBuilder {

    private Configuration configuration;

    public XMLConfigBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * 该方法就是使用dom4j对配置文件进行解析，封装Configuration
     */
    public Configuration parseConfig(InputStream inputStream) throws DocumentException, PropertyVetoException {

        //dom4j读取配置文件
        Document read = new SAXReader().read(inputStream);
        //获取根目录 <configuration>
        Element rootElement = read.getRootElement();

        List<Element> list = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element element : list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name, value);
        }
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));

        configuration.setDataSource(comboPooledDataSource);

        //获取mapper
        List<Element> mapperList = rootElement.selectNodes("//mapper");

        for (Element element : mapperList) {
            //获取mapper的存放全路径
            String resource = element.attributeValue("resource");

            InputStream in = Resources.getResourceAsSteam(resource);
            //解析mapper.xml  并封装到configuration
            parseXMLMapperConfig(in);

        }
        return configuration;
    }

    /**
     * 解析maper.xml
     * @Param resource
     * @Return void
     * @Date 2021/07/13 16:29
     * @Author FengL
     */
    private void parseXMLMapperConfig(InputStream in) throws DocumentException {


        Document read = new SAXReader().read(in);

        Element rootElement = read.getRootElement();

        String namespace = rootElement.attributeValue("namespace");

        List<Element> selectList = rootElement.selectNodes("//select");
        forEachElement(selectList,namespace, Constants.SELECT);

        List<Element> updateList = rootElement.selectNodes("//update");
        forEachElement(updateList,namespace,Constants.UPDATE);

        List<Element> deleteList = rootElement.selectNodes("//delete");
        forEachElement(deleteList,namespace,Constants.DELETE);

        List<Element> insertList = rootElement.selectNodes("//insert");
        forEachElement(insertList,namespace,Constants.INSERT);

    }


    /**
     * 循环遍历list  封装成mappedStatement
     * @Param list
     * @Param namespace
     * @Return void
     * @Date 2021/07/14 16:28
     * @Author FengL
     */
    private void forEachElement(List<Element> list,String namespace,String sqlType){
        for (Element element : list) {
            String id = element.attributeValue("id");
            String paramterType = element.attributeValue("paramterType");
            String resultType = element.attributeValue("resultType");
            String sql = element.getTextTrim();
            MappedStatement mappedStatement = MappedStatement.builder().id(id).paramterType(paramterType).resultType(resultType).sql(sql).sqlType(sqlType).build();
            String key = namespace + "." + id;
            configuration.getMappedStatementMap().put(key,mappedStatement);
        }
    }

}
