import com.iscas.etm.ETMApplication;
import com.iscas.etm.sqlDao.domain.IndexInfo;
import com.iscas.etm.sqlDao.service.IndexInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = ETMApplication.class)
public class SqlTest {
    @Resource
    private IndexInfoService indexInfoService;

    @Test
    public void test(){
        IndexInfo indexInfo = new IndexInfo();
        indexInfo.setIndexName("test");
        indexInfo.setEnglishName("test");
        indexInfo.setYear(2021);
        indexInfo.setCategory("test");
        indexInfo.setLastIndexId(0L);
        boolean save = indexInfoService.save(indexInfo);
        System.out.println(save);
    }
}
