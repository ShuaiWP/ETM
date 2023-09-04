package com.iscas.etm.excelParser.factory;

import com.iscas.etm.excelParser.parser.defaultParser.DefaultParser;
import com.iscas.etm.excelParser.parser.AbstractParser;
import com.iscas.etm.excelParser.reader.BorderWrapper;

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
