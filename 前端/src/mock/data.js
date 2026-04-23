// 学习助手问答平台 - Mock数据

// Mock用户数据
export const mockUsers = {
  student: {
    id: '2021001234',
    username: 'student01',
    name: '张三',
    role: 'student',
    phone: '13812345678',
    email: 'zhangsan@student.edu.cn',
    avatar: '',
    class: '计算机2101班',
    major: '计算机科学与技术',
    college: '信息工程与自动化学院'
  },
  teacher: {
    id: 'T2020001',
    username: 'teacher01',
    name: '杨云飞',
    role: 'teacher',
    phone: '13912345678',
    email: 'yangyunfei@edu.cn',
    avatar: '',
    title: '副教授',
    college: '信息工程与自动化学院'
  },
  admin: {
    id: 'A001',
    username: 'admin',
    name: '系统管理员',
    role: 'admin',
    phone: '13712345678',
    email: 'admin@edu.cn',
    avatar: ''
  }
}

// Mock课程数据
export const mockCourses = [
  { id: 1, name: '数据结构与算法', teacher: '杨云飞', students: 156, resourceCount: 42, category: '专业课' },
  { id: 2, name: '计算机网络', teacher: '李明', students: 203, resourceCount: 38, category: '专业课' },
  { id: 3, name: '操作系统原理', teacher: '王芳', students: 178, resourceCount: 35, category: '专业课' },
  { id: 4, name: '数据库系统', teacher: '张伟', students: 165, resourceCount: 40, category: '专业课' },
  { id: 5, name: '软件工程', teacher: '刘静', students: 142, resourceCount: 33, category: '专业课' },
  { id: 6, name: '人工智能导论', teacher: '陈强', students: 198, resourceCount: 45, category: '选修课' },
  { id: 7, name: 'Python程序设计', teacher: '赵敏', students: 256, resourceCount: 28, category: '基础课' },
  { id: 8, name: 'Web开发技术', teacher: '孙伟', students: 189, resourceCount: 36, category: '专业课' }
]

// Mock问答数据
export const mockQuestions = [
  { id: 1, title: '如何理解二叉树的遍历算法？', course: '数据结构与算法', student: '张三', time: '2024-03-15 14:30', answers: 5, views: 128, status: 'answered' },
  { id: 2, title: 'TCP三次握手四次挥手详解', course: '计算机网络', student: '李四', time: '2024-03-15 10:20', answers: 8, views: 256, status: 'answered' },
  { id: 3, title: '进程与线程的区别是什么？', course: '操作系统原理', student: '王五', time: '2024-03-14 16:45', answers: 12, views: 342, status: 'answered' },
  { id: 4, title: 'SQL查询优化技巧有哪些？', course: '数据库系统', student: '赵六', time: '2024-03-14 09:15', answers: 6, views: 189, status: 'pending' },
  { id: 5, title: '什么是机器学习中的过拟合？', course: '人工智能导论', student: '钱七', time: '2024-03-13 20:00', answers: 15, views: 421, status: 'answered' },
  { id: 6, title: 'Python装饰器如何使用？', course: 'Python程序设计', student: '孙八', time: '2024-03-13 15:30', answers: 0, views: 45, status: 'pending' },
  { id: 7, title: 'Vue3响应式原理是什么？', course: 'Web开发技术', student: '周九', time: '2024-03-12 18:00', answers: 9, views: 312, status: 'answered' }
]

// Mock考试数据
export const mockExams = [
  { id: 1, title: '数据结构期中考试', course: '数据结构与算法', date: '2024-03-20', duration: 120, totalScore: 100, status: 'upcoming' },
  { id: 2, title: '计算机网络实验', course: '计算机网络', date: '2024-03-18', duration: 90, totalScore: 50, status: 'upcoming' },
  { id: 3, title: '操作系统期末模拟', course: '操作系统原理', date: '2024-03-10', duration: 150, totalScore: 100, status: 'completed' }
]

