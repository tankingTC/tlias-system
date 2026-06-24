/**
 * 用户认证模块
 * 提供登录、获取用户信息、退出登录等认证相关接口
 */
import request from '../utils/request'

// 用户登录（提交用户名和密码，返回Token）
export function login(data) { return request({ url: '/login', method: 'post', data }) }
// 获取当前登录用户信息（需要Token认证）
export function getUserInfo() { return request({ url: '/user/info', method: 'get' }) }
// 退出登录（清除服务端Token）
export function logout() { return request({ url: '/logout', method: 'post' }) }
