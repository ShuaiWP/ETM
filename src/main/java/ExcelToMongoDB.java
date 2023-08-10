import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.client.MongoClients;
import dataParser.DataParser;
import docGenerator.DocGenerator;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import reader.ExcelReader;
import utils.BorderWrapper;
import utils.ColHeadingWrapper;
import utils.CommonUtil;


public class ExcelToMongoDB {
    public static void main(String[] args) throws IOException {
//        String dicPath = "D:\\年鉴数据分析\\data\\2012.xlsx";       //彩色版
//        String dicPath = "D:\\年鉴数据分析\\data\\1994年\\after\\附录4-42 教师及学生人数.xlsx";     //普通格式, 有问题
        String dicPath = "D:\\年鉴数据分析\\data\\2001年\\after\\附录2-3  就业状况.xlsx";   //半包围结构标题
//        String dicPath = "D:\\年鉴数据分析\\data\\real\\2021\\1-8 按地区和登记注册类型分企业法人单位数(2020年).xlsx";
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
        DataParser dataParser = new DataParser();
        BorderWrapper borderWrapper = new BorderWrapper();

        //todo 读取excel信息
        ArrayList<ArrayList<String>> excelDataList = ExcelReader.read(filepath, sheetIndex);
        borderWrapper.setBorderList(filepath, sheetIndex);

        //todo DataParse,进行格式解析
        dataParser.setExcelDataList(excelDataList);
        dataParser.setBorderWrapper(borderWrapper);
        dataParser.setPath(filepath);
        dataParser.parse();
        ArrayList<String> rowHeadingsList = dataParser.getRowHeadingsList();      //第一行的合并后的分级标题
        ArrayList<ColHeadingWrapper> colHeadingsList = dataParser.getColHeadingsList();      //第一列的合并后的分级标题
        int firstDataRowIndex = dataParser.getFirstDataRowIndex();

        System.out.println("==============="+ dataParser.getExcelName() +"===============");
        for (String s : rowHeadingsList) {
            System.out.println(s);
        }

        //todo 生成document对象
        Document document = DocGenerator.get(excelDataList, rowHeadingsList, colHeadingsList,
                firstDataRowIndex, dataParser.getTotalUnit(), year, dataParser.getExcelName());

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
    }

}