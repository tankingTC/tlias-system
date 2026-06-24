/**
 * 考试管理模块
 * 提供考试的增删改查接口，用于管理各类考试安排
 */
import request from '../utils/request'

// 获取所有考试列表
export function getExams() { return request({ url: '/exams', method: 'get' }) }
// 根据ID获取考试详情
export function getExam(id) { return request({ url: `/exams/${id}`, method: 'get' }) }
// 新增考试
export function addExam(data) { return request({ url: '/exams', method: 'post', data }) }
// 更新考试信息
export function updateExam(data) { return request({ url: '/exams', method: 'put', data }) }
// 根据ID删除考试
export function deleteExam(id) { return request({ url: `/exams/${id}`, method: 'delete' }) }
