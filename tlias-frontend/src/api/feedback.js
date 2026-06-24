/**
 * 反馈管理模块
 * 提供反馈的增删改查接口，用于管理学员和员工的反馈信息
 */
import request from '../utils/request'

// 分页查询反馈列表（支持条件筛选）
export function getFeedbacks(params) { return request({ url: '/feedbacks', method: 'get', params }) }
// 根据ID获取反馈详情
export function getFeedback(id) { return request({ url: `/feedbacks/${id}`, method: 'get' }) }
// 新增反馈
export function addFeedback(data) { return request({ url: '/feedbacks', method: 'post', data }) }
// 更新反馈信息
export function updateFeedback(data) { return request({ url: '/feedbacks', method: 'put', data }) }
// 根据ID删除反馈
export function deleteFeedback(id) { return request({ url: `/feedbacks/${id}`, method: 'delete' }) }
