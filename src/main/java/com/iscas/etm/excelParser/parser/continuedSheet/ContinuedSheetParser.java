package com.iscas.etm.excelParser.parser.continuedSheet;

import com.iscas.etm.excelParser.classifier.FileClassifier;
import com.iscas.etm.excelParser.factory.AbstractParserFactory;
import org.bson.Document;
import com.iscas.etm.excelParser.parser.AbstractParser;
import com.iscas.etm.excelParser.reader.BorderWrapper;
import com.iscas.etm.excelParser.utils.CommonUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContinuedSheetParser extends AbstractParser {
    private ContinuedPointWrapper pointWrapper = new ContinuedPointWrapper();

    @Override
    public Document getDocument() {
        //1. 获取ContinuedPointWrapper
        pointWrapper.wrapContinuedPoint(excelDataList, borderWrapper);
        ArrayList<ContinuedPoint> continuedPointList = pointWrapper.getContinuedPointList();
        List<Document> dataDoc = new ArrayList<>();

        //2. 遍历ContinuedPoint，分别对每一个续表进行分析
        for (ContinuedPoint point : continuedPointList) {
            //  2.1 切割成子表
            ArrayList<ArrayList<String>> sonExcelDataList = CommonUtil.cutExcelDataList(excelDataList, point.getRow(),
                    point.getCol(), point.getColLength(), point.getRowLength());
            BorderWrapper sonBorderWrapper = CommonUtil.cutBorderWrapper(borderWrapper, point.getRow(),
                    point.getCol(), point.getColLength(), point.getRowLength());

            //  2.2 用defaultParser解析子表
            FileClassifier fileClassifier = new FileClassifier();
            fileClassifier.setBorderWrapper(sonBorderWrapper);
            fileClassifier.setExcelDataList(sonExcelDataList);
            fileClassifier.setFilepath("ContinuedPoint");
            String fileClass = fileClassifier.classify();

            AbstractParser parser = AbstractParserFactory.getParserFactory(fileClass)
                    .getParser(sonExcelDataList, sonBorderWrapper, "ContinuedPoint");
            Document document = parser.getDocument();

            //  2.3 获取到doc中数据那一块
            List<Document> sonData = (List<Document>) (document.get("data"));
            dataDoc.addAll(sonData);
        }

        //3. 整合数据，形成新doc
        return generateDocument(dataDoc);
    }

    public Document generateDocument(List<Document> dataDoc){

        String year = "";
        Pattern pattern = Pattern.compile("\\\\(\\d{4})\\\\");
        Matcher matcher = pattern.matcher(filepath);
        while (matcher.find()) {
            year = matcher.group(1);
        }

        String excelName = filepath.substring(filepath.lastIndexOf("\\")+1);

        /**
         * "name":“中国统计年鉴”，
         * "year":"2012"
         * "address":“全国”
         * "execel_name":“私人汽车拥有量”，//excel名
         * "industry”:“运输和邮电”,//行业分组
         */

        return new Document()
                .append("name", "中国统计年鉴")
                .append("year", year)
                .append("address", "全国")
                .append("excel_name", excelName)
                .append("industry", "综合")
                .append("data", dataDoc);
    }
}
