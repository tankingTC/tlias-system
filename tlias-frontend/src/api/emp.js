/**
 * 员工管理模块
 * 提供员工的增删改查接口，包括分页查询和头像上传
 */
import request from '../utils/request'

// 分页查询员工列表（支持条件筛选）
export function getEmps(params) { return request({ url: '/emps', method: 'get', params }) }
// 新增员工
export function addEmp(data) { return request({ url: '/emps', method: 'post', data }) }
// 更新员工信息
export function updateEmp(data) { return request({ url: '/emps', method: 'put', data }) }
// 根据ID删除员工
export function deleteEmp(id) { return request({ url: `/emps/${id}`, method: 'delete' }) }
// 上传文件（头像等），以FormData格式提交
export function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({ url: '/upload', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}
