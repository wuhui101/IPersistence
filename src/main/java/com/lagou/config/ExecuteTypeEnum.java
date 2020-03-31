package com.lagou.config;

import com.mysql.jdbc.StringUtils;

/**
 * Created by wuhui
 * Date: 2020/3/31
 */
public enum ExecuteTypeEnum {


    SELECT("select"){
        @Override
        public String getNodePathExpression() {
            return "//select";
        }
    },
    INSERT("insert"){
        @Override
        public String getNodePathExpression() {
            return "//insert";
        }
    },
    UPDATE("update"){
        @Override
        public String getNodePathExpression() {
            return "//update";
        }
    },
    DELETE("delete"){
        @Override
        public String getNodePathExpression() {
            return "//delete";
        }
    },;

    private String code;
    ExecuteTypeEnum(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
    public abstract String getNodePathExpression();

    public static ExecuteTypeEnum findByCode(String code){
        if(StringUtils.isNullOrEmpty(code)){
            throw new RuntimeException("未找到对应code");
        }
        for(ExecuteTypeEnum executeTypeEnum: values()){
            if(code.equals(executeTypeEnum.getCode())){
                return executeTypeEnum;
            }
        }
        throw new RuntimeException("未找到对应code");
    }
}
