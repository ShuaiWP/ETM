import java.util.ArrayList;
import com.mongodb.client.MongoClients;
import dataParser.DataParser;
import docGenerator.DocGenerator;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import reader.ExcelReader;
import utils.ColHeadingWrapper;


public class ExcelToMongoDB {
    public static void main(String[] args) {
        String path = "C:\\Users\\IDo\\Desktop\\年鉴数据分析\\2012.xls";
        String dbURL = "mongodb://localhost:27017";
        String dbName = "testdb";
        String collectionName = "cl2012";
        ExcelToMongoDB.run(path, dbURL, dbName, collectionName);
    }

    /**
     * Excel数据解析模块运行接口
     * @param filepath 需要转换的Excel文件的路径名字
     */
    public static void run(String filepath, String dbURL, String dbName, String collectionName){
        DataParser dataParser = new DataParser();
        int sheetIndex = 8;

        //todo 读取excel信息
        ArrayList<ArrayList<String>> excelDataList = ExcelReader.read(filepath, sheetIndex);

        //todo DataParse,进行格式解析
        dataParser.setExcelDataList(excelDataList);
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