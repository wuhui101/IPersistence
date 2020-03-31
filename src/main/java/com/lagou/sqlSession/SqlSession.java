package com.lagou.sqlSession;

import java.util.List;
import java.util.Objects;

public interface SqlSession {

    //查询所有
    public <E> List<E> selectList(String statementid,Object... params) throws Exception;

    //根据条件查询单个
    public <T> T selectOne(String statementid,Object... params) throws Exception;

    Boolean delete(String statementId, Object... params)throws Exception;

    Boolean insert(String statementId, Object... params)throws Exception;

    Boolean update(String statementId, Object... params)throws Exception;

    //为Dao接口生成代理实现类
    public <T> T getMapper(Class<?> mapperClass);


}
