package utils;

import java.util.ArrayList;

public class CommonUtil {
    /**
     * 判断某一行是否为空
     * @param rowData
     * @return
     */
    public static boolean isNullRow(ArrayList<String> rowData){
        for(String s : rowData){
            if (s!=null && !s.equals("")){
                return false;
            }
        }
        return true;
    }
}
