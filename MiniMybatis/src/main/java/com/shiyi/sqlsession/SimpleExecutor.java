package com.shiyi.sqlsession;

import com.shiyi.pojo.BoundSql;
import com.shiyi.pojo.Configuration;
import com.shiyi.pojo.MappedStatement;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName SimpleExecutor
 * @Description
 * @Author FengL
 * @Date 2021/07/14 9:29
 * @Version V1.0
 */
public class SimpleExecutor implements Executor{
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {

        PreparedStatement preparedStatement = getPreparedStatement(configuration, mappedStatement, params);

        //5、执行SQL
        ResultSet resultSet = preparedStatement.executeQuery();

        //6、封装结果集
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);

        List<Object> objectList = new ArrayList<>();
        while(resultSet.next()){
            Object o = resultTypeClass.newInstance();
            //获取元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //字段名
                String columnName = metaData.getColumnName(i);
                //字段值
                Object value = resultSet.getObject(columnName);

                //使用反射、内省 根据数据库表和实体的关系 完成封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o,value);
            }
            objectList.add(o);
        }
        return (List<E>) objectList;
    }

    /**
     *  获取 PreparedStatement 对象
     * @Param configuration
     * @Param mappedStatement
     * @Param param
     * @Return java.sql.PreparedStatement
     * @Date 2021/07/14 16:44
     * @Author FengL
     */
    private PreparedStatement getPreparedStatement(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, IllegalAccessException {
        //1、获取连接
        Connection connection = configuration.getDataSource().getConnection();

        //2、获取SQL语句
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);

        //3、获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        //4、设置参数  利用反射来设置参数
        //获取参数的全路径
        String paramterType = mappedStatement.getParamterType();

        Class<?> paramterTypeClass = getClassType(paramterType);

        List<String> paramsList = boundSql.getParamsList();
        for (int i = 0; i < paramsList.size(); i++) {
            //传入的参数
            Field declaredFieldIngoreCase = getDeclaredFieldIngoreCase(paramterTypeClass, paramsList.get(i));
            //设置暴力访问
            declaredFieldIngoreCase.setAccessible(true);
            //参数值
            Object value = declaredFieldIngoreCase.get(params[0]);
            //给占位符? 赋值
            preparedStatement.setObject(i + 1, value);
        }
        return preparedStatement;
    }

    @Override
    public Integer execute(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {

        PreparedStatement preparedStatement = getPreparedStatement(configuration, mappedStatement, params[0]);

        int result = preparedStatement.executeUpdate();

        return  result;
    }

    /**
     * 根据字段名返回匹配的属性  忽略大小写
     * @Param aClass
 * @Param fieldName
     * @Return java.lang.reflect.Field
     * @Date 2021/07/14 11:07
     * @Author FengL
     */
    private Field getDeclaredFieldIngoreCase(Class<?> aClass,String fieldName) {
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if(declaredField.getName().equalsIgnoreCase(fieldName)){
                return declaredField;
            }
        }
        throw new RuntimeException("没有找到该声明属性" + fieldName);
    }

    /**
     * 根据类的全路径 获取类
     * @Param paramterType
     * @Return java.lang.Class<?> 
     * @Date 2021/07/14 10:34
     * @Author FengL
     */
    public Class<?> getClassType(String paramterType) throws ClassNotFoundException {
        if(paramterType != null){
            Class<?> aClass = Class.forName(paramterType);
            return aClass;
        }
        return  null;
    }

    /**
     *
     *j解析SQL 用?替换#{}  并将参数名称转换成小写
     * @Return com.shiyi.pojo.BoundSql
     * @Date 2021/07/14 9:43
     * @Author FengL
     */
    public BoundSql getBoundSql(String sql) {
        //匹配#{}
        String regex = "\\#\\{(.+?)}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sql);

        List<String> paramsList = new ArrayList<>();
       while(matcher.find()){
           String matcherParam = matcher.group();
           matcherParam = matcherParam.substring(2, matcherParam.length() - 1);
           paramsList.add(matcherParam);
       }
        String sqlText = sql.replaceAll(regex, "?");
        BoundSql boundSql = BoundSql.builder().sqlText(sqlText).paramsList(paramsList).build();
        return  boundSql;
    }
}
