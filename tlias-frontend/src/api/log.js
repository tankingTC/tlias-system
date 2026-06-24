/**
 * 操作日志模块
 * 提供系统操作日志的查询接口，用于审计追踪用户行为
 */
import request from '../utils/request'

// 分页查询操作日志列表（支持条件筛选）
export function getLogs(params) { return request({ url: '/logs', method: 'get', params }) }
