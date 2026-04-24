import request from '@/utils/request'

// 课程相关API

/**
 * 获取课程列表
 * @param {Object} params { keyword, categoryId, teacherId, status, page, size }
 */
export function getCourseList(params) {
  return request.get('/courses', { params })
}

/**
 * 获取课程详情
 * @param {number} id
 */
export function getCourseDetail(id) {
  return request.get(`/courses/${id}`)
}

/**
 * 获取课程资源列表
 * @param {number} courseId
 * @param {Object} params
 */
export function getCourseResources(courseId, params) {
  return request.get('/resources', { params: { courseId, ...params } })
}

/**
 * 获取课程知识点
 * @param {number} courseId
 */
export function getCourseKnowledgePoints(courseId) {
  return request.get('/qa/knowledge', { params: { courseId } })
}

/**
 * 获取我的课程（复用课程列表接口）
 */
export function getMyCourses() {
  return request.get('/courses', { params: { page: 1, size: 100 } })
}

/**
 * 上传资源
 * @param {Object} data FormData with courseId, title, description, file
 */
export function uploadResource(data) {
  return request.post('/resources', data, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 获取资源详情
 * @param {number} resourceId
 */
export function getResourceDetail(resourceId) {
  return request.get(`/resources/${resourceId}`)
}

/**
 * 下载资源
 * @param {number} resourceId
 */
export function downloadResource(resourceId) {
  return request.get(`/resources/${resourceId}/download`)
}
