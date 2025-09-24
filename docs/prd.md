## 轻量级图书管理系统 PRD（基于给定 SQL 模型）

### 一、项目概述

- **技术栈**: 前端 Vue，后端 Spring Boot，数据库 MySQL 8.0+（InnoDB，utf8mb4_unicode_ci）
- **目标**: 基于既有三张表结构（user、book、borrow_record）实现最小可用的图书管理系统：用户注册/登录、图书基础管理、借阅与归还流程。

### 二、范围与不做事项

- **包含**:
  - 用户注册、登录、启禁用；基础资料管理（昵称、手机号）。
  - 图书录入、查询、更新、删除；库存校验（available_stock ≤ total_stock）。
  - 借阅、归还；借阅状态管理（0-在借，1-已还，2-逾期未还）。
- **不包含（与原大纲差异）**:
  - 读者/管理员/超级管理员三层权限与系统参数配置（使用 user.role 0=普通用户，1=管理员）。
  - 逾期罚款与缴纳、续借次数与应还日期（模型中无 due/penalty 字段）。
  - 操作日志、管理员独立表、书架位置、分类字典管理、导出、统计面板等扩展功能。

### 三、角色与权限

- **普通用户（role=0）**:
  - 注册、登录、查看/修改本人资料。
  - 查询图书。
  - 借阅、归还本人图书。
- **管理员（role=1）**:
  - 拥有普通用户全部权限。
  - 维护图书（新增/更新/删除）。
  - 查询全部借阅记录。

用户状态约束：`status` 0-正常，1-禁用。禁用用户不可登录与进行借还操作。

### 四、数据模型（与 SQL 一致）

1. `user`

   - 主键：`id` BIGINT UNSIGNED AUTO_INCREMENT
   - 唯一：`username`，`phone`
   - 字段：`password`（加密存储）、`nickname`、`phone`、`status`(0 正常/1 禁用)、`role`(0 普通/1 管理员)、`is_delete`(0/1)、`create_time`、`update_time`
   - 约束：`status` ∈ {0,1}，`role` ∈ {0,1}，`is_delete` ∈ {0,1}

2. `book`

   - 主键：`id` BIGINT UNSIGNED AUTO_INCREMENT
   - 唯一：`isbn`
   - 字段：`title`、`author`、`publisher`、`publish_date`、`category_id`、`total_stock`、`available_stock`、`is_delete`、`create_time`、`update_time`
   - 约束：`available_stock` ≤ `total_stock`，`is_delete` ∈ {0,1}

3. `borrow_record`
   - 主键：`id` BIGINT UNSIGNED AUTO_INCREMENT
   - 外键：`user_id` → `user.id`、`book_id` → `book.id`（更新级联、删除限制）
   - 字段：`borrow_time`（默认当前）、`return_time`（可空）、`status`(0 在借/1 已还/2 逾期未还)
   - 约束：`status` ∈ {0,1,2}

说明：逾期判定需由业务规则与定时/任务在应用层补充（因缺少应还日期字段，本版本仅保留状态位，默认不自动置逾期）。

### 五、核心业务规则

- **用户**

  - 注册：`username`、`password`、`nickname`、`phone` 必填；`username`/`phone` 唯一；密码前端可 MD5，后端统一使用 BCrypt 存储。
  - 登录：`status` 必须为 0；匹配 `username + password`；返回 JWT（2 小时）。
  - 资料修改：仅本人可改 `nickname`、`phone`、`password`（需验证旧密码）。`username` 不可改。

- **图书**

  - 新增：`isbn` 唯一，`title/author/publisher/publish_date/category_id/total_stock` 必填；`available_stock` 默认等于 `total_stock`。
  - 更新：不可改 `isbn`；需保证 `available_stock` ≤ `total_stock`。
  - 删除：仅软删（`is_delete=1`）或物理删（二选一，建议软删）；若物理删除，需确保无在借记录（由外键 RESTRICT 保障）。

- **借阅/归还**
  - 借阅前置条件：
    1. 用户 `status=0` 且未删除；
    2. 图书 `available_stock > 0` 且未删除；
    3. 同一用户可重复借同一本书的限制由业务决定（本模型未强约束，建议同一用户同一 `book_id` 存在 `status=0` 时禁止再次借该书）。
  - 借阅成功：`available_stock -= 1`，写入 `borrow_record(status=0, borrow_time=now)`。
  - 归还前置条件：存在该用户的在借记录（`status=0`）。
  - 归还成功：`available_stock += 1`，`borrow_record.status=1`，`return_time=now`。
  - 逾期：当前模型无应还日/罚款字段，不计算逾期与罚款；`status=2` 仅保留为业务可选标记位（如外部任务认定）。

