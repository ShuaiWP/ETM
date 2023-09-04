package com.iscas.etm.excelParser.reader;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BorderWrapper {
    private ArrayList<ArrayList<BorderCell>> borderList = new ArrayList<>();

    /**
     * 设置边界线列表信息
     * @param filepath 表格文件路径
     */
    public void setBorderList(String filepath, int sheetIndex) throws IOException {
        // 创建文件对象
        File file = new File(filepath);

        // 创建文件输入流
        FileInputStream fis = new FileInputStream(file);

        // 创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        // 获取第一个工作表
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);

        // 遍历每一行
        for (Row row : sheet) {
            ArrayList<BorderCell> borderRowList = new ArrayList<>();
            // 遍历每个单元格
            for (Cell cell : row) {
                BorderCell curBorderCell = new BorderCell();
                // 获取单元格样式
                XSSFCellStyle style = (XSSFCellStyle) cell.getCellStyle();
                if (style != null) {
                    // 检查单元格顶部边框线
                    if (style.getBorderTopEnum() != BorderStyle.NONE) {
                        curBorderCell.setUp(true);
                    }
                    // 检查单元格底部边框线
                    if (style.getBorderBottomEnum() != BorderStyle.NONE) {
                        curBorderCell.setBottom(true);
                    }

                    // 检查单元格左侧边框线
                    if (style.getBorderLeftEnum() != BorderStyle.NONE) {
                        curBorderCell.setLeft(true);
                    }
                    // 检查单元格右侧边框线
                    if (style.getBorderRightEnum() != BorderStyle.NONE) {
                        curBorderCell.setRight(true);
                    }
                }
                borderRowList.add(curBorderCell);
            }
            if (borderRowList.size() > 0)
                this.borderList.add(borderRowList);
        }

        // 关闭工作簿和文件输入流
        workbook.close();
        fis.close();
    }

    public int getFirstHeadingsRowIndex(){
        //逐行遍历，去寻找firstHeadingsRowIndex
        for (int i = 0; i < borderList.size(); i++){
            if (isExistUpBorder_row(i)){
                return i;
            }
        }
        return -1;
    }

    public int getLastHeadingsRowIndex(int firstHeadingsRowIndex){
        //从最后一行往上遍历，去寻找倒数第二条borderRow,作为lastHeadingsRowIndex
        boolean flag = false;   //用于判断是否已经遍历过最后一根borderRow，去寻找倒数第二根
        for (int i = borderList.size() - 1; i > firstHeadingsRowIndex && i > 0; i--){
            if (isExistUpBorder_row(i) || isExistBottomBorder_row(i-1)){
                if (flag) {
                    return i - 1;
                }
                else
                    flag = true;
            }else if (i == getBorderList().size()-1 && !flag){       //考虑到有些表格没有附加信息，最后一根边界在最后一行下方
                if (isExistBottomBorder_row(i))
                    flag = true;
            }
        }
        return  -1;
    }

    public int getRowBorderLineNum(){
        int num = 0;
        for (int i = 0; i < borderList.size(); i++){
            if (i == borderList.size() -1){
                if (isExistBottomBorder_row(i))
                    num++;
            }else {
                if (isExistBottomBorder_row(i) || isExistUpBorder_row(i+1))
                    num++;
            }
        }
        return num;
    }

    public boolean isExistUpBorder_cell(int row, int col){
        boolean res = this.borderList.get(row).get(col).isExistUpBorder();
        if (res)
            return true;
        else if (row > 0 && col < borderList.get(row-1).size())
            return this.borderList.get(row-1).get(col).isExistBottom();
        else
            return false;
    }

    public boolean isExistBottomBorder_cell(int row, int col){
        return this.borderList.get(row).get(col).isExistBottom();
    }

    public boolean isExistLeftBorder_cell(int row, int col){
        boolean res = this.borderList.get(row).get(col).isExistLeftBorder();
        if (res)
            return true;
        else if (col > 0)
            return this.borderList.get(row).get(col-1).isExistRight();
        else
            return false;
    }

    public boolean isExistUpBorder_row(int row){
        boolean res = true;

        for (int i = 0; i < borderList.get(row).size(); i++){
            if (!isExistUpBorder_cell(row, i)){
                res =false;
                break;
            }
        }
        return res;
    }

    public boolean isExistBottomBorder_row(int row){
        boolean res = true;

        for (int i = 0; i < borderList.get(row).size(); i++){
            if (!this.borderList.get(row).get(i).isExistBottom()){
                res =false;
                break;
            }
        }
        return res;
    }

    public ArrayList<ArrayList<BorderCell>> getBorderList() {
        return borderList;
    }
}


