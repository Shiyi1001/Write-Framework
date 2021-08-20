package com.shiyi.sqlsession;

import com.shiyi.pojo.Configuration;
import com.shiyi.pojo.MappedStatement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @ClassName DefaultSqlSession
 * @Description
 * @Author FengL
 * @Date 2021/07/14 9:20
 * @Version V1.0
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {

        SimpleExecutor simpleExecutor = new SimpleExecutor();
        List<Object> list = simpleExecutor.query(configuration, configuration.getMappedStatementMap().get(statementId), params);
        return (List<E>) list;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = selectList(statementId, params);
        if (objects.size() == 1) {
            return (T) objects.get(0);
        } else {
            throw new RuntimeException("查询结果为空或返回结果过多");
        }
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {

        //jdk动态代理生成实现类
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {

            /**
             *
             * @Param proxy 当前代理对象的应用
             * @Param method    当前被调用方法的引用
             * @Param args  传递的参数
             * @Return java.lang.Object
             * @Date 2021/07/14 16:02
             * @Author FengL
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //底层调用的还是JDBC代码  根据不同情况来调用selectList 还是 selectOne
                //statementid： SQL语句的唯一标识  namespace.id = 接口全限定名.方法名
                String className = method.getDeclaringClass().getName();
                String methodName = method.getName();

                String statementId = className + "." + methodName;

                MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
                String sqlType = mappedStatement.getSqlType();
                if("SELECT".equals(sqlType)){
                    //方法的返回值类型
                    Type genericReturnType = method.getGenericReturnType();
                    //判断是都进行了泛型类型参数化
                    if(genericReturnType instanceof ParameterizedType){
                        return selectList(statementId, args);
                    }
                    return selectOne(statementId,args);
                }
                //执行更新操作
                return update(statementId,args);

            }
        });
        return (T) proxyInstance;
    }

    @Override
    public Integer update(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        return simpleExecutor.execute(configuration,configuration.getMappedStatementMap().get(statementId),params);
    }

    @Override
    public Integer delete(String statemenetId, Object... params) throws Exception{
        return update(statemenetId,params);
    }

    @Override
    public Integer insert(String statemenetId, Object... params) throws Exception{
        return update(statemenetId,params);
    }
}
