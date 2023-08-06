import java.io.IOException;
import java.util.ArrayList;
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
//        String path = "C:\\Users\\IDo\\Desktop\\年鉴数据分析\\N2021040003000930(1).xls";
//        String path = "C:\\Users\\IDo\\Desktop\\年鉴数据分析\\data\\1997年\\after\\18-1 教育事业基本情况.xlsx";
//        String path = "D:\\年鉴数据分析\\data\\2009年\\after\\附录2-6 中国主要指标居世界位次.xlsx";
        String path = "D:\\年鉴数据分析\\data\\2009年\\after\\附录2-10 外汇储备.xlsx";
        String dbURL = "mongodb://localhost:27017";
        String dbName = "testdb";
        String collectionName = "cl2009";

        ExcelToMongoDB.run(path, dbURL, dbName, collectionName);
    }

    /**
     * Excel数据解析模块运行接口
     * @param filepath 需要转换的Excel文件的路径名字
     */
    public static void run(String filepath, String dbURL, String dbName, String collectionName) throws IOException {
        DataParser dataParser = new DataParser();
        BorderWrapper borderWrapper = new BorderWrapper();
        int sheetIndex = 0;

        //todo 读取excel信息
        ArrayList<ArrayList<String>> excelDataList = ExcelReader.read(filepath, sheetIndex);
        borderWrapper.setBorderList(filepath);

        //todo DataParse,进行格式解析
        dataParser.setExcelDataList(excelDataList);
        dataParser.setBorderWrapper(borderWrapper);
        dataParser.parse();
        ArrayList<String> rowHeadingsList = dataParser.getRowHeadingsList();      //第一行的合并后的分级标题
        ArrayList<ColHeadingWrapper> colHeadingsList = dataParser.getColHeadingsList();      //第一列的合并后的分级标题
        int firstDataRowIndex = dataParser.getFirstDataRowIndex();

        //todo 生成document对象
        Document document = DocGenerator.get(excelDataList, rowHeadingsList, colHeadingsList,
                firstDataRowIndex, dataParser.getTotalUnit());

        //todo 将document存储进MongoDB中
        //配置MongoDB信息
        MongoClient mongoClient = MongoClients.create(dbURL);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        //存储doc
        collection.insertOne(document);
        // 关闭MongoDB连接
        mongoClient.close();
        System.out.println("success!");
    }

}