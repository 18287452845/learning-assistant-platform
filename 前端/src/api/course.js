import request from './index'

// 课程相关API

/**
 * 获取课程列表
 * @param {Object} params
 */
export function getCourseList(params) {
  return request.get('/course/list', { params })
}

/**
 * 获取课程详情
 * @param {number} id
 */
export function getCourseDetail(id) {
  return request.get(`/course/detail/${id}`)
}

/**
 * 获取热门课程
 */
export function getHotCourses() {
  return request.get('/course/hot')
}

/**
 * 获取推荐课程
 */
export function getRecommendCourses() {
  return request.get('/course/recommend')
}

/**
 * 获取课程资源列表
 * @param {number} courseId
 * @param {Object} params
 */
export function getCourseResources(courseId, params) {
  return request.get(`/course/${courseId}/resources`, { params })
}

/**
 * 获取课程知识点
 * @param {number} courseId
 */
export function getCourseKnowledgePoints(courseId) {
  return request.get(`/course/${courseId}/knowledge-points`)
}

/**
 * 获取学习进度
 * @param {number} courseId
 */
export function getLearningProgress(courseId) {
  return request.get(`/course/${courseId}/progress`)
}

/**
 * 更新学习进度
 * @param {number} courseId
 * @param {Object} data
 */
export function updateLearningProgress(courseId, data) {
  return request.post(`/course/${courseId}/progress`, data)
}

/**
 * 获取我的课程
 */
export function getMyCourses() {
  return request.get('/course/my')
}
