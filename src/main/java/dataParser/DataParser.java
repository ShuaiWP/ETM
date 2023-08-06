package dataParser;

import utils.BorderCell;
import utils.BorderWrapper;
import utils.ColHeadingWrapper;
import utils.CommonUtil;

import java.util.ArrayList;

public class DataParser {
    private ArrayList<ArrayList<String>> excelDataList = null;
    private int lastHeadingsRowIndex = 0;       //最后一级行标题的index
    private int firstHeadingsRowIndex = 0;       //表格第一行行标题所在行
    private int firstDataRowIndex = 0;  //第一行
    private String totalUnit = "";  //全表的总体单位
    private ArrayList<String> rowHeadingsList = new ArrayList<>();
    private ArrayList<ColHeadingWrapper> colHeadingsList = new ArrayList<>();
    private BorderWrapper borderWrapper;

    /**
     * DataParser之一，适配于多层级行标题，但是需要顶层行标题是合并单元格的
     * 解析方法：从最下层行标题往上合并，中间用“-”隔开
     */
    public void parse(){
        //todo 获取基本参数
        getBasicParam();

        //todo 合并第一行标题和第一列标题
        mergeRowHeadings();
        mergeColHeadings();

    }

    /**
     * 获取基本信息，包括lastHeadingsRowIndex，firstDataRowIndex，firstHeadingsRowIndex，totalUnit
     */
    public void getBasicParam(){
        //逐行遍历，去寻找firstHeadingsRowIndex
        for (int i = 0; i < borderWrapper.getBorderList().size(); i++){
            if (borderWrapper.isExistUpBorder_row(i)){
                this.firstHeadingsRowIndex = i;
                break;
            }
        }

        //从最后一行往上遍历，去寻找倒数第二条borderRow,作为lastHeadingsRowIndex
        boolean flag = false;   //用于判断是否已经遍历过最后一根borderRow，去寻找倒数第二根
        for (int i = borderWrapper.getBorderList().size() - 1; i > firstHeadingsRowIndex; i--){
            if (borderWrapper.isExistUpBorder_row(i)){
                if (flag) {
                    this.lastHeadingsRowIndex = i - 1;
                    break;
                }
                else
                    flag = true;
            }
        }

        //根据lastHeadingsRowIndex去寻找firstDataRowIndex
        for (int i = lastHeadingsRowIndex + 1; i < borderWrapper.getBorderList().size(); i++) {
            if (!CommonUtil.isNullRow(excelDataList.get(i))){
                this.firstDataRowIndex = i;
                break;
            }
        }

        //寻找TotalUnit
        for (int i = 0; i < firstHeadingsRowIndex; i++){
            ArrayList<String> curRow = excelDataList.get(i);
            if (!CommonUtil.isNullRow(curRow)){
                for (String curCell : curRow) {
                    if (curCell.contains("单位")) {
                        totalUnit = curCell.substring(curCell.indexOf("：") + 1);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 合并第一行标题列表
     */
    public void mergeRowHeadings(){
        //合并rowHeadings
        ArrayList<String> lastHeadingsRow = excelDataList.get(lastHeadingsRowIndex);
//        rowHeadingsList = new ArrayList<>(lastHeadingsRow);
        for (String s : lastHeadingsRow)
            rowHeadingsList.add(s);

        rowHeadingsList.set(0, lastHeadingsRow.get(0));  //设置第一列的行标题
        //找到之后每一列的headings
        String lastSonHeading = "";
        for(int i = 1; i < lastHeadingsRow.size(); i++){
            String curHeadings = lastHeadingsRow.get(i);
            lastSonHeading = curHeadings;
            int upRow = lastHeadingsRowIndex - 1;
            while(upRow >= firstHeadingsRowIndex){
                if (i < excelDataList.get(upRow).size()) {
                    String curSonHeading = excelDataList.get(upRow).get(i);
                    if (!curSonHeading.equals("") && !curHeadings.contains(curSonHeading)){
                        lastSonHeading = curSonHeading;
                        if (!curHeadings.equals("")) {
                            curHeadings = curSonHeading + "-" + curHeadings;
                        }else
                            curHeadings = curSonHeading;
                    }
                }
                upRow--;
            }
            rowHeadingsList.set(i, curHeadings);
        }
    }

    /**
     * 合并第一列标题列表
     */
    public void mergeColHeadings(){
        //对colHeadings进行包装
        wrapColHeadings();

        //对分层标题进行合并
        int curRank = -1;
        for (int i = 0; i < colHeadingsList.size(); i++){
            ColHeadingWrapper curCellWrapper = colHeadingsList.get(i);
            curRank  = curCellWrapper.getPreBlankNum();
            String res = curCellWrapper.getContent();
            //1. 判断当前层是否是子表标题，即curRank == 0
            if (curRank == 0){
                curCellWrapper.setColHeading(res);
                curCellWrapper.setName(res);
                continue;
            }

            //2. 逐层向上合并
            for (int j = i - 1; j >= 0; j--){
                //跳过空行
                if (colHeadingsList.get(j).getIndex() == -1)
                    continue;

                int preRank = colHeadingsList.get(j).getPreBlankNum();

                //判断j层层级是否高于curRank
                if (preRank < curRank){
                    curRank = preRank;
                    res = colHeadingsList.get(j).getContent() + "-" + res;
                    if (curCellWrapper.getUnit().equals("") && !colHeadingsList.get(j).getUnit().equals("")){
                        curCellWrapper.setUnit(colHeadingsList.get(j).getUnit());
                    }
                    curCellWrapper.setName(colHeadingsList.get(j).getContent());
                    curCellWrapper.setColHeading(res.substring(res.indexOf("-")+1));
                    if (preRank == 0) {
                        break;
                    }
                }
            }

            //3.如果上面层中都没有父层级，则直接把该cell的content当成colHeading
            curCellWrapper.setColHeading(curCellWrapper.getContent());
            if (CommonUtil.isNullStr(curCellWrapper.getName()))
                curCellWrapper.setName(excelDataList.get(lastHeadingsRowIndex).get(0));
        }
    }

    /**
     * 对Excel解析后的第一列标题数据进行包装
     */
    public void wrapColHeadings(){
        for (int i = firstDataRowIndex; i < excelDataList.size(); i++){
            //如果该行为空则设置一个空wrapper(index == -1)，并跳过
            if (CommonUtil.isNullRow(excelDataList.get(i))){
                this.colHeadingsList.add(new ColHeadingWrapper(-1));
                continue;
            }

            //如果该行不为空，则新建一个ColHeadingWrapper对象
            String curCell = excelDataList.get(i).get(0);
            ColHeadingWrapper wrapper = new ColHeadingWrapper(i);

            wrapper.setPreBlankNum(CommonUtil.countBlack(curCell));
            wrapper.setContent(CommonUtil.formatColHeading(curCell));
            wrapper.setUnit(CommonUtil.getColHeadingUnit(curCell));

            //判断该行是否为子表的表标题
            if(CommonUtil.isNullDataRow(excelDataList.get(i))
                    && CommonUtil.isNullRow(excelDataList.get(i - 1))){
                wrapper.setPreBlankNum(0);
            }
            this.colHeadingsList.add(wrapper);
        }
    }

    public ArrayList<ArrayList<String>> getExcelDataList() {
        return excelDataList;
    }

    public int getLastHeadingsRowIndex() {
        return lastHeadingsRowIndex;
    }

    public int getFirstHeadingsRowIndex() {
        return firstHeadingsRowIndex;
    }

    public int getFirstDataRowIndex() {
        return firstDataRowIndex;
    }

    public String getTotalUnit() {
        return totalUnit;
    }

    public ArrayList<String> getRowHeadingsList() {
        return rowHeadingsList;
    }

    public ArrayList<ColHeadingWrapper> getColHeadingsList() {
        return colHeadingsList;
    }

    public void setExcelDataList(ArrayList<ArrayList<String>> excelDataList) {
        this.excelDataList = excelDataList;
    }

    public BorderWrapper getBorderWrapper() {
        return borderWrapper;
    }

    public void setBorderWrapper(BorderWrapper borderWrapper) {
        this.borderWrapper = borderWrapper;
    }
}
