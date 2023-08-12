package factory;

import parser.AbstractParser;
import parser.defaultParser.DefaultParser;
import reader.BorderWrapper;

import java.util.ArrayList;

public class DefaultParserFactory extends AbstractParserFactory{
    @Override
    public AbstractParser getParser(ArrayList<ArrayList<String>> excelDataList, BorderWrapper borderWrapper, String filepath) {
        DefaultParser defaultParser = new DefaultParser();

        defaultParser.setExcelDataList(excelDataList);
        defaultParser.setFilepath(filepath);
        defaultParser.setBorderWrapper(borderWrapper);

        return defaultParser;
    }

}
