import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mongodb.client.MongoClients;
import dataParser.DataParser;
import docGenerator.DocGenerator;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.poi.ss.util.CellRangeAddress;
import org.bson.Document;
import reader.ExcelReader;
import utils.CommonUtil;


public class ExcelToMongoDB {
    public static void main(String[] args) {
        String path = "C:\\Users\\IDo\\Desktop\\年鉴数据分析\\2012.xls";
        ExcelToMongoDB.run(path);
    }

    /**
     * Excel数据解析模块运行接口
     * @param filepath 需要转换的Excel文件的路径名字
     */
    public static void run(String filepath){
        String sheetName = "Sheet1";

        //todo 读取excel信息
        ArrayList<ArrayList<String>> excelDataList = ExcelReader.read(filepath);

        //todo DataParse,进行格式解析
        ArrayList<String> rowHeadingsList = new ArrayList<>();
        int firstDataRowIndex = DataParser.multipleRowTitleParser(excelDataList, rowHeadingsList);

        //todo 生成document对象
        Document document = DocGenerator.get(excelDataList, rowHeadingsList, firstDataRowIndex);

        //todo 将document存储进MongoDB中
        //配置MongoDB信息
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        String dbName = "testdb";
        String collectionName = "cl2012";
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        //存储doc
        collection.insertOne(document);
        // 关闭MongoDB连接
        mongoClient.close();
        System.out.println("success!");
    }

}