import com.iscas.etm.excelParser.classifier.FileClassifier;
import org.junit.Test;
import com.iscas.etm.excelParser.reader.BorderWrapper;
import com.iscas.etm.excelParser.reader.ExcelReader;
import com.iscas.etm.excelParser.utils.CommonUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileClassifierTest {
    @Test
    public void test() throws IOException {
        String Path = "D:\\projects\\ETM-main\\年鉴数据分析\\data\\others\\2021_xlxs";

        File directory = new File(Path);
        File[] subDirectories = directory.listFiles(File::isDirectory);

        ArrayList<String> subDirectoryPaths = new ArrayList<>();
        for (File subDirectory : subDirectories) {
            subDirectoryPaths.add(subDirectory.getAbsolutePath());
        }
        subDirectoryPaths.add(Path);

        FileWriter fileWriter = new FileWriter("errPath.txt");

        double num = 0;
        double err = 0;

        for (String dicPath : subDirectoryPaths) {

//        String dicPath = "D:\\projects\\ETM-main\\年鉴数据分析\\data\\data_xlsx\\2005";
            ArrayList<String> pathList = new ArrayList<>();
            CommonUtil.searchXLSXFiles(dicPath, pathList);
            int sheetIndex = 0;

            double totalNum = 0;
            double errorNum = 0;
            double defaultNum = 0;
            for (String filepath : pathList) {
                num++;
                totalNum++;

                //todo 读取excel信息
                ArrayList<ArrayList<String>> excelDataList = ExcelReader.read(filepath, sheetIndex);
                BorderWrapper borderWrapper = new BorderWrapper();
                borderWrapper.setBorderList(filepath, sheetIndex);
//        int rowBorderLineNum = borderWrapper.getRowBorderLineNum();

                //todo 对file进行归类
                FileClassifier fileClassifier = new FileClassifier(excelDataList, borderWrapper, filepath);
                String fileClass = null;
                try {
                    fileClass = fileClassifier.classify();
                } catch (Exception e) {
                    fileClass = e.getMessage();
                    fileWriter.write(fileClass + "\n");
//                    System.out.println(fileClass);
                }
//            System.out.println(filepath.substring(filepath.lastIndexOf("\\")+1) + "  ---  " + fileClass);
                if (fileClass.contains("error")) {
                    err++;
                    errorNum++;
                } else if (fileClass.contains("factory.DefaultParserFactory"))
                    defaultNum++;
            }
            System.out.println("======================================");
            System.out.println(dicPath);
            System.out.println("该路径下共有：" + totalNum + "个excel文件");
            System.out.println("  未分类的有" + errorNum + "个，占比：" + errorNum / totalNum * 100 + "%");
            System.out.println("  default类占比：" + defaultNum / totalNum  * 100 + "%");
            System.out.println("  存在续表类占比：" + (totalNum - defaultNum - errorNum) / totalNum * 100 + "%");
            System.out.println("预计可处理的占比：" + (1 - errorNum / totalNum)* 100 + "%");
        }
        System.out.println("======================================");
        System.out.println("总计有：" + num + "个excel文件");
        System.out.println("未分类的有" + err + "个，占比：" + err / num * 100 + "%");
        System.out.println("预计可处理的占比：" + (1 - err / num)* 100 + "%");
    }
}
