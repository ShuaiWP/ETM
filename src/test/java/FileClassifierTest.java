import org.junit.Test;
import reader.BorderWrapper;
import reader.ExcelReader;
import utils.CommonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileClassifierTest {
    @Test
    public void test() throws IOException {
        String dicPath = "D:\\projects\\ETM-main\\年鉴数据分析\\data\\data_xlsx\\2021";
        ArrayList<String> pathList = new ArrayList<>();
        CommonUtil.searchXLSXFiles(dicPath, pathList);
        int sheetIndex = 0;

        for (String filepath : pathList) {
            String year = "";
            Pattern pattern = Pattern.compile("\\\\(\\d{4})\\\\");
            Matcher matcher = pattern.matcher(filepath);
            while (matcher.find()) {
                year = matcher.group(1);
            }

            //todo 读取excel信息
            ArrayList<ArrayList<String>> excelDataList = ExcelReader.read(filepath, sheetIndex);
            BorderWrapper borderWrapper = new BorderWrapper();
            borderWrapper.setBorderList(filepath, sheetIndex);
//        int rowBorderLineNum = borderWrapper.getRowBorderLineNum();

            //todo 对file进行归类
            FileClassifier fileClassifier = new FileClassifier(excelDataList, borderWrapper, filepath);
            String fileClass = fileClassifier.classify();
            System.out.println(filepath.substring(filepath.lastIndexOf("\\")+1) + "  ---  " + fileClass);
        }
    }
}
