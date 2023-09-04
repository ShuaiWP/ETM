package com.iscas.etm.excelParser.factory;

import com.iscas.etm.excelParser.reader.BorderWrapper;
import com.iscas.etm.excelParser.parser.AbstractParser;

import java.util.ArrayList;

public abstract class AbstractParserFactory {
    public static AbstractParserFactory getParserFactory(String factoryClass){
        AbstractParserFactory factory = null;

        try {
            factory = (AbstractParserFactory) Class.forName(factoryClass).newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("没有找到" + factoryClass + "类！！！");
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return factory;
    }

    public abstract AbstractParser getParser(ArrayList<ArrayList<String>> excelDataList, BorderWrapper borderWrapper, String filepath);

}
