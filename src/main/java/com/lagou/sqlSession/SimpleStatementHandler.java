package com.lagou.sqlSession;

import com.lagou.config.BoundSql;
import com.lagou.pojo.MappedStatement;
import com.lagou.utils.ParameterMapping;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by wuhui
 * Date: 2020/3/31
 */
public class SimpleStatementHandler implements StatementHandler {
    private Executor executor;

    private MappedStatement mappedStatement;

    private Object[] params;

    private BoundSql boundSql;

    public SimpleStatementHandler(Executor executor, BoundSql boundSql, MappedStatement mappedStatement, Object[] params) {
        this.executor = executor;
        this.boundSql = boundSql;
        this.mappedStatement = mappedStatement;
        this.params = params;
    }

//    @Override
    public void prepareParams(PreparedStatement statement) throws Exception {
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();
            Class<?> clazz = getClassType(mappedStatement.getParamterType());
            if (isBasicClass(clazz)) {
                statement.setObject(i + 1, params[0]);
            } else {
                //反射
                Field declaredField = getClassType(mappedStatement.getParamterType()).getDeclaredField(content);
                //暴力访问
                declaredField.setAccessible(true);
                Object o = declaredField.get(params[0]);

                statement.setObject(i + 1, o);
            }
        }
    }

    private boolean isBasicClass(Class<?> clazz){
        return  String.class == clazz ||
                boolean.class == clazz ||
                char.class == clazz ||
                Character.class == clazz ||
                Boolean.class == clazz ||
                byte.class == clazz ||
                Byte.class == clazz ||
                short.class == clazz ||
                Short.class == clazz ||
                int.class == clazz ||
                Integer.class == clazz ||
                float.class == clazz ||
                Float.class == clazz ||
                double.class == clazz ||
                Double.class == clazz ||
                long.class == clazz ||
                Long.class == clazz ||
                void.class == clazz ||
                Void.class == clazz;
    }

    private Class<?> getClassType(String paramterType) throws ClassNotFoundException {
        if(paramterType!=null){
            Class<?> aClass = Class.forName(paramterType);
            return aClass;
        }
        return null;

    }

    @Override
    public boolean update(PreparedStatement statement) throws Exception {
        prepareParams(statement);
        return statement.execute();
    }
}
