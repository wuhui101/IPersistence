package com.lagou.sqlSession;

import com.lagou.config.ExecuteTypeEnum;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    //可以将executor
    private Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        executor = this.configuration.createExecutor();
    }

    @Override
    public <E> List<E> selectList(String statementid, Object... params) throws Exception {

        //将要去完成对simpleExecutor里的query方法的调用
//        SimpleExecutor simpleExecutor = new SimpleExecutor(configuration);
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementid);
        List<Object> list = executor.query(mappedStatement, params);

        return (List<E>) list;
    }




    @Override
    public <T> T selectOne(String statementid, Object... params) throws Exception {
        List<Object> objects = selectList(statementid, params);
        if(objects.size()==1){
            return (T) objects.get(0);
        }else {
            throw new RuntimeException("查询结果为空或者返回结果过多");
        }

    }

    @Override
    public Boolean delete(String statementId, Object... params)throws Exception{
       return update(statementId, params);
    }

    @Override
    public Boolean update(String statementId,Object...params)throws Exception{
        SimpleExecutor simpleExecutor = new SimpleExecutor(configuration);
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return simpleExecutor.update(mappedStatement, params);
    }

    @Override
    public Boolean insert(String statementId,Object...params)throws Exception{
       return update(statementId, params);
    }



    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        // 使用JDK动态代理来为Dao接口生成代理对象，并返回

        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 底层都还是去执行JDBC代码 //根据不同情况，来调用selctList或者selectOne
                // 准备参数 1：statmentid :sql语句的唯一标识：namespace.id= 接口全限定名.方法名
                // 方法名：findAll
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();

                String statementId = className+"."+methodName;
                String targetMethodType = getTargetMethodType(statementId);
                ExecuteTypeEnum executeTypeEnum = ExecuteTypeEnum.findByCode(targetMethodType);
                switch (executeTypeEnum){
                    case DELETE:
                          return delete(statementId, args);
                    case INSERT:
                        return insert(statementId, args);
                    case UPDATE:
                        return update(statementId, args);
                    case SELECT:{
                        // 准备参数2：params:args
                        // 获取被调用方法的返回值类型
                        Type genericReturnType = method.getGenericReturnType();
                        // 判断是否进行了 泛型类型参数化
                        if(genericReturnType instanceof ParameterizedType){
                            List<Object> objects = selectList(statementId, args);
                            return objects;
                        }

                        return selectOne(statementId,args);
                    }
                    default:
                        throw new RuntimeException("未找到对应处理方法");
                }

            }
        });

        return (T) proxyInstance;
    }

    private String getTargetMethodType(String statementId) throws Exception{
        MappedStatement mappedStatement = this.configuration.getMappedStatementMap().get(statementId);
        if(mappedStatement == null){
            throw new RuntimeException("No such method!");
        }
        return mappedStatement.getExecuteType().replaceAll("//", "");
    }


}
