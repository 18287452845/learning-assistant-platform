import request from './index'

// 问答相关API

/**
 * 提问（通用AI）
 * @param {Object} data { question }
 */
export function askGeneralQuestion(data) {
  return request.post('/qa/ask/general', data)
}

/**
 * 提问（个性化AI）
 * @param {Object} data { question }
 */
export function askPersonalQuestion(data) {
  return request.post('/qa/ask/personal', data)
}

/**
 * 获取问答历史
 * @param {Object} params
 */
export function getQAHistory(params) {
  return request.get('/qa/history', { params })
}

/**
 * 获取问答详情
 * @param {number} id
 */
export function getQADetail(id) {
  return request.get(`/qa/detail/${id}`)
}

/**
 * 收藏问答
 * @param {number} id
 */
export function favoriteQA(id) {
  return request.post(`/qa/favorite/${id}`)
}

/**
 * 取消收藏
 * @param {number} id
 */
export function unfavoriteQA(id) {
  return request.delete(`/qa/favorite/${id}`)
}

/**
 * 获取推荐问题
 * @param {string} question
 */
export function getRecommendedQuestions(question) {
  return request.get('/qa/recommend', { params: { question } })
}

/**
 * 获取薄弱点分析
 * @param {Object} params
 */
export function getWeakPointsAnalysis(params) {
  return request.get('/qa/weak-points', { params })
}

/**
 * 获取推荐学习资源
 * @param {Object} params
 */
export function getRecommendedResources(params) {
  return request.get('/qa/recommended-resources', { params })
}

/**
 * 提交追问
 * @param {Object} data { parentId, question }
 */
export function sendFollowUp(data) {
  return request.post('/qa/follow-up', data)
}

/**
 * 评价问答
 * @param {number} id
 * @param {boolean} helpful
 */
export function rateQA(id, helpful) {
  return request.post(`/qa/rate/${id}`, { helpful })
}
