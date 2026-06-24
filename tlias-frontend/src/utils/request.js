/**
 * Axios 请求封装模块
 * 功能：
 * 1. 统一设置请求基础路径和超时时间
 * 2. 请求拦截器：自动携带 JWT Token
 * 3. 响应拦截器：统一处理成功/失败响应，401 自动跳转登录
 */
import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

// 创建 Axios 实例，基础路径 /api 由 Vite 代理转发到后端
const request = axios.create({
  baseURL: '/api',
  timeout: 10000 // 请求超时时间 10 秒
})

/**
 * 请求拦截器
 * 每次发送请求前，从 localStorage 读取 Token 并添加到请求头
 */
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['token'] = token // 自定义 token 头，非标准 Authorization
    }
    return config
  },
  error => Promise.reject(error)
)

/**
 * 响应拦截器
 * 统一处理后端返回的 Result 对象 {code, message, data}
 */
let isRedirecting = false // 防止并发 401 请求重复弹窗

request.interceptors.response.use(
  response => {
    // 文件下载（blob 类型）直接返回原始响应，不解析 JSON
    if (response.config.responseType === 'blob') {
      return response
    }
    // 解析业务响应
    const res = response.data
    if (res.code === 200) {
      return res // 成功：返回 {code, message, data}
    } else {
      // 业务错误：弹出错误提示
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message))
    }
  },
  error => {
    // HTTP 错误处理
    if (error.response && error.response.status === 401) {
      // Token 过期或无效：使用标志位防止多个并发请求重复弹窗
      if (!isRedirecting) {
        isRedirecting = true
        ElMessage.error('登录已过期，请重新登录')
        localStorage.removeItem('token')
        router.push('/login')
        setTimeout(() => { isRedirecting = false }, 2000) // 2 秒后重置标志
      }
    } else {
      // 其他网络错误
      ElMessage.error(error.message || '网络异常')
    }
    return Promise.reject(error)
  }
)

export default request
