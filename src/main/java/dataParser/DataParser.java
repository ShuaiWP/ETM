package dataParser;

import utils.CommonUtil;

import java.util.ArrayList;

public class DataParser {
    private ArrayList<ArrayList<String>> excelDataList = null;
    private int lastHeadingsRowIndex = 0;       //最后一级行标题的index
    private int unitRowIndex = 0;       //表格单位所在行
    private int firstDataRowIndex = 0;  //第一行
    private String totalUnit = "";  //全表的总体单位
    private ArrayList<String> rowHeadingsList = new ArrayList<>();
    private ArrayList<String> colHeadingsList = new ArrayList<>();

    /**
     * DataParser之一，适配于多层级行标题，但是需要顶层行标题是合并单元格的
     * 解析方法：从最下层行标题往上合并，中间用“-”隔开
     */
    public void parse(){
        //todo 获取基本参数: lastHeadingsRowIndex，unitRowIndex，totalUnit
        getBasicParam();

        //todo 合并第一行标题和第一列标题
        mergeRowHeadings();
        mergeColHeadings();

    }

    public void getBasicParam(){
        int nullRowCount = 0;
        for (int i = 0; i < excelDataList.size(); i++){
            ArrayList<String> curRow = excelDataList.get(i);
            if (CommonUtil.isNullRow(curRow)){
                //之前是否已经有一段空白行了
                if (nullRowCount == 1){
                    lastHeadingsRowIndex = i - 1;
                    break;
                }else{
                    //下一行是否还是空白行
                    if(!CommonUtil.isNullRow(excelDataList.get(i + 1))){
                        nullRowCount++;
                        unitRowIndex = i + 1;
                    }
                }
            }
        }
        firstDataRowIndex = lastHeadingsRowIndex + 2;
        totalUnit = excelDataList.get(unitRowIndex).get(0);
    }

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
            while(upRow > unitRowIndex){
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

        String unitCell = excelDataList.get(unitRowIndex).get(0);
        rowHeadingsList.add(unitCell.substring(unitCell.indexOf("：")+1));
    }

    public void mergeColHeadings(){

    }

    public ArrayList<ArrayList<String>> getExcelDataList() {
        return excelDataList;
    }

    public int getLastHeadingsRowIndex() {
        return lastHeadingsRowIndex;
    }

    public int getUnitRowIndex() {
        return unitRowIndex;
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

    public ArrayList<String> getColHeadingsList() {
        return colHeadingsList;
    }

    public void setExcelDataList(ArrayList<ArrayList<String>> excelDataList) {
        this.excelDataList = excelDataList;
    }
}
