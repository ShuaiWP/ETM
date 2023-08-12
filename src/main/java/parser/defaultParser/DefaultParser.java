package parser.defaultParser;

import lombok.Data;
import org.bson.Document;
import parser.AbstractParser;
import utils.ColHeadingWrapper;
import utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class DefaultParser extends AbstractParser {
    private int lastHeadingsRowIndex = 0;       //最后一级行标题的index
    private int firstHeadingsRowIndex = 0;       //表格第一行行标题所在行
    private int firstDataRowIndex = 0;  //第一行
    private String totalUnit = "";  //全表的总体单位
    private ArrayList<String> rowHeadingsList = new ArrayList<>();
    private ArrayList<ColHeadingWrapper> colHeadingsList = new ArrayList<>();
    private String excelName;

    @Override
    public Document getDocument() {
        //todo 获取基本参数
        getBasicParam();

        //todo 合并第一行标题和第一列标题
        mergeRowHeadings();
        mergeColHeadings();

        System.out.println("==============="+ excelName +"===============");
        for (String s : rowHeadingsList) {
            System.out.println(s);
        }


        //todo 获取document
        return generateDocument();
    }


    /**
     * 获取基本信息，包括lastHeadingsRowIndex，firstDataRowIndex，firstHeadingsRowIndex，totalUnit, excelName
     */
    public void getBasicParam(){
        this.firstHeadingsRowIndex = borderWrapper.getFirstHeadingsRowIndex();

        //从最后一行往上遍历，去寻找倒数第二条borderRow,作为lastHeadingsRowIndex
//        boolean flag = false;   //用于判断是否已经遍历过最后一根borderRow，去寻找倒数第二根
//        for (int i = borderWrapper.getBorderList().size() - 1; i > firstHeadingsRowIndex; i--){
//            if (borderWrapper.isExistUpBorder_row(i) || borderWrapper.isExistBottomBorder_row(i-1)){
//                if (flag) {
//                    this.lastHeadingsRowIndex = i - 1;
//                    break;
//                }
//                else
//                    flag = true;
//            }else if (i == borderWrapper.getBorderList().size()-1 && !flag){       //考虑到有些表格没有附加信息，最后一根边界在最后一行下方
//                if (borderWrapper.isExistBottomBorder_row(i))
//                    flag = true;
//            }
//        }

        //根据lastHeadingsRowIndex去寻找firstDataRowIndex

        this.lastHeadingsRowIndex = borderWrapper.getLastHeadingsRowIndex(firstHeadingsRowIndex);

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
                    if (curCell.contains("单位：")) {
                        totalUnit = curCell.substring(curCell.indexOf("：") + 1);
                        break;
                    }
                    if (curCell.contains("单位:")){
                        totalUnit = curCell.substring(curCell.indexOf(":") + 1);
                        break;
                    }
                }
            }
        }

        //解析出excelName
        excelName = filepath.substring(filepath.lastIndexOf("\\")+1);
    }

    /**
     * 合并第一行标题列表
     */
    public void mergeRowHeadings(){
        //合并rowHeadings
        ArrayList<String> lastHeadingsRow = excelDataList.get(lastHeadingsRowIndex);
//        rowHeadingsList = new ArrayList<>(lastHeadingsRow);
        rowHeadingsList.addAll(lastHeadingsRow);

        rowHeadingsList.set(0, lastHeadingsRow.get(0));  //设置第一列的行标题

        //通过遍历，从lastRowHeadings往上合并，得到之后每一列的headings
        String lastSonHeading = "";
        for(int i = 0; i < lastHeadingsRow.size(); i++){
            String curHeadings = lastHeadingsRow.get(i);
            lastSonHeading = curHeadings;
            int upRow = lastHeadingsRowIndex - 1;           //当前行的上一行
            while(upRow >= firstHeadingsRowIndex){          //逐行往上遍历
                if (i < excelDataList.get(upRow).size()) {
                    String curSonHeading = excelDataList.get(upRow).get(i);     //上一行的标题内容

                    //判断当前子标题是否为空，并且判断当前标题是否已经包含了当前子标题，即判断是否为新的子标题
                    if (!curSonHeading.equals("") && !curHeadings.contains(curSonHeading)){
                        lastSonHeading = curSonHeading;
                        if (!curHeadings.equals("")) {
                            if (borderWrapper.isExistUpBorder_cell(upRow+1, i) || borderWrapper.isExistBottomBorder_cell(upRow, i)) {
                                curHeadings = curSonHeading + "-" + curHeadings;
                            }else {
                                curHeadings = curSonHeading + curHeadings;
                            }
                        }else
                            curHeadings = curSonHeading;
                    }

                    //如果当前列的子标题为空，则搜索左右两侧是否存在border
                    if (curSonHeading.equals("")){
                        int curColIndex = i;
                        //判断是否有左边界,如果没有，则向左搜索
                        int leftColIndex = curColIndex-1;
                        while (leftColIndex > 0 && !borderWrapper.isExistLeftBorder_cell(upRow,leftColIndex+1)){
                            curSonHeading = excelDataList.get(upRow).get(leftColIndex);
                            //判断当前单元格是否有下边界, 如果没有下边界，则合并
                            for (int j = 1; upRow + j <= lastHeadingsRowIndex; j++){
                                if (!borderWrapper.isExistUpBorder_cell(upRow + j, leftColIndex)
                                        && !borderWrapper.isExistBottomBorder_cell(upRow+j-1, leftColIndex)){
                                    if (!curSonHeading.contains(excelDataList.get(upRow + j).get(leftColIndex)))
                                        curSonHeading = curSonHeading + excelDataList.get(upRow + j).get(leftColIndex);
                                }else
                                    break;
                            }

                            if (!curSonHeading.equals("") && !curHeadings.contains(curSonHeading)){
                                lastSonHeading = curSonHeading;
                                if (!curHeadings.equals("")) {
                                    curHeadings = curSonHeading + "-" + curHeadings;
                                }else
                                    curHeadings = curSonHeading;
                            }
                            leftColIndex--;
                        }

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

        //查找总体colHeadingsName
        String totalColHeadingsName = "";
        for (int j = 0; lastHeadingsRowIndex - j >= firstHeadingsRowIndex; j++){
            String cur = excelDataList.get(lastHeadingsRowIndex-j).get(0);
            if (!CommonUtil.isNullStr(cur)){
                if (totalColHeadingsName.equals("")) {
                    totalColHeadingsName = cur;
                }else if (!totalColHeadingsName.contains(cur)){
                    totalColHeadingsName = cur + totalColHeadingsName;
                }
            }
            if (borderWrapper.isExistUpBorder_cell(lastHeadingsRowIndex - j, 0))
                break;
        }

        //对分层标题进行合并
        int curRank = -1;
        for (int i = 0; i < colHeadingsList.size(); i++){
            ColHeadingWrapper curCellWrapper = colHeadingsList.get(i);
            curRank  = curCellWrapper.getPreBlankNum();
            String res = curCellWrapper.getContent();
            //1. 判断当前层是否是子表标题，即curRank == 0
            if (curRank == 0){
                curCellWrapper.setColHeading(res);
                curCellWrapper.setName(totalColHeadingsName);
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
            if (CommonUtil.isNullStr(curCellWrapper.getColHeading()))
                curCellWrapper.setColHeading(curCellWrapper.getContent());
            if (CommonUtil.isNullStr(curCellWrapper.getName()))
                curCellWrapper.setName(totalColHeadingsName);
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

    public Document generateDocument(){

        String year = "";
        Pattern pattern = Pattern.compile("\\\\(\\d{4})\\\\");
        Matcher matcher = pattern.matcher(filepath);
        while (matcher.find()) {
            year = matcher.group(1);
        }

        /**
         * "name":“中国统计年鉴”，
         * "year":"2012"
         * "address":“全国”
         * "execel_name":“私人汽车拥有量”，//excel名
         * "industry”:“运输和邮电”,//行业分组
         */
        Document doc = new Document()
                .append("name", "中国统计年鉴")
                .append("year", year)
                .append("address", "全国")
                .append("excel_name", excelName)
                .append("industry", "综合");

        //存储data数据
        // 创建数据列表
        List<Document> dataDoc = new ArrayList<>();
        for(int i = firstDataRowIndex; i < excelDataList.size(); i++){
            ArrayList<String> curRowData = excelDataList.get(i);
            //如果该行数据全为空，则跳过该行，对下一行进行处理
            if (CommonUtil.isNullDataRow(curRowData)){
                continue;
            }

            for (int j = 1; j < curRowData.size(); j++) {
                if (!curRowData.get(j).equals("")) {
                    Document itemDoc = new Document();
                    ColHeadingWrapper curColHeading = colHeadingsList.get(i - firstDataRowIndex);
                    //1.附加单位
                    if (!curColHeading.getUnit().equals("")){
                        itemDoc.append("unit", curColHeading.getUnit());
                    }else {
                        itemDoc.append("unit", totalUnit);
                    }

                    //2. 附加row和col
                    itemDoc.append("row", new Document()
                                    .append("name", curColHeading.getName())
                                    .append("type", "label")
                                    .append("value", curColHeading.getColHeading()))
                            .append("col", new Document()
                                    .append("name", rowHeadingsList.get(j))
                                    .append("type", "label")
                                    .append("value", curRowData.get(j)));
                    dataDoc.add(itemDoc);
                }
            }
        }
        doc.append("data", dataDoc);

        return doc;
    }
}