### 六、接口设计（RESTful，前缀 /api/v1）

统一响应：

```
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 6.1 认证

- Authorization: Bearer {JWT}
- 管理接口需 `role=1`

#### 6.2 用户接口

1. 注册

   - POST `/api/v1/user/register`
   - req: { username, password(md5), nickname, phone }
   - resp: { id, username, nickname, phone }
   - 校验：username/phone 唯一，必填，密码后端加密存储

2. 登录

   - POST `/api/v1/user/login`
   - req: { username, password(md5) }
   - resp: { token, id, username, role }
   - 校验：用户存在、未禁用、密码匹配

3. 更新个人信息

   - PUT `/api/v1/user/profile`
   - auth: 登录
   - req: { nickname?, phone?, oldPassword?, newPassword? }
   - resp: { id, username, nickname, phone }
   - 限制：仅本人；改密码需验证旧密码

4. 用户列表（管理员）

   - GET `/api/v1/user/list?pageNum=1&pageSize=10&usernameLike=xx&phone=xx&status=0`
   - auth: 管理员
   - resp: { total, list:[{ id, username, nickname, phone, status, role }] }

5. 启禁用用户（管理员）
   - PUT `/api/v1/user/status`
   - auth: 管理员
   - req: { userId, status } // 0 正常，1 禁用
   - resp: { userId, status }

#### 6.3 图书接口

1. 新增图书（管理员）

   - POST `/api/v1/book`
   - req: { isbn, title, author, publisher, publishDate, categoryId, totalStock, availableStock }
   - resp: { id, isbn, title }
   - 校验：isbn 唯一；availableStock ≤ totalStock

2. 图书查询

   - GET `/api/v1/book/list?pageNum=1&pageSize=10&isbn=...&titleLike=...&authorLike=...&categoryId=...`
   - resp: { total, list:[{ id, isbn, title, author, publisher, publishDate, categoryId, totalStock, availableStock }] }

3. 更新图书（管理员）

   - PUT `/api/v1/book`
   - req: { id, title?, author?, publisher?, publishDate?, categoryId?, totalStock?, availableStock? }
   - 限制：不可修改 isbn；需保证 availableStock ≤ totalStock

4. 删除图书（管理员）
   - DELETE `/api/v1/book/{id}`
   - 策略：推荐软删（is_delete=1）；如物理删除，若存在外键在借记录将被数据库拒绝

#### 6.4 借阅接口

1. 借阅（登录用户）

   - POST `/api/v1/borrow`
   - req: { bookId }
   - resp: { id, bookId, borrowTime, status } // status=0
   - 校验：用户正常、书未删且库存>0；用户对同一本书不允许重复在借

2. 归还（登录用户）

   - PUT `/api/v1/borrow/return`
   - req: { borrowId }
   - resp: { id, returnTime, status } // status=1
   - 逻辑：仅归还本人在借记录；成功后库存 +1

3. 我的借阅列表

   - GET `/api/v1/borrow/my?pageNum=1&pageSize=10&status=0`
   - resp: { total, list:[{ id, bookId, borrowTime, returnTime, status }] }

4. 借阅记录（管理员）
   - GET `/api/v1/borrow/list?pageNum=1&pageSize=10&userId=...&bookId=...&status=...`
   - auth: 管理员
   - resp: { total, list:[{ id, userId, bookId, borrowTime, returnTime, status }] }

### 七、错误码约定

- 200 成功；400 参数错误；401 未登录；403 无权限；404 资源不存在；409 冲突（如唯一约束/库存不足）；500 服务器错误。

### 八、数据一致性与并发

- 借阅与归还涉及 `available_stock` 增减，使用事务保证：
  1. 读库存行时加行级锁；
  2. 校验 >0 再减库存；
  3. 写入/更新 `borrow_record`；
  4. 提交事务。
- 删除图书前校验无在借记录（由外键 RESTRICT 与业务共同保障）。

### 九、非功能与安全

- 密码后端采用 BCrypt 存储；JWT 2 小时有效，支持刷新。
- 输入校验与防重复提交；审计可后续引入操作日志表。
- 软删优先，保留历史一致性。

### 十、后续可扩展方向（超出本次范围）

- 增加应还日期、续借、罚款与支付；操作日志；更细粒度角色；统计与看板；分类字典与检索优化；书架位置管理。
