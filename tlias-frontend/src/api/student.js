/**
 * 学员管理模块
 * 提供学员的增删改查接口，支持违纪处理、Excel批量导入导出
 */
import request from '../utils/request'

// 分页查询学员列表（支持条件筛选）
export function getStudents(params) { return request({ url: '/students', method: 'get', params }) }
// 新增学员
export function addStudent(data) { return request({ url: '/students', method: 'post', data }) }
// 更新学员信息
export function updateStudent(data) { return request({ url: '/students', method: 'put', data }) }
// 根据ID删除学员
export function deleteStudent(id) { return request({ url: `/students/${id}`, method: 'delete' }) }
// 对学员进行违纪扣分处理
export function addViolation(id, score) { return request({ url: `/students/${id}/violation`, method: 'post', data: { score } }) }
// 通过Excel文件批量导入学员数据
export function importStudents(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({ url: '/students/import', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}
// 导出学员数据为Excel文件（返回blob二进制数据）
export function exportStudents() { return request({ url: '/students/export', method: 'get', responseType: 'blob' }) }
