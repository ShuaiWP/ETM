package factory;

import parser.AbstractParser;
import reader.BorderWrapper;

import java.util.ArrayList;

public abstract class AbstractParserFactory {
    public static AbstractParserFactory getParserFactory(String factoryClass){
        AbstractParserFactory factory = null;

        try {
            factory = (AbstractParserFactory) Class.forName(factoryClass).newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println("没有找到该类！！！");
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return factory;
    }

    public abstract AbstractParser getParser(ArrayList<ArrayList<String>> excelDataList, BorderWrapper borderWrapper, String filepath);

}
