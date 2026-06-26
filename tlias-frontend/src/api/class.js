/**
 * 班级管理模块
 * 提供班级的增删改查接口，用于管理教学班级信息
 */
import request from '../utils/request'

// 获取所有班级列表
export function getClasses() { return request({ url: '/classes', method: 'get' }) }
// 新增班级
export function addClass(data) { return request({ url: '/classes', method: 'post', data }) }
// 更新班级信息
export function updateClass(data) { return request({ url: '/classes', method: 'put', data }) }
// 根据ID删除班级
export function deleteClass(id) { return request({ url: `/classes/${id}`, method: 'delete' }) }
