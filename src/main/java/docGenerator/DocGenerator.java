package docGenerator;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class DocGenerator {

    public static Document get(ArrayList<ArrayList<String>> excelDataList,
                               ArrayList<String> rowHeadingsList,
                               int firstDataRowIndex){
        /**
         * "name":“中国统计年鉴”，
         * "year":"2012"
         * "address":“全国”
         * "execel_name":“私人汽车拥有量”，//excel名
         * "industry”:“运输和邮电”,//行业分组
         */
        Document doc = new Document()
                .append("name", "中国统计年鉴")
                .append("year", "2012")
                .append("address", "全国")
                .append("execel_name", excelDataList.get(0).get(0))
                .append("industry", "综合");      //where the info?

        //存储data数据
        // 创建数据列表
        List<Document> dataDoc = new ArrayList<>();
        for(int i = firstDataRowIndex; i < excelDataList.size(); i++){
            ArrayList<String> curRowData = excelDataList.get(i);
            for (int j = 1; j < curRowData.size(); j++) {
                if (!curRowData.get(j).equals(""))
                    dataDoc.add(new Document()
                            .append("unit", rowHeadingsList.get(rowHeadingsList.size()-1))
                            .append("row", new Document()
                                    .append("name", rowHeadingsList.get(0))
                                    .append("type", "label")
                                    .append("value", curRowData.get(0)))
                            .append("col", new Document()
                                    .append("name", rowHeadingsList.get(j))
                                    .append("type", "label")
                                    .append("value", curRowData.get(j))));
            }
        }
        doc.append("data", dataDoc);

        return doc;
    }
}
