package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.util.List;

public interface Executor {

    public <E> List<E> query(MappedStatement mappedStatement,Object... params) throws Exception;

    boolean update(MappedStatement mappedStatement,Object... params)throws Exception;
}
