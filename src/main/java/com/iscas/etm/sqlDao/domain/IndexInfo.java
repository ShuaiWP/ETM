package com.iscas.etm.sqlDao.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 指标信息表
 * @TableName index_info
 */
@TableName(value ="index_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 指标名称
     */
    private String indexName;

    /**
     * 英文名称
     */
    private String englishName;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 指标分类
     */
    private String category;

    /**
     * 上级指标id
     */
    private Long lastIndexId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除 (0-未删, 1-已删)
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}