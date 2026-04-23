-- =====================================================
-- 学习助手与问答平台 - 数据库初始化脚本
-- 数据库: MySQL 8.0
-- 字符集: utf8mb4
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `learning_assistant` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `learning_assistant`;

-- =====================================================
-- 1. 用户与权限相关表
-- =====================================================

-- 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_key` varchar(50) NOT NULL COMMENT '角色标识(STUDENT/TEACHER/ADMIN)',
  `description` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `status` tinyint DEFAULT 1 COMMENT '状态(0禁用 1启用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 初始化角色
INSERT INTO `sys_role` (`role_name`, `role_key`, `description`) VALUES 
('学生', 'STUDENT', '普通学生用户，可使用学习资源、智能问答等功能'),
('教师', 'TEACHER', '教师用户，可处理学生答疑、查看统计数据'),
('管理员', 'ADMIN', '系统管理员，拥有所有权限');

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名(学号/工号)',
  `password` varchar(100) NOT NULL COMMENT '密码(BCrypt加密)',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `gender` tinyint DEFAULT 0 COMMENT '性别(0未知 1男 2女)',
  `face_token` varchar(100) DEFAULT NULL COMMENT '百度云人脸特征Token',
  `face_registered` tinyint DEFAULT 0 COMMENT '人脸是否已注册(0否 1是)',
  `status` tinyint DEFAULT 1 COMMENT '状态(0禁用 1启用)',
  `login_count` int DEFAULT 0 COMMENT '登录次数',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标记(0正常 1已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- =====================================================
-- 2. 课程与资源相关表
-- =====================================================

-- 课程分类表
DROP TABLE IF EXISTS `course_category`;
CREATE TABLE `course_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `parent_id` bigint DEFAULT 0 COMMENT '父分类ID',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态(0禁用 1启用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程分类表';

-- 初始化分类
INSERT INTO `course_category` (`name`, `parent_id`, `sort_order`) VALUES 
('计算机科学', 0, 1),
('数学', 0, 2),
('物理', 0, 3),
('英语', 0, 4);

-- 课程表
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `course_name` varchar(100) NOT NULL COMMENT '课程名称',
  `course_code` varchar(50) DEFAULT NULL COMMENT '课程代码',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `teacher_id` bigint DEFAULT NULL COMMENT '教师ID',
  `teacher_name` varchar(50) DEFAULT NULL COMMENT '教师姓名',
  `description` text COMMENT '课程描述',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图片URL',
  `credit` decimal(3,1) DEFAULT NULL COMMENT '学分',
  `student_count` int DEFAULT 0 COMMENT '学生数量',
  `resource_count` int DEFAULT 0 COMMENT '资源数量',
  `status` tinyint DEFAULT 1 COMMENT '状态(0下架 1上架)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_code` (`course_code`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 课程资源表
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `title` varchar(200) NOT NULL COMMENT '资源标题',
  `description` text COMMENT '资源描述',
  `file_path` varchar(500) NOT NULL COMMENT '文件存储路径',
  `file_name` varchar(200) NOT NULL COMMENT '原始文件名',
  `file_type` varchar(20) NOT NULL COMMENT '文件类型(pdf/doc/docx/ppt/pptx)',
  `file_size` bigint DEFAULT 0 COMMENT '文件大小(字节)',
  `parsed_content` longtext COMMENT '解析后的文本内容',
  `parse_status` tinyint DEFAULT 0 COMMENT '解析状态(0未解析 1解析中 2已完成 3失败)',
  `vector_status` tinyint DEFAULT 0 COMMENT '向量化状态(0未处理 1处理中 2完成)',
  `download_count` int DEFAULT 0 COMMENT '下载次数',
  `status` tinyint DEFAULT 0 COMMENT '审核状态(0待审核 1已通过 2已驳回)',
  `reject_reason` varchar(500) DEFAULT NULL COMMENT '驳回原因',
  `uploader_id` bigint NOT NULL COMMENT '上传者ID',
  `uploader_name` varchar(50) DEFAULT NULL COMMENT '上传者姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_uploader_id` (`uploader_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程资源表';

-- 知识点表
DROP TABLE IF EXISTS `knowledge_point`;
CREATE TABLE `knowledge_point` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '知识点ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `title` varchar(200) NOT NULL COMMENT '知识点标题',
  `content` text COMMENT '知识点内容',
  `keywords` varchar(500) DEFAULT NULL COMMENT '关键词(JSON数组)',
  `parent_id` bigint DEFAULT NULL COMMENT '父知识点ID',
  `level` int DEFAULT 1 COMMENT '层级',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `importance` tinyint DEFAULT 1 COMMENT '重要程度(1一般 2重要 3核心)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识点表';

-- =====================================================
-- 3. 学习与问答相关表
-- =====================================================

-- 学习记录表
DROP TABLE IF EXISTS `learning_record`;
CREATE TABLE `learning_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `resource_id` bigint DEFAULT NULL COMMENT '资源ID',
  `knowledge_id` bigint DEFAULT NULL COMMENT '知识点ID',
  `study_duration` int DEFAULT 0 COMMENT '学习时长(秒)',
  `progress` int DEFAULT 0 COMMENT '学习进度(0-100)',
  `score` decimal(5,2) DEFAULT NULL COMMENT '得分',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习记录表';

-- 问答记录表
DROP TABLE IF EXISTS `question_record`;
CREATE TABLE `question_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户姓名',
  `course_id` bigint DEFAULT NULL COMMENT '课程ID',
  `course_name` varchar(100) DEFAULT NULL COMMENT '课程名称',
  `question` text NOT NULL COMMENT '问题内容',
  `global_answer` longtext COMMENT '全网问答答案',
  `personal_answer` longtext COMMENT '个人问答答案',
  `weak_points` text DEFAULT NULL COMMENT '识别的薄弱知识点(JSON)',
  `recommendations` text DEFAULT NULL COMMENT '推荐学习资源(JSON)',
  `answer_type` tinyint DEFAULT 1 COMMENT '答案类型(1AI生成 2人工回答)',
  `helpful` tinyint DEFAULT NULL COMMENT '是否有帮助(0否 1是)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问答记录表';

-- 错题本表
DROP TABLE IF EXISTS `wrong_question`;
CREATE TABLE `wrong_question` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '错题ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `knowledge_id` bigint DEFAULT NULL COMMENT '知识点ID',
  `question` text NOT NULL COMMENT '题目内容',
  `user_answer` text COMMENT '用户答案',
  `correct_answer` text NOT NULL COMMENT '正确答案',
  `analysis` text COMMENT '解析说明',
  `question_type` tinyint DEFAULT 1 COMMENT '题目类型(1选择题 2填空题 3简答题)',
  `difficulty` tinyint DEFAULT 2 COMMENT '难度(1简单 2中等 3困难)',
  `mastery_level` tinyint DEFAULT 0 COMMENT '掌握程度(0未掌握 1部分掌握 2已掌握)',
  `review_count` int DEFAULT 0 COMMENT '复习次数',
  `last_review_time` datetime DEFAULT NULL COMMENT '最后复习时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='错题本表';

-- =====================================================
-- 4. 教学辅助相关表
-- =====================================================

-- 答疑请求表
DROP TABLE IF EXISTS `qa_request`;
CREATE TABLE `qa_request` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '请求ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `student_name` varchar(50) DEFAULT NULL COMMENT '学生姓名',
  `teacher_id` bigint DEFAULT NULL COMMENT '教师ID',
  `teacher_name` varchar(50) DEFAULT NULL COMMENT '教师姓名',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `course_name` varchar(100) DEFAULT NULL COMMENT '课程名称',
  `question` text NOT NULL COMMENT '问题内容',
  `answer` text COMMENT '回答内容',
  `status` tinyint DEFAULT 0 COMMENT '状态(0待处理 1已回答 2已关闭)',
  `priority` tinyint DEFAULT 2 COMMENT '优先级(1高 2中 3低)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `answer_time` datetime DEFAULT NULL COMMENT '回答时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='答疑请求表';

-- 学习计划表
DROP TABLE IF EXISTS `study_plan`;
CREATE TABLE `study_plan` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '计划ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `plan_name` varchar(100) NOT NULL COMMENT '计划名称',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `daily_hours` decimal(3,1) DEFAULT 2.0 COMMENT '每日学习时长(小时)',
  `target_score` decimal(5,2) DEFAULT NULL COMMENT '目标分数',
  `plan_content` text COMMENT '计划详情(JSON)',
  `progress` int DEFAULT 0 COMMENT '完成进度(0-100)',
  `status` tinyint DEFAULT 1 COMMENT '状态(0暂停 1进行中 2已完成)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习计划表';

-- =====================================================
-- 5. 系统管理相关表
-- =====================================================

-- 操作日志表
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(100) NOT NULL COMMENT '操作描述',
  `module` varchar(50) DEFAULT NULL COMMENT '操作模块',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `request_url` varchar(255) DEFAULT NULL COMMENT '请求URL',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方式',
  `request_params` text COMMENT '请求参数',
  `response_result` text COMMENT '响应结果',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `location` varchar(100) DEFAULT NULL COMMENT '操作地点',
  `status` tinyint DEFAULT 1 COMMENT '状态(0失败 1成功)',
  `error_msg` text COMMENT '错误信息',
  `duration` bigint DEFAULT 0 COMMENT '执行时长(ms)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 登录日志表
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `login_type` varchar(20) DEFAULT 'password' COMMENT '登录方式(password/face)',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `location` varchar(100) DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(50) DEFAULT NULL COMMENT '浏览器',
  `os` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `status` tinyint DEFAULT 1 COMMENT '状态(0失败 1成功)',
  `message` varchar(200) DEFAULT NULL COMMENT '提示消息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- =====================================================
-- 6. 初始化测试数据
-- =====================================================

-- 初始化管理员账号 (密码: admin123, BCrypt加密)
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `email`, `gender`, `status`) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt.4vGC', '系统管理员', '13800000000', 'admin@example.com', 1, 1);

