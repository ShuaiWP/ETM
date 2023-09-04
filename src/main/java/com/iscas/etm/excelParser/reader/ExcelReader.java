package com.iscas.etm.excelParser.reader;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ExcelReader {

    /**
     * 读取Excel表格数据
     * @param fileName  excel文件路径
     * @return ArrayList<ArrayList<String>>形式的数据
     */
    public static ArrayList<ArrayList<String>> read(String fileName, int sheetIndex) {
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

        try {
            FileInputStream file = new FileInputStream(new File(fileName));
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(sheetIndex); // 获取第n个工作表

            // 获取所有的合并单元格区域
            int mergedRegions = sheet.getNumMergedRegions();
            CellRangeAddress mergedRegion;
            int mergedRegionRowStart, mergedRegionRowEnd, mergedRegionColStart, mergedRegionColEnd;

            for (Row row : sheet){
                ArrayList<String> rowData = new ArrayList<String>();
                for (Cell cell : row) {
                    // 获取当前单元格所在的合并单元格区域的行数和列数
                    mergedRegionRowStart = mergedRegionRowEnd = cell.getRowIndex();
                    mergedRegionColStart = mergedRegionColEnd = cell.getColumnIndex();
                    for (int i = 0; i < mergedRegions; i++) {
                        mergedRegion = sheet.getMergedRegion(i);
                        if (mergedRegion.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
                            mergedRegionRowStart = mergedRegion.getFirstRow();
                            mergedRegionRowEnd = mergedRegion.getLastRow();
                            mergedRegionColStart = mergedRegion.getFirstColumn();
                            mergedRegionColEnd = mergedRegion.getLastColumn();
                            break;
                        }
                    }

                    // 根据合并单元格区域的行数和列数读取数据
                    String cellValue = "";
                    for (int i = mergedRegionRowStart; i <= mergedRegionRowEnd; i++) {
                        Row mergedRow = sheet.getRow(i);
                        if (mergedRow != null) {
                            for (int j = mergedRegionColStart; j <= mergedRegionColEnd; j++) {
                                Cell mergedCell = mergedRow.getCell(j);
                                if (mergedCell != null) {
                                    switch (mergedCell.getCellType()) { // 根据单元格类型读取数据
                                        case STRING:
                                            cellValue += mergedCell.getStringCellValue() + " ";
                                            break;
                                        case NUMERIC:
                                            cellValue += String.valueOf(mergedCell.getNumericCellValue()) + " ";
                                            break;
                                        case BOOLEAN:
                                            cellValue += String.valueOf(mergedCell.getBooleanCellValue()) + " ";
                                            break;
                                        case BLANK:
                                            cellValue += " ";
                                            break;
                                        default:
                                            cellValue += " ";
                                            break;
                                    }
                                }
                            }
                        }
                    }
                    if (cell.getColumnIndex() != 0)
                        cellValue = cellValue.trim();
                    rowData.add(cellValue.replaceAll("[\\n\\r]", " "));
                }
                if (rowData.size() > 0)
                    data.add(rowData);
            }

            workbook.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
