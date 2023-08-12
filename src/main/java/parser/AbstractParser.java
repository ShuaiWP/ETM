package parser;

import lombok.Data;
import org.bson.Document;
import reader.BorderWrapper;


import java.util.ArrayList;

@Data
public abstract class AbstractParser {
    public ArrayList<ArrayList<String>> excelDataList;
    public BorderWrapper borderWrapper;
    public String filepath;

    public abstract Document getDocument();
}
