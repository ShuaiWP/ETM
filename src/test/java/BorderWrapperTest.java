import org.junit.Test;
import reader.BorderWrapper;

import java.io.IOException;

public class BorderWrapperTest {

    public BorderWrapper borderWrapper = new BorderWrapper();
    @Test
    public void setBorderListTest() throws IOException {
        borderWrapper.setBorderList("D:\\年鉴数据分析\\data\\2012.xlsx",0);
        System.out.println(borderWrapper.isExistUpBorder_cell(4,0));
    }

    @Test
    public void isExistUpBorder_row_test() throws IOException {
        borderWrapper.setBorderList("D:\\年鉴数据分析\\data\\2009年\\after\\附录2-10 外汇储备.xlsx",0);
        boolean s4 = borderWrapper.isExistUpBorder_row(0);
        boolean s5 = borderWrapper.isExistUpBorder_row(5);
        boolean s6 = borderWrapper.isExistUpBorder_row(49);
        System.out.println(s4 + " " + s5 + " " + s6);
    }
}
