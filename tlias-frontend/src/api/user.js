/**
 * 用户认证模块
 * 提供获取用户信息接口
 */
import request from '../utils/request'

// 获取当前登录用户信息（需要Token认证）
export function getUserInfo() { return request({ url: '/user/info', method: 'get' }) }
