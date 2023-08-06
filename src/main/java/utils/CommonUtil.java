package utils;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    /**
     * 判断某一行是否为空
     * @param rowData exceldata中的一行数据
     * @return 是否为空
     */
    public static boolean isNullRow(ArrayList<String> rowData){
        for(String s : rowData){
            if (s!=null && !s.trim().equals("")){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断某一行是否只有标题，没有数据, 或者都为空
     * @param rowData exceldata中的一行数据
     * @return 是否满足
     */
    public static boolean isNullDataRow(ArrayList<String> rowData){
        if (isNullRow(rowData))
            return true;

        if (rowData.get(0) != null && !rowData.get(0).trim().equals("")){
            for (int i = 1; i < rowData.size(); i++){
                String s = rowData.get(i);
                if (s!=null && !s.equals("")){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 计算一个字符串开头空格的数量
     */
    public static int countBlack(String s){
        int res = 0;
        if(s != null){
            for (int i = 0; i < s.length(); i++){
                if (s.charAt(i)  == ' ')
                    res++;
                else
                    break;
            }
        }
        return res;
    }

    /**
     * 去除列标题中的空格和（）内容
     */
    public static String formatColHeading(String s){
        return s.replaceAll("\\s+","").replaceAll("\\([^()]*\\)", "");
    }

    /**
     * 获取列标题中的单位
     */
    public static String getColHeadingUnit(String s){
        StringBuilder res = new StringBuilder();
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            if (res.toString().equals(""))
                res.append(matcher.group(1));
            else
                res.append("-").append(matcher.group(1));
        }
        return res.toString();
    }

    public static boolean isNullStr(String s){
        return s == null || s.equals("");
    }
}
