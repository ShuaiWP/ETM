package com.iscas.etm.excelParser.factory;

import com.iscas.etm.excelParser.parser.continuedSheet.ContinuedSheetParser;
import com.iscas.etm.excelParser.reader.BorderWrapper;
import com.iscas.etm.excelParser.parser.AbstractParser;

import java.util.ArrayList;

public class ContinuedSheetParserFactory extends AbstractParserFactory{
    @Override
    public AbstractParser getParser(ArrayList<ArrayList<String>> excelDataList, BorderWrapper borderWrapper, String filepath) {
        ContinuedSheetParser continuedSheetParser = new ContinuedSheetParser();

        continuedSheetParser.setExcelDataList(excelDataList);
        continuedSheetParser.setBorderWrapper(borderWrapper);
        continuedSheetParser.setFilepath(filepath);

        return continuedSheetParser;
    }
}
