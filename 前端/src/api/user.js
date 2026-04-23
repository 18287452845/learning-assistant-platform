import request from './index'

// 用户相关API

/**
 * 获取用户信息
 */
export function getUserInfo() {
  return request.get('/user/info')
}

/**
 * 更新用户信息
 * @param {Object} data
 */
export function updateUserInfo(data) {
  return request.put('/user/info', data)
}

/**
 * 修改密码
 * @param {Object} data { oldPassword, newPassword }
 */
export function changePassword(data) {
  return request.post('/user/change-password', data)
}

/**
 * 上传头像
 * @param {FormData} formData
 */
export function uploadAvatar(formData) {
  return request.post('/user/avatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 绑定人脸
 * @param {string} imageData 人脸图片base64数据
 */
export function bindFace(imageData) {
  return request.post('/user/bind-face', { image: imageData })
}

/**
 * 解绑人脸
 */
export function unbindFace() {
  return request.delete('/user/unbind-face')
}

/**
 * 获取学习记录
 * @param {Object} params
 */
export function getLearningRecords(params) {
  return request.get('/user/learning-records', { params })
}

/**
 * 获取错题本
 * @param {Object} params
 */
export function getWrongQuestions(params) {
  return request.get('/user/wrong-questions', { params })
}

/**
 * 获取问答历史
 * @param {Object} params
 */
export function getQAHistory(params) {
  return request.get('/user/qa-history', { params })
}
