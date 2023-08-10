package docGenerator;

import org.bson.Document;
import utils.ColHeadingWrapper;
import utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class DocGenerator {

    public static Document get(ArrayList<ArrayList<String>> excelDataList,
                               ArrayList<String> rowHeadingsList,
                               ArrayList<ColHeadingWrapper> colHeadingsList,
                               int firstDataRowIndex,
                               String totalUnit,
                               String year,
                               String excelName){
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
