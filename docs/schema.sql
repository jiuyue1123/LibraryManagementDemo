-- 建议：使用 InnoDB、utf8mb4_unicode_ci，MySQL 8.0+
-- 由于是单体应用，使用原生外键约束
-- 先删子表再删父表，避免外键依赖问题
DROP TABLE IF EXISTS `borrow_record`;
DROP TABLE IF EXISTS `book`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `category`;

-- 用户表
CREATE TABLE `user`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户id，主键，自增',
    `username`    VARCHAR(64)     NOT NULL COMMENT '用户名',
    `password`    VARCHAR(255)    NOT NULL COMMENT '密码，加密',
    `nickname`    VARCHAR(64)     NOT NULL COMMENT '昵称',
    `phone`       VARCHAR(32)     NOT NULL COMMENT '手机号,唯一',
    `status`      TINYINT         NOT NULL DEFAULT 0 COMMENT '状态，0-正常，1-禁用',
    `role`        TINYINT         NOT NULL DEFAULT 0 COMMENT '角色，0-普通用户，1-管理员',
    `is_delete`   TINYINT         NOT NULL DEFAULT 0 COMMENT '是否删除，0-正常，1-删除',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_is_delete` (`is_delete`) COMMENT '是否删除索引,用于查询未删除的数据',
    KEY `idx_status` (`status`),
    CHECK (`status` IN (0, 1)),
    CHECK (`role` IN (0, 1)),
    CHECK (`is_delete` IN (0, 1))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='用户表';

-- 图书分类表
CREATE TABLE `category`
(
    `id`   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类id，主键，自增',
    `name` VARCHAR(64)     NOT NULL COMMENT '分类名称',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='图书分类表';

-- 图书表
CREATE TABLE `book`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '图书id，主键，自增',
    `isbn`            VARCHAR(32)     NOT NULL COMMENT '图书isbn',
    `title`           VARCHAR(255)    NOT NULL COMMENT '图书标题',
    `author`          VARCHAR(128)    NOT NULL COMMENT '作者',
    `publisher`       VARCHAR(128)    NOT NULL COMMENT '出版社',
    `publish_date`    DATE            NOT NULL COMMENT '出版日期',
    `category_id`     BIGINT UNSIGNED NOT NULL COMMENT '图书分类Id, 外键(外部或字典表)',
    `total_stock`     INT UNSIGNED    NOT NULL DEFAULT 0 COMMENT '总库存数量',
    `available_stock` INT UNSIGNED    NOT NULL DEFAULT 0 COMMENT '当前可借数量,≤total_stock',
    `is_delete`       TINYINT         NOT NULL DEFAULT 0 COMMENT '是否删除，0-正常，1-删除',
    `create_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_isbn` (`isbn`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_is_delete` (`is_delete`),
    CHECK (`available_stock` <= `total_stock`),
    CHECK (`is_delete` IN (0, 1)),
    CONSTRAINT `fk_category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='图书表';

-- 借阅记录表
CREATE TABLE `borrow_record`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '借阅记录id，主键，自增',
    `user_id`     BIGINT UNSIGNED NOT NULL COMMENT '用户id, 外键',
    `book_id`     BIGINT UNSIGNED NOT NULL COMMENT '图书id, 外键',
    `borrow_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借阅时间',
    `return_time` DATETIME        NULL     DEFAULT NULL COMMENT '还书时间',
    `status`      TINYINT         NOT NULL DEFAULT 0 COMMENT '借阅状态 0-在借，1-已还，2-逾期未还',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_book_id` (`book_id`),
    KEY `idx_status` (`status`),
    CONSTRAINT `fk_borrow_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT `fk_borrow_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CHECK (`status` IN (0, 1, 2))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='借阅记录表';