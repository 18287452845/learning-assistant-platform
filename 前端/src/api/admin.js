import request from './index'

// 管理员相关API

/**
 * 获取用户列表
 * @param {Object} params
 */
export function getUserList(params) {
  return request.get('/admin/users', { params })
}

/**
 * 获取用户详情
 * @param {number} id
 */
export function getUserDetail(id) {
  return request.get(`/admin/users/${id}`)
}

/**
 * 创建用户
 * @param {Object} data
 */
export function createUser(data) {
  return request.post('/admin/users', data)
}

/**
 * 更新用户
 * @param {number} id
 * @param {Object} data
 */
export function updateUser(id, data) {
  return request.put(`/admin/users/${id}`, data)
}

/**
 * 删除用户
 * @param {number} id
 */
export function deleteUser(id) {
  return request.delete(`/admin/users/${id}`)
}

/**
 * 启用/禁用用户
 * @param {number} id
 * @param {boolean} enabled
 */
export function toggleUserStatus(id, enabled) {
  return request.post(`/admin/users/${id}/toggle-status`, { enabled })
}

/**
 * 重置用户密码
 * @param {number} id
 */
export function resetUserPassword(id) {
  return request.post(`/admin/users/${id}/reset-password`)
}

/**
 * 获取待审核资源列表
 * @param {Object} params
 */
export function getPendingReviews(params) {
  return request.get('/admin/reviews/pending', { params })
}

/**
 * 审核资源
 * @param {number} id
 * @param {boolean} approved
 * @param {string} reason
 */
export function reviewResource(id, approved, reason) {
  return request.post(`/admin/reviews/${id}`, { approved, reason })
}

/**
 * 获取系统统计
 */
export function getSystemStats() {
  return request.get('/admin/stats')
}

/**
 * 获取在线用户统计
 */
export function getOnlineUserStats() {
  return request.get('/admin/stats/online')
}

/**
 * 获取操作日志
 * @param {Object} params
 */
export function getOperationLogs(params) {
  return request.get('/admin/logs/operation', { params })
}

/**
 * 获取登录日志
 * @param {Object} params
 */
export function getLoginLogs(params) {
  return request.get('/admin/logs/login', { params })
}

/**
 * 获取系统状态
 */
export function getSystemStatus() {
  return request.get('/admin/system/status')
}

/**
 * 获取课程列表（管理员）
 * @param {Object} params
 */
export function getAdminCourseList(params) {
  return request.get('/admin/courses', { params })
}

/**
 * 创建课程
 * @param {Object} data
 */
export function createCourse(data) {
  return request.post('/admin/courses', data)
}

/**
 * 更新课程
 * @param {number} id
 * @param {Object} data
 */
export function updateCourse(id, data) {
  return request.put(`/admin/courses/${id}`, data)
}

/**
 * 删除课程
 * @param {number} id
 */
export function deleteCourse(id) {
  return request.delete(`/admin/courses/${id}`)
}

/**
 * 获取问答统计（管理员）
 * @param {Object} params
 */
export function getAdminQAStats(params) {
  return request.get('/admin/stats/qa', { params })
}
