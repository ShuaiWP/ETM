package com.iscas.etm;

import com.iscas.etm.sqlDao.domain.IndexInfo;
import com.iscas.etm.sqlDao.service.IndexInfoService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
@MapperScan("com.iscas.etm.sqlDao.mapper")
public class ETMApplication {

    @Resource
    private IndexInfoService indexInfoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ETMApplication.class, args);
        context.getBean(ETMApplication.class).test();
    }

    public void test(){
        IndexInfo indexInfo = new IndexInfo();
        indexInfo.setIndexName("test_update");
        indexInfo.setId(1L);
        boolean b = indexInfoService.updateById(indexInfo);
        System.out.println(b);
    }
}
