package com.lagou.sqlSession;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by wuhui
 * Date: 2020/3/31
 */
public interface StatementHandler {

//    void parameterize()throws Exception;
    boolean update(PreparedStatement statement) throws Exception;

}
