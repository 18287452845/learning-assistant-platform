import { createRouter, createWebHistory } from 'vue-router'
import { getToken, getUserRole } from '@/utils/auth'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginIndex.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/login/RegisterIndex.vue'),
    meta: { title: '注册', requiresAuth: false }
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/error/NotFound.vue'),
    meta: { title: '页面不存在' }
  },
  {
    path: '/403',
    name: 'Forbidden',
    component: () => import('@/views/error/Forbidden.vue'),
    meta: { title: '无权限访问' }
  },
  // 学生端
  {
    path: '/student',
    name: 'Student',
    component: () => import('@/views/student/StudentLayout.vue'),
    meta: { requiresAuth: true, roles: ['student'] },
    children: [
      { path: '', redirect: '/student/home' },
      { path: 'home', name: 'StudentHome', component: () => import('@/views/student/StudentHome.vue'), meta: { title: '首页' } },
      { path: 'courses', name: 'StudentCourses', component: () => import('@/views/student/StudentCourses.vue'), meta: { title: '我的课程' } },
      { path: 'course/:id', name: 'CourseDetail', component: () => import('@/views/student/CourseDetail.vue'), meta: { title: '课程详情' } },
      { path: 'qa', name: 'StudentQA', component: () => import('@/views/student/StudentQA.vue'), meta: { title: '智能问答' } },
      { path: 'qa/history', name: 'QAHistory', component: () => import('@/views/student/QAHistory.vue'), meta: { title: '问答历史' } },
      { path: 'profile', name: 'StudentProfile', component: () => import('@/views/student/StudentProfile.vue'), meta: { title: '个人中心' } },
      { path: 'wrong-questions', name: 'WrongQuestions', component: () => import('@/views/student/WrongQuestions.vue'), meta: { title: '错题本' } },
      { path: 'learning-plan', name: 'LearningPlan', component: () => import('@/views/student/LearningPlan.vue'), meta: { title: '学习计划' } }
    ]
  },
  // 教师端
  {
    path: '/teacher',
    name: 'Teacher',
    component: () => import('@/views/teacher/TeacherLayout.vue'),
    meta: { requiresAuth: true, roles: ['teacher'] },
    children: [
      { path: '', redirect: '/teacher/home' },
      { path: 'home', name: 'TeacherHome', component: () => import('@/views/teacher/TeacherHome.vue'), meta: { title: '首页' } },
      { path: 'qa', name: 'TeacherQA', component: () => import('@/views/teacher/TeacherQA.vue'), meta: { title: '答疑管理' } },
      { path: 'students', name: 'TeacherStudents', component: () => import('@/views/teacher/TeacherStudents.vue'), meta: { title: '学生管理' } },
      { path: 'stats', name: 'TeacherStats', component: () => import('@/views/teacher/TeacherStats.vue'), meta: { title: '统计分析' } },
      { path: 'profile', name: 'TeacherProfile', component: () => import('@/views/teacher/TeacherProfile.vue'), meta: { title: '个人中心' } }
    ]
  },
  // 管理员端
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/admin/AdminLayout.vue'),
    meta: { requiresAuth: true, roles: ['admin'] },
    children: [
      { path: '', redirect: '/admin/home' },
      { path: 'home', name: 'AdminHome', component: () => import('@/views/admin/AdminHome.vue'), meta: { title: '首页' } },
      { path: 'users', name: 'AdminUsers', component: () => import('@/views/admin/AdminUsers.vue'), meta: { title: '用户管理' } },
      { path: 'courses', name: 'AdminCourses', component: () => import('@/views/admin/AdminCourses.vue'), meta: { title: '课程管理' } },
      { path: 'review', name: 'AdminReview', component: () => import('@/views/admin/AdminReview.vue'), meta: { title: '资源审核' } },
      { path: 'monitor', name: 'AdminMonitor', component: () => import('@/views/admin/AdminMonitor.vue'), meta: { title: '系统监控' } },
      { path: 'profile', name: 'AdminProfile', component: () => import('@/views/admin/AdminProfile.vue'), meta: { title: '个人中心' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = `${to.meta.title || '页面'} - 智能学习助手`
  
  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    const token = getToken()
    const role = getUserRole()
    
    if (!token) {
      next('/login')
      return
    }
    
    // 检查角色权限
    if (to.meta.roles && !to.meta.roles.includes(role)) {
      next('/403')
      return
    }
  }
  
  next()
})

export default router
