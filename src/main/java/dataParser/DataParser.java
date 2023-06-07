package dataParser;

import utils.CommonUtil;

import java.util.ArrayList;

public class DataParser {
    /**
     * DataParser之一，适配于多层级行标题，但是需要顶层行标题是合并单元格的
     * 解析方法：从最下层行标题往上合并，中间用“-”隔开
     * @param data excelReader生成的数据列表
     * @param rowHeadingsList 合并完格式的行标题列表，比如“县级-县”
     * @return data中数据部分的第一行index
     */
    public static int multipleRowTitleParser(ArrayList<ArrayList<String>> data, ArrayList<String> rowHeadingsList){
        //定位最后一行子标题的位置
        int lastHeadingsRowIndex = 0;
        int nullRowCount = 0;
        int unitRowIndex = 0;
        for (int i = 0; i < data.size(); i++){
            ArrayList<String> curRow = data.get(i);
            if (CommonUtil.isNullRow(curRow)){
                //之前是否已经有一段空白行了
                if (nullRowCount == 1){
                    lastHeadingsRowIndex = i - 1;
                    break;
                }else{
                    //下一行是否还是空白行
                    if(!CommonUtil.isNullRow(data.get(i + 1))){
                        nullRowCount++;
                        unitRowIndex = i + 1;
                    }
                }
            }
        }

        //合并rowHeadings
        ArrayList<String> lastHeadingsRow = data.get(lastHeadingsRowIndex);
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
                if (i < data.get(upRow).size()) {
                    String curSonHeading = data.get(upRow).get(i);
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

        String unitCell = data.get(unitRowIndex).get(0);
        rowHeadingsList.add(unitCell.substring(unitCell.indexOf("：")+1));

        return lastHeadingsRowIndex + 2;
    }

}