// Mock错题数据
export const mockWrongQuestions = [
  { id: 1, question: '关于快速排序的时间复杂度', subject: '数据结构与算法', errorCount: 3, lastTime: '2024-03-14', correctRate: 0.45 },
  { id: 2, question: 'TCP流量控制机制', subject: '计算机网络', errorCount: 2, lastTime: '2024-03-13', correctRate: 0.62 },
  { id: 3, question: '银行家算法理解', subject: '操作系统原理', errorCount: 4, lastTime: '2024-03-12', correctRate: 0.35 },
  { id: 4, question: '数据库事务隔离级别', subject: '数据库系统', errorCount: 1, lastTime: '2024-03-11', correctRate: 0.78 }
]

// Mock AI问答答案
export const mockAIAnswers = {
  general: {
    answer: `根据您的问题，这是一个涉及核心概念的问题。让我从以下几个方面为您详细解答：

## 1. 基本概念

该知识点是计算机科学中的基础概念，涉及到数据组织和管理的基本原理。

## 2. 工作原理

系统通过特定的算法和数据结构来实现高效的操作，主要包括以下几个步骤：
- 数据采集与预处理
- 算法分析与优化
- 结果验证与输出

## 3. 应用场景

这个概念在以下场景中有广泛应用：
- **软件开发**：作为程序设计的核心基础
- **系统架构**：影响整体设计决策
- **性能优化**：关键的性能瓶颈点

## 4. 注意事项

在实际应用中，需要注意以下几点：
1. 理解基本原理
2. 掌握实现细节
3. 注意性能影响

如果您有更多具体问题，欢迎继续提问！`,
    sources: ['教材第三章', '在线文档', '学术论文', '官方教程'],
    confidence: 0.92,
    relatedQuestions: ['相关概念的区别', '实际应用案例', '面试常见问题']
  },
  personalized: {
    answer: `根据您之前的学习记录，您在数据结构部分掌握较好，但在算法分析方面需要加强。

## 针对您的问题，结合您的学习进度：

### 1. 您已掌握 ✓
- 基本概念和定义
- 简单的应用场景
- 基础操作方法

### 2. 建议加强 ⚠️
- **算法实现细节**：需要多练习具体的代码实现
- **复杂度分析**：时间复杂度和空间复杂度的计算
- **边界条件处理**：特殊情况下的处理方式

### 3. 推荐学习资源 📚
基于您的学习情况，我为您推荐以下资源：
- 教材第3.2节：深度讲解核心概念
- 视频教程：第15-20集针对性讲解
- 练习题集：重点练习第5、8、12题

### 4. 学习建议 💡
建议您每天花30分钟复习相关概念，配合做题巩固效果会更好。`,
    sources: ['您的学习笔记', '个性化推荐课程', '您的错题记录', '历史问答记录'],
    confidence: 0.95,
    weakPoints: ['算法复杂度分析', '递归思想理解', '边界条件处理'],
    recommendations: ['算法专项训练', '递归函数练习', '复杂度分析专题']
  }
}

// Mock学生列表
export const mockStudentList = [
  { id: '2021001234', name: '张三', class: '计算机2101', questions: 15, answers: 28, lastActive: '2024-03-15', status: 'active' },
  { id: '2021001235', name: '李四', class: '计算机2101', questions: 8, answers: 12, lastActive: '2024-03-15', status: 'active' },
  { id: '2021001236', name: '王五', class: '计算机2102', questions: 22, answers: 45, lastActive: '2024-03-14', status: 'active' },
  { id: '2021001237', name: '赵六', class: '计算机2102', questions: 11, answers: 19, lastActive: '2024-03-14', status: 'inactive' },
  { id: '2021001238', name: '钱七', class: '计算机2103', questions: 6, answers: 8, lastActive: '2024-03-13', status: 'active' },
  { id: '2021001239', name: '孙八', class: '计算机2103', questions: 18, answers: 35, lastActive: '2024-03-12', status: 'inactive' }
]

