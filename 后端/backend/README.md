# 学习助手与问答平台 - 后端系统

基于SpringBoot 3.2.x 与大语言模型的学习助手与问答平台后端系统。

---

## 📊 项目统计

| 模块 | Java文件 | 代码行数 | 说明 |
|------|----------|----------|------|
| la-common | 19 | 1351 | 公共模块 |
| la-domain | 24 | 1387 | 领域模型 |
| la-infrastructure | 18 | 849 | 基础设施 |
| la-service | 12 | 2233 | 业务服务 |
| la-application | 1 | 53 | 启动应用 |
| **总计** | **74** | **~5800** | - |

---

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 开发语言 |
| SpringBoot | 3.2.5 | 应用框架 |
| Spring Security | 6.2.x | 安全框架 |
| MyBatis-Plus | 3.5.6 | ORM框架 |
| Redis | 7.2.x | 缓存数据库 |
| MySQL | 8.0.x | 关系数据库 |
| JWT | 0.12.5 | Token认证 |
| Swagger/OpenAPI | 2.5.0 | API文档 |

## 项目结构

```
backend/
├── pom.xml                          # 父POM
├── la-common/                       # 公共模块
│   └── src/main/java/com/learning/common/
│       ├── constant/                # 常量定义
│       ├── enums/                   # 枚举类
│       ├── exception/               # 异常处理
│       ├── result/                  # 统一返回
│       └── utils/                   # 工具类
├── la-domain/                       # 领域模型
│   └── src/main/java/com/learning/domain/
│       ├── entity/                  # 实体类(9个)
│       ├── dto/                     # 数据传输对象(10个)
│       └── vo/                      # 视图对象(5个)
├── la-infrastructure/               # 基础设施
│   └── src/main/java/com/learning/infra/
│       ├── config/                  # 配置类(6个)
│       ├── security/                # 安全配置(3个)
│       └── mapper/                  # Mapper接口(8个)
├── la-service/                      # 业务服务
│   └── src/main/java/com/learning/service/
│       ├── controller/              # 控制器(6个)
│       └── service/                 # 服务类(5个)
└── la-application/                  # 应用启动
    └── src/main/
        ├── java/com/learning/
        │   └── LearningAssistantApplication.java
        └── resources/
            ├── application.yml
            ├── application-dev.yml
            └── application-prod.yml
```

## 功能模块

### 1. 用户认证模块
- ✅ 用户注册（支持学生、教师）
- ✅ 账号密码登录
- ✅ JWT Token认证
- ✅ 人脸登录（Mock百度云API）
- ✅ 图形验证码

### 2. 用户管理模块
- ✅ 获取当前用户信息
- ✅ 修改个人信息
- ✅ 修改密码
- ✅ 上传头像
- ✅ 用户列表查询（管理员）
- ✅ 启用/禁用用户（管理员）
- ✅ 重置密码（管理员）

### 3. 课程资源模块
- ✅ 课程列表查询
- ✅ 课程详情
- ✅ 资源上传
- ✅ 资源列表查询
- ✅ 资源审核（管理员）
- ✅ 资源下载

### 4. 智能问答模块
- ✅ 全网问答（Mock LLM）
- ✅ 个人问答（Mock LLM + RAG）
- ✅ 问答历史记录
- ✅ 知识点列表
- ✅ 学习计划生成（Mock）

### 5. 教学辅助模块
- ✅ 人工答疑请求
- ✅ 答疑处理（教师）

## API接口

### 认证接口
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 用户注册 | POST | /api/auth/register | 注册账号 |
| 用户登录 | POST | /api/auth/login | 账号密码登录 |
| 人脸登录 | POST | /api/auth/face-login | 人脸登录 |
| 绑定人脸 | POST | /api/auth/bind-face | 绑定人脸 |
| 生成验证码 | GET | /api/auth/captcha | 获取图形验证码 |
| 退出登录 | POST | /api/auth/logout | 退出登录 |

### 用户接口
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 获取用户信息 | GET | /api/user/info | 获取当前用户 |
| 修改个人信息 | PUT | /api/user/info | 修改信息 |
| 修改密码 | PUT | /api/user/password | 修改密码 |
| 上传头像 | POST | /api/user/avatar | 上传头像 |
| 用户列表 | GET | /api/user/list | 用户列表(管理员) |

### 课程资源接口
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 课程列表 | GET | /api/courses | 课程列表 |
| 课程详情 | GET | /api/courses/{id} | 课程详情 |
| 资源列表 | GET | /api/resources | 资源列表 |
| 上传资源 | POST | /api/resources | 上传资源 |
| 删除资源 | DELETE | /api/resources/{id} | 删除资源 |
| 待审核资源 | GET | /api/admin/resources/pending | 审核列表 |
| 审核资源 | PUT | /api/admin/resources/{id}/approve | 审核资源 |

### 智能问答接口
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 智能问答 | POST | /api/qa/ask | 提交问题 |
| 问答历史 | GET | /api/qa/history | 历史记录 |
| 问答详情 | GET | /api/qa/{id} | 问答详情 |
| 知识点列表 | GET | /api/qa/knowledge | 课程知识点 |
| 生成学习计划 | POST | /api/qa/plan | 学习计划 |

## 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 7.2+

## 快速开始

### 1. 环境准备

```bash
# 安装 JDK 17
# 安装 Maven 3.8+
# 安装 MySQL 8.0+
# 安装 Redis 7.2+
```

### 2. 数据库初始化

```bash
# 创建数据库
mysql -u root -p < ../../数据库/init.sql
```

### 3. 项目构建

```bash
cd backend

# 编译项目
mvn clean compile

# 打包项目
mvn clean package -DskipTests
```

### 4. 运行项目

```bash
# 开发环境运行
cd la-application
mvn spring-boot:run

# 或者打包后运行
java -jar target/la-application-1.0.0.jar --spring.profiles.active=dev
```

### 5. 访问API文档

启动后访问：http://localhost:8080/swagger-ui.html

## 配置说明

### 开发环境配置 (application-dev.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/learning_assistant
    username: root
    password: root123
  redis:
    host: localhost
    port: 6379

jwt:
  secret: learning-assistant-jwt-secret-key-2024
  expiration: 86400000  # 24小时
```

### 生产环境配置

使用环境变量或配置中心管理敏感信息：
- `DB_HOST`: 数据库地址
- `DB_PORT`: 数据库端口
- `DB_USERNAME`: 数据库用户名
- `DB_PASSWORD`: 数据库密码
- `REDIS_HOST`: Redis地址
- `REDIS_PASSWORD`: Redis密码
- `JWT_SECRET`: JWT密钥（必须修改）

## 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 教师 | 2020001 | teacher123 |
| 学生 | 202011604100 | student123 |

## 项目特点

1. **微服务架构**：采用模块化设计，低耦合高内聚
2. **统一返回格式**：所有接口返回统一的Result<T>格式
3. **统一异常处理**：全局异常处理器，提供友好的错误提示
4. **JWT认证**：无状态认证，支持Token刷新
5. **BCrypt密码加密**：安全的密码存储
6. **RBAC权限控制**：基于角色的权限管理
7. **Swagger文档**：完善的API文档
8. **Mock实现**：人脸识别和LLM调用采用Mock实现，便于开发和测试

## 注意事项

1. 人脸登录功能为Mock实现，实际使用时需对接百度云API
2. 智能问答功能为Mock实现，实际使用时需对接大语言模型API
3. 文件上传采用本地存储，实际部署时建议使用OSS
4. 生产环境请务必修改JWT密钥
5. 请定期备份数据库

## 许可证

本项目采用 Apache 2.0 许可证
