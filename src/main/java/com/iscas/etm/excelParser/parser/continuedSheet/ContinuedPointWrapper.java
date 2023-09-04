package com.iscas.etm.excelParser.parser.continuedSheet;

import com.iscas.etm.excelParser.reader.BorderWrapper;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ContinuedPointWrapper {

    private ArrayList<ContinuedPoint> continuedPointList = new ArrayList<>();

    /**
     * 收集所有的ContinuedPoint，并获取到对应子表的长宽
     */
    public void wrapContinuedPoint(ArrayList<ArrayList<String>> excelDataList, BorderWrapper borderWrapper) {
        //1. 收集续点
        collectAllPoint(excelDataList);

        //2. 为每个续点确定长宽大小
        //  2.1 找到与当前续点相邻的续点
        for (ContinuedPoint point : continuedPointList) {
            //down
            for (int i = point.getRow()+1; i < excelDataList.size(); i++) {
                for (ContinuedPoint otherPoint : continuedPointList) {
                    if (point.getDown() == null && otherPoint != point && otherPoint.getRow() == i) {
                        point.setDown(otherPoint.clone());
                    }
                }
            }

            //right
            for (int j = point.getCol()+1; j < excelDataList.get(point.getRow()).size(); j++) {
                for (ContinuedPoint otherPoint : continuedPointList) {
                    if (point.getRight() == null && otherPoint != point && otherPoint.getCol() == j)
                        point.setRight(otherPoint.clone());
                }
            }
        }

        //  2.2 找到与续点相邻的边界线的位置
        for (ContinuedPoint point : continuedPointList) {
            //rowLength
            if (point.getRight() != null) {
                ContinuedPoint rightPoint = point.getRight();
                point.setRowLength(rightPoint.getCol() - point.getCol() - 1);
            }else{
                point.setRowLength(excelDataList.get(point.getRow()).size() - point.getCol()-1);
            }

            //colLength
            if (point.getDown() != null) {
                ContinuedPoint downPoint = point.getDown();
                point.setColLength(downPoint.getRow() - point.getRow() - 1);
            }else {
                point.setColLength(excelDataList.size() - point.getRow()-1);
            }
        }
    }

    /**
     * 搜索全部续表顶点
     */
    public void collectAllPoint(ArrayList<ArrayList<String>> excelDataList) {
        //添加原点
        continuedPointList.add(new ContinuedPoint(0, 0));

        // 逐个单元格遍历，寻找续表所在点
        for (int row = 0; row < excelDataList.size(); row++) {
            for (int col = 0; col < excelDataList.get(row).size(); col++) {
                if (excelDataList.get(row).get(col).contains("续表")) {
                    continuedPointList.add(new ContinuedPoint(row, col));
                }
            }
        }
    }
}
