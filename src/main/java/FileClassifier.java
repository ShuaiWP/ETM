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
        if (continuedSheetFileJudge())              //续表判断
            return classNameMap.get("continuedSheet");
        else if (defaultFileJudge())
            return classNameMap.get("default");     //default表格
        else
            throw new IllegalArgumentException("error: " + filepath);
//            return "error: " + filepath;
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
     *  1.lastHeadingsRowIndex 不小于 firstHeadingsRowIndex
     *  2.表中一共只有三条rowBorderLine
     *  3.不存在空列
     */
    public boolean defaultFileJudge() {
        int firstHeadingsRowIndex = borderWrapper.getFirstHeadingsRowIndex();
        int lastHeadingsRowIndex = borderWrapper.getLastHeadingsRowIndex(firstHeadingsRowIndex);
        // 1
        if (firstHeadingsRowIndex < 0 || lastHeadingsRowIndex < 0 ||lastHeadingsRowIndex < firstHeadingsRowIndex) {
//            System.out.println("\t *** Illegal row index ***");
            return false;
        }

        // 2
        if (borderWrapper.getRowBorderLineNum() != 3) {
//            System.out.println("\t *** borderLine ！= 3 ***");
            return false;
        }

        // 3
        for (int i = 0; i < excelDataList.get(0).size(); i++){
            boolean flag = true;
            for (ArrayList<String> row : excelDataList) {
                if (!row.get(i).isEmpty()) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
//                System.out.println("\t *** exist null col "+ i + " ***");
                return false;
            }
        }

        return true;
    }
}
