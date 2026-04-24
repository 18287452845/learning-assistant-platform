import request from '@/utils/request'

// 管理员相关API - 路径对齐后端Controller

/**
 * 获取用户列表
 * @param {Object} params { keyword, status, page, size }
 */
export function getUserList(params) {
  return request.get('/user/list', { params })
}

/**
 * 获取用户详情
 * @param {number} id
 */
export function getUserDetail(id) {
  return request.get(`/user/${id}`)
}

/**
 * 启用/禁用用户
 * @param {number} id
 * @param {number} status 0=禁用 1=启用
 */
export function toggleUserStatus(id, status) {
  return request.put(`/user/${id}/status`, null, { params: { status } })
}

/**
 * 重置用户密码
 * @param {number} id
 */
export function resetUserPassword(id) {
  return request.post(`/user/${id}/reset-password`)
}

/**
 * 获取待审核资源列表
 * @param {Object} params { page, size }
 */
export function getPendingReviews(params) {
  return request.get('/admin/resources/pending', { params })
}

/**
 * 审核资源
 * @param {number} id
 * @param {number} status 1=通过 2=驳回
 * @param {string} rejectReason 驳回原因
 */
export function reviewResource(id, status, rejectReason) {
  return request.put(`/admin/resources/${id}/approve`, null, {
    params: { status, rejectReason }
  })
}

/**
 * 获取系统统计
 */
export function getSystemStats() {
  return request.get('/admin/statistics')
}

/**
 * 获取用户活跃度统计
 */
export function getUserActivityStats() {
  return request.get('/admin/statistics/user-activity')
}

/**
 * 获取操作日志
 * @param {Object} params { keyword, page, size }
 */
export function getOperationLogs(params) {
  return request.get('/admin/logs/operation', { params })
}

/**
 * 获取登录日志
 * @param {Object} params { keyword, page, size }
 */
export function getLoginLogs(params) {
  return request.get('/admin/logs/login', { params })
}

/**
 * 获取系统状态
 */
export function getSystemStatus() {
  return request.get('/admin/health')
}

/**
 * 获取课程列表
 * @param {Object} params
 */
export function getAdminCourseList(params) {
  return request.get('/courses', { params })
}

/**
 * 获取问答统计
 */
export function getAdminQAStats() {
  return request.get('/admin/statistics/questions')
}