// Mock统计数据
export const mockStatistics = {
  dailyActive: [
    { date: '03-09', students: 320 },
    { date: '03-10', students: 345 },
    { date: '03-11', students: 298 },
    { date: '03-12', students: 356 },
    { date: '03-13', students: 389 },
    { date: '03-14', students: 412 },
    { date: '03-15', students: 435 }
  ],
  questionTypes: [
    { name: '概念理解', value: 35 },
    { name: '算法实现', value: 28 },
    { name: '原理解释', value: 20 },
    { name: '实践应用', value: 12 },
    { name: '其他', value: 5 }
  ],
  highFrequencyQuestions: [
    { keyword: '递归', count: 156 },
    { keyword: '排序算法', count: 134 },
    { keyword: '数据结构', count: 121 },
    { keyword: '时间复杂度', count: 98 },
    { keyword: '图算法', count: 87 },
    { keyword: '动态规划', count: 76 },
    { keyword: '查找算法', count: 65 }
  ],
  courseStats: [
    { course: '数据结构', questions: 456, answers: 892 },
    { course: '计算机网络', questions: 389, answers: 756 },
    { course: '操作系统', questions: 298, answers: 567 },
    { course: '数据库', questions: 234, answers: 445 },
    { course: '软件工程', questions: 178, answers: 323 }
  ]
}

// Mock系统统计
export const mockSystemStats = {
  totalUsers: 1523,
  activeUsers: 892,
  totalQuestions: 5689,
  todayQuestions: 156,
  totalAnswers: 12456,
  avgResponseTime: 1.2,
  cpu: 45,
  memory: 62,
  disk: 38,
  onlineUsers: [
    { time: '08:00', count: 234 },
    { time: '10:00', count: 456 },
    { time: '12:00', count: 389 },
    { time: '14:00', count: 567 },
    { time: '16:00', count: 623 },
    { time: '18:00', count: 512 },
    { time: '20:00', count: 678 },
    { time: '22:00', count: 345 }
  ]
}

// Mock待审核资源
export const mockPendingReviews = [
  { id: 1, title: 'Python数据分析实战', type: 'PDF文档', submitter: '李明', submitTime: '2024-03-15', size: '15.2MB', status: 'pending' },
  { id: 2, title: '机器学习基础教程', type: '视频课程', submitter: '王芳', submitTime: '2024-03-14', size: '256MB', status: 'pending' },
  { id: 3, title: 'Web安全攻防实战', type: '实验文档', submitter: '张伟', submitTime: '2024-03-14', size: '8.5MB', status: 'pending' },
  { id: 4, title: '云计算技术概论', type: 'PDF文档', submitter: '刘静', submitTime: '2024-03-13', size: '12.8MB', status: 'pending' }
]

// Mock学习记录
export const mockLearningRecords = [
  { date: '2024-03-15', duration: 45, courses: ['数据结构', '算法'], questions: 3 },
  { date: '2024-03-14', duration: 60, courses: ['计算机网络'], questions: 5 },
  { date: '2024-03-13', duration: 30, courses: ['操作系统'], questions: 2 },
  { date: '2024-03-12', duration: 90, courses: ['数据库', '软件工程'], questions: 8 }
]

// Mock公告
export const mockAnnouncements = [
  { id: 1, title: '系统升级通知', content: '系统将于本周日凌晨2:00-6:00进行升级维护', time: '2024-03-15', important: true },
  { id: 2, title: '新增课程资源', content: '新增人工智能导论完整课件', time: '2024-03-14', important: false },
  { id: 3, title: '问答奖励机制', content: '回答问题可获得积分奖励', time: '2024-03-10', important: false }
]

// Mock操作日志
export const mockOperationLogs = [
  { id: 1, user: '张三', action: '登录系统', ip: '192.168.1.100', time: '2024-03-15 14:30:25' },
  { id: 2, user: '李四', action: '上传资源', ip: '192.168.1.101', time: '2024-03-15 14:28:10' },
  { id: 3, user: '王五', action: '提问', ip: '192.168.1.102', time: '2024-03-15 14:25:00' },
  { id: 4, user: '赵六', action: '回答问题', ip: '192.168.1.103', time: '2024-03-15 14:20:15' },
  { id: 5, user: '管理员', action: '审核资源', ip: '192.168.1.1', time: '2024-03-15 14:15:30' }
]

