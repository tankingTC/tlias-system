/**
 * 部门管理模块
 * 提供部门的增删改查接口，用于维护组织架构信息
 */
import request from '../utils/request'

// 获取所有部门列表
export function getDepts() { return request({ url: '/depts', method: 'get' }) }
// 新增部门
export function addDept(data) { return request({ url: '/depts', method: 'post', data }) }
// 更新部门信息
export function updateDept(data) { return request({ url: '/depts', method: 'put', data }) }
// 根据ID删除部门
export function deleteDept(id) { return request({ url: `/depts/${id}`, method: 'delete' }) }
