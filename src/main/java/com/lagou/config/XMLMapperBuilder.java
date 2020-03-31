package com.lagou.config;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XMLMapperBuilder {

    /**
     * 需要解析的节点名xPath表达式列表
     */
    private List<String> specifiedNodePathExpressionList = Arrays.asList("//select", "//insert", "//update", "//delete");

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration =configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {

        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();

        String namespace = rootElement.attributeValue("namespace");
        specifiedNodePathExpressionList.forEach(s -> {
            List<Element> list = rootElement.selectNodes(s);
            for (Element element : list) {
                String id = element.attributeValue("id");
                String resultType = element.attributeValue("resultType");
                String paramterType = element.attributeValue("paramterType");
                String sqlText = element.getTextTrim();
                MappedStatement mappedStatement = new MappedStatement();
                mappedStatement.setId(id);
                mappedStatement.setResultType(resultType);
                mappedStatement.setParamterType(paramterType);
                mappedStatement.setSql(sqlText);
                mappedStatement.setExecuteType(s);
                String key = namespace+"."+id;
                configuration.getMappedStatementMap().put(key,mappedStatement);
            }
            list.addAll(rootElement.selectNodes(s));
        });

//        for (Element element : list) {
//            String id = element.attributeValue("id");
//            String resultType = element.attributeValue("resultType");
//            String paramterType = element.attributeValue("paramterType");
//            String sqlText = element.getTextTrim();
//            MappedStatement mappedStatement = new MappedStatement();
//            mappedStatement.setId(id);
//            mappedStatement.setResultType(resultType);
//            mappedStatement.setParamterType(paramterType);
//            mappedStatement.setSql(sqlText);
//            String key = namespace+"."+id;
//            configuration.getMappedStatementMap().put(key,mappedStatement);
//        }

    }


}
