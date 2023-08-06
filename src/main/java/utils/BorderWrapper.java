package utils;

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
    public void setBorderList(String filepath) throws IOException {
        // 创建文件对象
        File file = new File(filepath);

        // 创建文件输入流
        FileInputStream fis = new FileInputStream(file);

        // 创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        // 获取第一个工作表
        XSSFSheet sheet = workbook.getSheetAt(0);

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
//                    // 检查单元格右侧边框线
//                    if (style.getBorderRightEnum() != BorderStyle.NONE) {
//                        // 输出右侧边框线位置
//                        System.out.printf("Right border: %d,%d%n", row.getRowNum(), cell.getColumnIndex());
//                    }
                }
                borderRowList.add(curBorderCell);
            }
            this.borderList.add(borderRowList);
        }

        // 关闭工作簿和文件输入流
        workbook.close();
        fis.close();
    }

    public boolean isExistUpBorder_cell(int row, int col){
        boolean res = this.borderList.get(row).get(col).isExistUpBorder();
        if (res)
            return true;
        else if (row > 0)
            return this.borderList.get(row-1).get(col).isExistBottom();
        else
            return false;
    }

    public boolean isExistLeftBorder_cell(int row, int col){
        return this.borderList.get(row).get(col).isExistLeftBorder();
    }

    public boolean isExistUpBorder_row(int row){
        boolean res = true;

        for (int i = 0; i < borderList.get(0).size(); i++){
            if (!isExistUpBorder_cell(row, i)){
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


