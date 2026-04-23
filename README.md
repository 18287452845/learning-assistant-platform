# 基于SpringBoot与大语言模型的学习助手与问答平台

## 项目简介

本项目是一个面向高校学生的学习助手与智能问答平台，融合大语言模型与RAG技术，实现课程资源管理、精准智能问答、知识点梳理与个性化学习推荐功能。

## 技术架构

- **前端**：Vue 3 + Vite + Element Plus + ECharts
- **后端**：SpringBoot 3.2 + MyBatis-Plus + Spring Security + JWT
- **数据库**：MySQL 8.0 + Redis
- **AI服务**：大语言模型 + RAG检索增强生成

## 项目结构

```
学习助手问答平台/
├── 需求文档/
│   └── 需求规格说明书.md          # 详细需求分析
├── 架构设计/
│   └── 系统架构设计.md            # 技术架构与数据库设计
├── 数据库/
│   └── init.sql                  # 数据库数据库初始化脚本
├── 后端/
│   └── backend/                  # SpringBoot后端项目
│       ├── la-common/            # 公共模块
│       ├── la-domain/            # 领域模型
│       ├── la-infrastructure/    # 基础设施
│       ├── la-service/           # 业务服务
│       └── la-application/       # 启动应用
├── 前端/
│   ├── src/
│   │   ├── api/                  # API接口
│   │   ├── views/                # 页面组件
│   │   ├── stores/               # 状态管理
│   │   └── router/               # 路由配置
│   └── package.json
└── 测试/
    └── 测试报告.md                # 功能测试报告
```

## 核心功能

### 学生端
- 📚 课程浏览与资源下载
- 🤖 智能问答（全网问答+个人问答双板块对比）
- 📊 学习记录与错题本
- 📝 学习计划生成
- 👤 个人中心

### 教师端
- 📋 答疑请求处理
- 📈 学生学习统计
- 📊 高频问题分析

### 管理员端
- 👥 用户管理
- ✅ 资源审核
- 📊 系统监控
- 📝 操作日志

## 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Redis 7.0+

### 数据库初始化
```bash
mysql -u root -p < 数据库/init.sql
```

### 后端启动
```bash
cd 后端/backend
mvn clean install -DskipTests
cd la-application
mvn spring-boot:run
```

### 前端启动
```bash
cd 前端
npm install
npm run dev
```

### 访问地址
- 前端界面：http://localhost:3000
- 后端API：http://localhost:8080
- API文档：http://localhost:8080/swagger-ui.html

## 演示账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 学生 | 202011604100 | student123 |
| 教师 | 2020001 | teacher123 |
| 管理员 | admin | admin123 |

## 项目特色

### 1. 双板块智能问答
- **全网问答**：基于通用AI模型，提供标准化解答
- **个人问答**：结合学习记录，识别薄弱点，提供个性化推荐

### 2. 人脸登录
- 集成百度云人脸识别API
- 支持账号密码和人脸两种登录方式

### 3. 个性化学习
- 自动识别薄弱知识点
- 生成个性化学习计划
- 智能推荐学习资源

## 项目信息

- **学生姓名**：赵石林
- **学院**：信息工程与自动化学院
- **专业**：计算机科学与技术
- **指导教师**：杨云飞
- **完成时间**：2026年4月

## License

MIT
