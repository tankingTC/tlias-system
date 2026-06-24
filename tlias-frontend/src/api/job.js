/**
 * 就业管理模块
 * 提供就业记录的增删改查接口，用于跟踪学员就业情况
 */
import request from '../utils/request'

// 获取所有就业记录列表
export function getJobs() { return request({ url: '/jobs', method: 'get' }) }
// 根据ID获取就业记录详情
export function getJob(id) { return request({ url: `/jobs/${id}`, method: 'get' }) }
// 新增就业记录
export function addJob(data) { return request({ url: '/jobs', method: 'post', data }) }
// 更新就业记录
export function updateJob(data) { return request({ url: '/jobs', method: 'put', data }) }
// 根据ID删除就业记录
export function deleteJob(id) { return request({ url: `/jobs/${id}`, method: 'delete' }) }