-- 分配管理员角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 3);

-- 初始化教师账号 (密码: teacher123)
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `email`, `gender`, `status`) VALUES 
('2020001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt.4vGC', '张老师', '13800000001', 'zhang@example.com', 1, 1),
('2020002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt.4vGC', '李老师', '13800000002', 'li@example.com', 2, 1);

-- 分配教师角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (2, 2), (3, 2);

-- 初始化学生账号 (密码: student123)
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `email`, `gender`, `status`) VALUES 
('202011604100', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt.4vGC', '王明', '13900000001', 'wangming@example.com', 1, 1),
('202011604101', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt.4vGC', '李华', '13900000002', 'lihua@example.com', 2, 1),
('202011604114', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt.4vGC', '赵石林', '13900000003', 'zhaoshilin@example.com', 1, 1);

-- 分配学生角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (4, 1), (5, 1), (6, 1);

-- 初始化课程
INSERT INTO `course` (`course_name`, `course_code`, `category_id`, `teacher_id`, `teacher_name`, `description`, `credit`, `status`) VALUES 
('数据结构', 'CS101', 1, 2, '张老师', '数据结构是计算机科学的核心课程，主要内容包括线性表、树、图等基本数据结构及其操作。', 4.0, 1),
('操作系统', 'CS102', 1, 3, '李老师', '操作系统课程介绍操作系统的基本原理，包括进程管理、内存管理、文件系统等。', 4.0, 1),
('数据库原理', 'CS103', 1, 2, '张老师', '数据库原理课程讲解关系数据库理论、SQL语言、数据库设计等内容。', 3.0, 1);

-- 初始化知识点
INSERT INTO `knowledge_point` (`course_id`, `title`, `content`, `parent_id`, `level`, `sort_order`, `importance`) VALUES 
(1, '线性表', '线性表是最基本的数据结构，包括顺序表和链表两种存储方式。', NULL, 1, 1, 3),
(1, '栈和队列', '栈是后进先出的线性表，队列是先进先出的线性表。', NULL, 1, 2, 3),
(1, '树与二叉树', '树是一种非线性数据结构，二叉树是每个节点最多有两个子树的树结构。', NULL, 1, 3, 3),
(1, '图', '图是由顶点和边组成的非线性数据结构，用于表示多对多的关系。', NULL, 1, 4, 3),
(2, '进程管理', '进程是程序在计算机上的一次执行过程，包括进程控制、同步与互斥等内容。', NULL, 1, 1, 3),
(2, '内存管理', '内存管理包括连续分配、分页、分段等内存管理技术。', NULL, 1, 2, 3),
(3, 'SQL语言', 'SQL是结构化查询语言，用于数据库的数据定义、查询、更新和控制。', NULL, 1, 1, 3);

-- =====================================================
-- 7. 创建视图
-- =====================================================

-- 用户详情视图
CREATE OR REPLACE VIEW `v_user_detail` AS
SELECT u.id, u.username, u.real_name, u.phone, u.email, u.avatar, u.gender,
       u.status, u.create_time, GROUP_CONCAT(r.role_name) as roles
FROM sys_user u
LEFT JOIN sys_user_role ur ON u.id = ur.user_id
LEFT JOIN sys_role r ON ur.role_id = r.id
WHERE u.deleted = 0
GROUP BY u.id;

-- 课程统计视图
CREATE OR REPLACE VIEW `v_course_stats` AS
SELECT c.id, c.course_name, c.teacher_name, c.credit,
       COUNT(DISTINCT r.id) as resource_count,
       COUNT(DISTINCT l.user_id) as student_count,
       COUNT(DISTINCT q.id) as question_count
FROM course c
LEFT JOIN resource r ON c.id = r.course_id AND r.status = 1
LEFT JOIN learning_record l ON c.id = l.course_id
LEFT JOIN question_record q ON c.id = q.course_id
WHERE c.status = 1
GROUP BY c.id;

-- =====================================================
-- 完成
-- =====================================================
SELECT '数据库初始化完成!' as message;
