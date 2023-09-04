package com.iscas.etm.excelParser.parser;

import com.iscas.etm.excelParser.reader.BorderWrapper;
import lombok.Data;
import org.bson.Document;


import java.util.ArrayList;

@Data
public abstract class AbstractParser {
    public ArrayList<ArrayList<String>> excelDataList;
    public BorderWrapper borderWrapper;
    public String filepath;

    public abstract Document getDocument();
}
