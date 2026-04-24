import request from '@/utils/request'

// 问答相关API

/**
 * 提问（通用AI）
 * @param {Object} data { question }
 */
export function askGeneralQuestion(data) {
  return request.post('/qa/ask', data, { params: { type: 'global' } })
}

/**
 * 提问（个性化AI）
 * @param {Object} data { question }
 */
export function askPersonalQuestion(data) {
  return request.post('/qa/ask', data, { params: { type: 'personal' } })
}

/**
 * 获取问答历史
 * @param {Object} params { courseId, page, size }
 */
export function getQAHistory(params) {
  return request.get('/qa/history', { params })
}

/**
 * 获取问答详情
 * @param {number} id
 */
export function getQADetail(id) {
  return request.get(`/qa/${id}`)
}

/**
 * 评价问答
 * @param {number} id
 * @param {number} helpful 1=有帮助 0=无帮助
 */
export function rateQA(id, helpful) {
  return request.post(`/qa/${id}/rate`, null, { params: { helpful } })
}

/**
 * 获取课程知识点
 * @param {number} courseId
 */
export function getKnowledgePoints(courseId) {
  return request.get('/qa/knowledge', { params: { courseId } })
}

/**
 * 生成学习计划
 * @param {number} courseId
 * @param {number} days
 */
export function generateStudyPlan(courseId, days = 7) {
  return request.post('/qa/plan', null, { params: { courseId, days } })
}

/**
 * 提交答疑请求（人工答疑）
 * @param {Object} data { courseId, question, teacherId }
 */
export function submitQARequest(data) {
  return request.post('/qa-request', null, { params: data })
}

/**
 * 获取待处理答疑列表（教师）
 * @param {Object} params { teacherId, page, size }
 */
export function getPendingRequests(params) {
  return request.get('/qa-request/pending', { params })
}

/**
 * 获取我的答疑请求（学生）
 * @param {Object} params { page, size }
 */
export function getMyRequests(params) {
  return request.get('/qa-request/my', { params })
}

/**
 * 回答问题（教师）
 * @param {number} id
 * @param {Object} data { answer, knowledgePoints }
 */
export function answerQuestion(id, data) {
  return request.put(`/qa-request/${id}`, data)
}

/**
 * 获取答疑统计（教师/管理员）
 * @param {Object} params { teacherId }
 */
export function getQARequestStatistics(params) {
  return request.get('/qa-request/statistics', { params })
}
