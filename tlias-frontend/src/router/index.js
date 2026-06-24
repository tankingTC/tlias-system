/**
 * Vue Router 路由配置
 * 功能：
 * 1. 定义所有页面路由（登录、仪表盘、各管理模块）
 * 2. 路由守卫：未登录自动跳转登录页，已登录访问登录页自动跳首页
 */
import { createRouter, createWebHistory } from 'vue-router'

/**
 * 路由表配置
 * meta.requiresAuth: 是否需要登录认证（默认 true）
 * meta.title: 页面标题（用于面包屑显示）
 */
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false } // 登录页不需要认证
  },
  {
    path: '/',
    component: () => import('../layout/MainLayout.vue'),
    redirect: '/dashboard', // 根路径重定向到首页
    meta: { requiresAuth: true }, // 需要登录认证
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/dashboard/Dashboard.vue'), meta: { title: '首页' } },
      { path: 'dept', name: 'Dept', component: () => import('../views/dept/DeptManagement.vue'), meta: { title: '部门管理' } },
      { path: 'emp', name: 'Emp', component: () => import('../views/emp/EmpManagement.vue'), meta: { title: '员工管理' } },
      { path: 'class', name: 'Class', component: () => import('../views/class/ClassManagement.vue'), meta: { title: '班级管理' } },
      { path: 'student', name: 'Student', component: () => import('../views/student/StudentManagement.vue'), meta: { title: '学员管理' } },
      { path: 'exam', name: 'Exam', component: () => import('../views/exam/ExamManagement.vue'), meta: { title: '考试管理' } },
      { path: 'statistics', name: 'Statistics', component: () => import('../views/dashboard/Statistics.vue'), meta: { title: '数据统计' } },
      { path: 'job', name: 'Job', component: () => import('../views/job/JobManagement.vue'), meta: { title: '就业管理' } },
      { path: 'feedback', name: 'Feedback', component: () => import('../views/feedback/FeedbackManagement.vue'), meta: { title: '反馈管理' } },
      { path: 'log', name: 'Log', component: () => import('../views/log/OperationLog.vue'), meta: { title: '操作日志' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(), // 使用 HTML5 History 模式（无 # 号）
  routes
})

/**
 * 全局前置路由守卫
 * 每次路由跳转前检查：
 * - 需要认证的页面：无 Token 则跳转登录页
 * - 登录页：已有 Token 则跳转首页（避免重复登录）
 */
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth !== false && !token) {
    next('/login') // 无 Token，跳转登录
  } else if (to.path === '/login' && token) {
    next('/') // 已登录，跳转首页
  } else {
    next() // 放行
  }
})

export default router
