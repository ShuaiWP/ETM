package com.iscas.etm.excelParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iscas.etm.excelParser.factory.AbstractParserFactory;
import com.iscas.etm.excelParser.reader.BorderWrapper;
import com.iscas.etm.excelParser.reader.ExcelReader;
import com.iscas.etm.excelParser.classifier.FileClassifier;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.iscas.etm.excelParser.parser.AbstractParser;
import com.iscas.etm.excelParser.utils.CommonUtil;


public class ExcelToMongoDB {
    public static void main(String[] args) throws IOException {
//        String dicPath = "D:\\projects\\ETM-main\\年鉴数据分析\\data\\2012.xlsx";       //彩色版
//        String dicPath = "D:\\projects\\ETM-main\\年鉴数据分析\\data\\data_xlsx\\2021\\test\\1-6 分地区按三次产业和机构类型分法人单位数(2020年).xlsx";
//        String dicPath = "D:\\projects\\ETM-main\\年鉴数据分析\\data\\data_xlsx\\1982";
        String dicPath = "D:\\projects\\ETM-main\\年鉴数据分析\\data\\data_xlsx\\2021\\1-2 国民经济和社会发展总量与速度指标.xlsx";    //续表纵向
//        String dicPath = "D:\\projects\\ETM-main\\年鉴数据分析\\data\\data_xlsx\\2021\\1-8 按地区和登记注册类型分企业法人单位数(2020年).xlsx";//续表横向

        ArrayList<String> pathList = new ArrayList<>();
        CommonUtil.searchXLSXFiles(dicPath, pathList);
        int sheetIndex = 0;

        String dbURL = "mongodb://localhost:27017";
        String dbName = "testdb";

        for (String path : pathList) {
            String year = "";
            Pattern pattern = Pattern.compile("\\\\(\\d{4})\\\\");
            Matcher matcher = pattern.matcher(path);
            while (matcher.find()) {
                year = matcher.group(1);
            }

            ExcelToMongoDB.run(path, dbURL, dbName, year, sheetIndex);
        }
    }

    /**
     * Excel数据解析模块运行接口
     * @param filepath 需要转换的Excel文件的路径名字
     */
    public static void run(String filepath, String dbURL, String dbName, String year, int sheetIndex) throws IOException {

        //todo 读取excel信息
        ArrayList<ArrayList<String>> excelDataList = ExcelReader.read(filepath, sheetIndex);
        BorderWrapper borderWrapper = new BorderWrapper();
        borderWrapper.setBorderList(filepath, sheetIndex);
//        int rowBorderLineNum = borderWrapper.getRowBorderLineNum();

        //todo 对file进行归类
        FileClassifier fileClassifier = new FileClassifier(excelDataList, borderWrapper, filepath);
        String fileClass = fileClassifier.classify();

        //todo 获取对应parser，并解析
        try {
            AbstractParser parser = AbstractParserFactory.getParserFactory(fileClass).getParser(excelDataList, borderWrapper, filepath);
            Document document = parser.getDocument();

            //todo 将document存储进MongoDB中
            //配置MongoDB信息
            MongoClient mongoClient = MongoClients.create(dbURL);
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection("cl" + year);
            //存储doc
            collection.insertOne(document);
            // 关闭MongoDB连接
            mongoClient.close();
            System.out.println("success!");
        }catch (RuntimeException e){
//            System.out.println(e.getMessage());
            throw e;
        }
    }

}