// Mock登录日志
export const mockLoginLogs = [
  { id: 1, username: 'student01', ip: '192.168.1.100', location: '校内网络', time: '2024-03-15 14:30:25', status: 'success' },
  { id: 2, username: 'teacher01', ip: '192.168.1.101', location: '校内网络', time: '2024-03-15 14:25:10', status: 'success' },
  { id: 3, username: 'unknown', ip: '202.96.1.100', location: '广东省广州市', time: '2024-03-15 14:20:00', status: 'failed' },
  { id: 4, username: 'admin', ip: '192.168.1.1', location: '校内网络', time: '2024-03-15 14:15:30', status: 'success' },
  { id: 5, username: 'student02', ip: '192.168.1.102', location: '校内网络', time: '2024-03-15 14:10:15', status: 'success' }
]

// Mock用户列表（管理员用）
export const mockUserList = [
  { id: 1, username: 'student01', name: '张三', role: '学生', phone: '13812345678', email: 'zhangsan@student.edu.cn', status: '正常', createTime: '2023-09-01', lastLogin: '2024-03-15' },
  { id: 2, username: 'student02', name: '李四', role: '学生', phone: '13812345679', email: 'lisi@student.edu.cn', status: '正常', createTime: '2023-09-01', lastLogin: '2024-03-15' },
  { id: 3, username: 'teacher01', name: '杨云飞', role: '教师', phone: '13912345678', email: 'yangyunfei@edu.cn', status: '正常', createTime: '2023-01-01', lastLogin: '2024-03-15' },
  { id: 4, username: 'admin', name: '系统管理员', role: '管理员', phone: '13712345678', email: 'admin@edu.cn', status: '正常', createTime: '2023-01-01', lastLogin: '2024-03-14' },
  { id: 5, username: 'student03', name: '王五', role: '学生', phone: '13812345680', email: 'wangwu@student.edu.cn', status: '禁用', createTime: '2023-09-01', lastLogin: '2024-03-10' }
]

// Mock课程详情
export const mockCourseDetail = {
  id: 1,
  name: '数据结构与算法',
  code: 'CS201',
  teacher: '杨云飞',
  teacherTitle: '副教授',
  description: '本课程主要介绍常用的数据结构（如线性表、栈、队列、树、图等）以及相关的算法设计和分析方法。通过本课程的学习，学生能够掌握基本的数据结构知识，并具备分析和设计算法的能力。',
  students: 156,
  resourceCount: 42,
  knowledgePoints: ['线性表', '栈和队列', '树和二叉树', '图', '查找算法', '排序算法'],
  recentResources: [
    { id: 1, title: '第三章 树和二叉树 课件', type: 'PPT', uploadTime: '2024-03-10' },
    { id: 2, title: '实验三 二叉树遍历', type: 'PDF', uploadTime: '2024-03-08' },
    { id: 3, title: '排序算法代码实现', type: 'ZIP', uploadTime: '2024-03-05' }
  ]
}

// Mock问答历史
export const mockQAHistory = [
  { id: 1, question: '快速排序的时间复杂度是多少？', answer: '快速排序的平均时间复杂度为O(n log n)，最坏情况下为O(n²)。', time: '2024-03-15 14:30', type: 'personal' },
  { id: 2, question: '二叉树和二叉搜索树的区别？', answer: '二叉树是一种每个节点最多有两个子节点的数据结构，而二叉搜索树是在二叉树基础上增加了左小右大的排序特性。', time: '2024-03-14 10:20', type: 'general' },
  { id: 3, question: '什么是递归算法的最优子结构？', answer: '最优子结构是指一个问题的最优解包含其子问题的最优解。这是动态规划和递归算法的重要特性。', time: '2024-03-13 16:45', type: 'personal' }
]
