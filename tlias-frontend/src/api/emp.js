/**
 * 员工管理模块
 * 提供员工的增删改查接口，包括分页查询、头像上传和工作经历管理
 */
import request from '../utils/request'

// 分页查询员工列表（支持条件筛选）
export function getEmps(params) { return request({ url: '/emps', method: 'get', params }) }
// 根据ID获取员工详情
export function getEmp(id) { return request({ url: `/emps/${id}`, method: 'get' }) }
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
// 获取指定员工的工作经历列表
export function getWorkExperience(empId) { return request({ url: `/emps/${empId}/work-experience`, method: 'get' }) }
// 保存指定员工的工作经历
export function saveWorkExperience(empId, data) { return request({ url: `/emps/${empId}/work-experience`, method: 'post', data }) }
