package com.iscas.etm.sqlDao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.etm.sqlDao.domain.IndexInfo;
import com.iscas.etm.sqlDao.service.IndexInfoService;
import com.iscas.etm.sqlDao.mapper.IndexInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author shuaishuai
* @description 针对表【index_info(指标信息表)】的数据库操作Service实现
* @createDate 2023-09-04 11:13:17
*/
@Service
public class IndexInfoServiceImpl extends ServiceImpl<IndexInfoMapper, IndexInfo>
    implements IndexInfoService{

}




