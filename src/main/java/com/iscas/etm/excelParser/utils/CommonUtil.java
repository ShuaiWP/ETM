package com.iscas.etm.excelParser.utils;


import com.iscas.etm.excelParser.reader.BorderCell;
import com.iscas.etm.excelParser.reader.BorderWrapper;

import java.io.*;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    public static void searchXLSXFiles(String folderPath, ArrayList<String> xlsxFiles) {
        File folder = new File(folderPath);

        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".xlsx")) {
                        xlsxFiles.add(file.getAbsolutePath());
                    } else if (file.isDirectory()) {
                        searchXLSXFiles(file.getAbsolutePath(), xlsxFiles); // 递归搜索子文件夹
                    }
                }
            }
        } else if (folder.isFile()) {
            xlsxFiles.add(folderPath);
        }
    }


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
            if (res.toString().isEmpty())
                res.append(matcher.group(1));
            else
                res.append("-").append(matcher.group(1));
        }
        return res.toString();
    }

    public static boolean isNullStr(String s){
        return s == null || s.equals("");
    }


    public static ArrayList<ArrayList<String>> cutExcelDataList(ArrayList<ArrayList<String>> excelDataList, int row,
                                                                int col, int rowLen, int colLen){
        ArrayList<ArrayList<String>> sonList = new ArrayList<>();

        for (int i = row; i <= Math.min(row + rowLen, excelDataList.size()-1); i++){
            ArrayList<String> rowList = new ArrayList<>();
            for (int j = col; j <= Math.min(col + colLen, excelDataList.get(i).size()-1); j++){
                if (excelDataList.get(i).get(j).contains("续表"))
                    rowList.add("");
                else
                    rowList.add(excelDataList.get(i).get(j));
            }
            if (!rowList.isEmpty())
                sonList.add(rowList);
        }
        return sonList;
    }

    public static BorderWrapper cutBorderWrapper(BorderWrapper borderWrapper, int row,
                                                 int col, int rowLen, int colLen){
        BorderWrapper sonBorderWrapper = new BorderWrapper();

        for (int i = row; i <= Math.min(row + rowLen, borderWrapper.getBorderList().size()-1); i++){
            ArrayList<BorderCell> rowList = new ArrayList<>();
            for (int j = col; j <= Math.min(col + colLen, borderWrapper.getBorderList().get(i).size()-1); j++){
                rowList.add(borderWrapper.getBorderList().get(i).get(j).clone());
            }
            if (!rowList.isEmpty())
                sonBorderWrapper.getBorderList().add(rowList);
        }

        return sonBorderWrapper;
    }
}
