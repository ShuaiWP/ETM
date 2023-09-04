create database if not exists etm_db default character set utf8mb4 collate utf8mb4_unicode_ci;
use etm_db;

CREATE TABLE IF NOT EXISTS etm_db.`index_info`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键' PRIMARY KEY,
    `indexName` VARCHAR(255) NOT NULL COMMENT '指标名称',
    `englishName` VARCHAR(255) NULL COMMENT '英文名称',
    `year` INT NOT NULL COMMENT '年份',
    `category` VARCHAR(255) NOT NULL COMMENT '指标分类',
    `lastIndexId` BIGINT NULL COMMENT '上级指标id',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除 (0-未删, 1-已删)'
) COMMENT '指标信息表';