import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reader.BorderWrapper;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileClassifier {
    public  ArrayList<ArrayList<String>> excelDataList;
    public  BorderWrapper borderWrapper;
    public  String filepath;
    public static HashMap<String, String> classNameMap = new HashMap<>();

    static {
        classNameMap.put("default", "factory.DefaultParserFactory");
        classNameMap.put("continuedSheet", "factory.ContinuedSheetParserFactory");
    }

    public String classify() {
        if (continuedSheetFileJudge())
            return classNameMap.get("continuedSheet");
        else if (defaultFileJudge())
            return classNameMap.get("default");
        else
            throw new IllegalArgumentException(filepath + "   未定义类型文件！！！");
    }

    /**
     * 判断标准：有无续表
     */
    public boolean continuedSheetFileJudge(){
        for (ArrayList<String> row : excelDataList) {
            for (String cell : row) {
                if (cell.contains("续表"))
                    return true;
            }
        }
        return false;
    }

    /**
     * 判断标准：
     *  1.表中一共只有三条rowBorderLine
     *  2.lastHeadingsRowIndex 不小于 firstHeadingsRowIndex
     */
    public boolean defaultFileJudge() {
        int firstHeadingsRowIndex = borderWrapper.getFirstHeadingsRowIndex();
        int lastHeadingsRowIndex = borderWrapper.getLastHeadingsRowIndex(firstHeadingsRowIndex);
        //
        if (firstHeadingsRowIndex < 0 || lastHeadingsRowIndex < 0 ||lastHeadingsRowIndex < firstHeadingsRowIndex)
            return false;
        if (borderWrapper.getRowBorderLineNum() != 3)
            return false;
        return true;
    }
}
