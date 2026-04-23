# 智能学习助手 - 前端项目

基于 Vue 3 + Vite + Element Plus 的智能学习助手问答平台前端系统。

## 项目介绍

这是一个基于 SpringBoot 与大语言模型的学习助手与问答平台，提供：

- **学生端**：课程学习、智能问答、个人学习管理
- **教师端**：答疑管理、学生管理、数据统计分析
- **管理员端**：用户管理、资源审核、系统监控

## 技术栈

- **框架**：Vue 3.4
- **构建工具**：Vite 5
- **UI 组件库**：Element Plus 2.6
- **状态管理**：Pinia
- **路由**：Vue Router 4
- **图表**：ECharts 5
- **HTTP 客户端**：Axios
- **样式**：SCSS

## 功能特性

### 认证模块
- 账号密码登录
- 人脸登录（模拟）
- 用户注册（学生/教师）
- Token 管理
- 路由权限控制

### 学生端
- 首页数据统计
- 课程列表浏览
- 课程详情页
- 智能问答（双板块对比）
- 问答历史记录
- 错题本
- 学习计划

### 教师端
- 首页数据概览
- 答疑问题管理
- 学生列表管理
- 统计分析图表

### 管理员端
- 系统数据概览
- 用户管理（CRUD、启用/禁用）
- 资源审核
- 系统监控（状态、日志）

## 项目结构

```
frontend/
├── public/
│   └── favicon.svg
├── src/
│   ├── api/                    # API 接口封装
│   │   ├── index.js           # axios 实例
│   │   ├── auth.js            # 认证接口
│   │   ├── user.js            # 用户接口
│   │   ├── course.js          # 课程接口
│   │   ├── qa.js              # 问答接口
│   │   └── admin.js           # 管理员接口
│   ├── assets/
│   │   ├── images/            # 图片资源
│   │   └── styles/            # 全局样式
│   ├── components/             # 公共组件
│   │   ├── Breadcrumb.vue
│   │   └── Pagination.vue
│   ├── hooks/                  # 组合式函数
│   ├── mock/                   # Mock 数据
│   │   └── data.js
│   ├── router/                 # 路由配置
│   │   └── index.js
│   ├── stores/                 # 状态管理
│   │   ├── user.js
│   │   └── app.js
│   ├── utils/                  # 工具函数
│   │   ├── request.js          # 请求封装
│   │   ├── auth.js             # Token 管理
│   │   └── validate.js         # 表单验证
│   ├── views/                  # 页面组件
│   │   ├── login/
│   │   ├── student/
│   │   ├── teacher/
│   │   ├── admin/
│   │   └── error/
│   ├── App.vue
│   └── main.js
├── index.html
├── package.json
├── vite.config.js
└── README.md
```

## 快速开始

### 安装依赖

```bash
npm install
```

### 开发环境

```bash
npm run dev
```

项目将在 `http://localhost:3000` 启动。

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

## 演示账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 学生 | student01 | 任意密码 |
| 教师 | teacher01 | 任意密码 |
| 管理员 | admin | 任意密码 |

## Mock 数据

项目使用 Mock 数据进行演示，无需后端服务即可运行所有功能。

如需对接真实后端接口，修改 `vite.config.js` 中的代理配置：

```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',  // 后端地址
    changeOrigin: true
  }
}
```

## 接口对接

API 接口封装在 `src/api/` 目录下，与后端接口一一对应：

- `auth.js` - 认证接口（登录、注册、登出）
- `user.js` - 用户接口（信息、修改密码）
- `course.js` - 课程接口（列表、详情、资源）
- `qa.js` - 问答接口（提问、历史）
- `admin.js` - 管理员接口（用户、资源、监控）

## 路由权限

系统采用基于角色的权限控制：

- 学生：`/student/*`
- 教师：`/teacher/*`
- 管理员：`/admin/*`

未登录用户自动跳转登录页，无权限访问跳转 403 页面。

## 开发说明

### 添加新页面

1. 在 `src/views/` 下创建页面组件
2. 在 `src/router/index.js` 中添加路由配置
3. 在对应角色 Layout 组件中添加菜单项

### 添加 API 接口

1. 在 `src/api/` 下对应的文件中添加接口函数
2. 使用 `request.js` 中的 axios 实例发送请求

### 添加组件

1. 在 `src/components/` 下创建组件
2. 在需要使用的页面中导入

## 许可证

MIT License
