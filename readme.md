# 图书管理系统（LibraryManagementSystem）

一个基于 Spring Boot 3 的简易图书管理系统，集成 Sa-Token + JWT 认证、RESTful API、基于 MyBatis-Plus 的数据访问，以及 Swagger/Knife4j 在线接口文档。

## 技术栈

- Spring Boot 3.5.6（Web）
- MyBatis-Plus 3.5.14（含 jsqlparser）
- MySQL 8（Connector/J）
- Sa-Token 1.44.0（含 JWT）
- Lombok
- Knife4j OpenAPI 3 Starter（Swagger UI）
- Java 21

## 模块结构

- `org.example.controller`：认证、用户、图书、分类、借阅记录的 REST 控制器
- `org.example.service`：服务接口与实现
- `org.example.mapper`：MyBatis-Plus Mapper 接口
- `org.example.pojo`：DTO、实体、VO
- `org.example.constant.enums`：角色、用户状态、删除标记、借阅状态枚举
- `org.example.config`：Sa-Token 相关配置

## 快速开始

### 环境要求

- Java 21
- Maven 3.9+
- MySQL 8.x

### 数据库

使用 `docs/schema.sql` 初始化数据库。推荐：InnoDB、utf8mb4_unicode_ci。
包含以下表：

- `user`（唯一 `phone` 与 `username`，逻辑删除 `is_delete`，角色/状态约束）
- `category`
- `book`（外键 `category_id`，库存字段及约束）
- `borrow_record`（外键 `user_id`、`book_id`，状态约束）

### 配置

主配置文件：`src/main/resources/application.yml`

```yaml
spring:
  application:
    name: LibraryManagementSystem
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://${lms.mysql.host}:${lms.mysql.port}/${lms.mysql.database}?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&allowPublicKeyRetrieval=true
    username: ${lms.mysql.username}
    password: ${lms.mysql.password}
  profiles:
    active: test
mybatis-plus:
  mapper-locations: classpath*:mapper/**/*.xml
  global-config:
    db-config:
      logic-delete-field: is_delete
      logic-delete-value: 1
      logic-not-delete-value: 0
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
springdoc:
  swagger-ui:
    path: /swagger-ui.html
  api-docs:
    path: /v3/api-docs
knife4j:
  enable: true
  setting:
    language: zh_cn
    enable-swagger-models: true
    swagger-model-name: models
sa-token:
  jwt-secret-key: <请设置你的密钥>
```

测试环境默认配置：`src/main/resources/application-test.yml`

```yaml
lms:
  mysql:
    host: wsl.local
    port: 3306
    database: lms_db
    username: root
    password: root
```

在其他环境请通过环境变量或 JVM `-D` 参数设置 `lms.mysql.*`。

### 构建与运行

- 构建：`mvn -q -DskipTests package`
- 运行：`mvn spring-boot:run`
- 或直接运行主类：`org.example.LibraryManagementSystemApplication`

### 运行方式

- 使用 Maven 打包（跳过测试）：

```bash
mvn clean package -DskipTests
```

- 以可执行 Jar 运行（示例）：

```bash
java -jar target/LibraryManagementSystem-0.0.1-SNAPSHOT.jar
# 或者
java -jar target/xxx.jar
```

### 接口文档

- Swagger UI（Knife4j）：`http://localhost:8080/swagger-ui.html`
- OpenAPI JSON：`http://localhost:8080/v3/api-docs`

## 鉴权说明

使用 Sa-Token + JWT。先登录获取 token，后续请求在请求头中携带：

```
Authorization: Bearer <token>
```

部分控制器使用 `@SaCheckLogin` 注解，需登录后访问。

### 认证（Auth）

- POST `/auth/login` — 用户登录
  - 请求体：`LoginDTO { phone, password }`
  - 响应：`{ code, msg, data: { token } }`
- POST `/auth/register` — 用户注册
  - 请求体：`RegisterDTO { phone, password }`
  - 自动生成用户名 `lms_#####`，角色为 USER，密码按代码逻辑使用 MD5 存储。

## 接口一览

全局响应包装：`Result<T>`（约定：`success(data)` 或 `success()`）

### 用户（`/user`，需登录）

- GET `/user` — 分页查询
  - 查询参数：`pageNum`, `pageSize`
- GET `/user/{id}` — 按 id 查询
- DELETE `/user/{id}` — 按 id 删除
- PUT `/user/{id}` — 更新用户
  - 请求体：`UpdateUserDTO { nickname, phone, status, password? }`

### 图书（`/book`，需登录）

- GET `/book` — 分页查询
  - 查询参数：`pageNum`, `pageSize`
- POST `/book` — 新增图书
  - 请求体：`Book`
- DELETE `/book/{id}` — 按 id 删除
- DELETE `/book` — 批量删除
  - 请求体：`List<Long>` ids

### 分类（`/category`，需登录）

- GET `/category/list` — 列表
- POST `/category` — 新增
  - 请求体：`Category`
- DELETE `/category/{id}` — 按 id 删除
- DELETE `/category` — 批量删除
  - 请求体：`List<Long>` ids

### 借阅记录（`/borrowRecord`，需登录）

- GET `/borrowRecord` — 分页查询
  - 查询参数：`pageNum`, `pageSize`
- GET `/borrowRecord/{id}` — 按 id 查询

## 实体（简要）

- `User { id, username, password, nickname, phone, status, role, isDelete, createTime, updateTime }`
- `Category { id, name }`
- `Book { id, isbn, title, author, publisher, publishDate, categoryId, totalStock, availableStock, isDelete, ... }`
- `BorrowRecord { id, userId, bookId, borrowTime, returnTime, status }`

## 开发说明

- 逻辑删除字段为 `is_delete`，通过 MyBatis-Plus 配置生效。
- 分页基于 `Page<T>`。
- 当前密码哈希使用 Hutool `SecureUtil.md5`（可按需替换更安全算法）。
- JWT 秘钥位于 `sa-token.jwt-secret-key` 配置项。

## 测试

- 存在占位测试类：`LibraryManagementSystemApplicationTests`。

## 许可证

MIT
