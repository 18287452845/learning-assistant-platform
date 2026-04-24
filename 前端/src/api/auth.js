import request from '@/utils/request'

// 认证相关API

/**
 * 用户登录
 * @param {Object} data { username, password }
 */
export function login(data) {
  return request.post('/auth/login', data)
}

/**
 * 用户注册
 * @param {Object} data
 */
export function register(data) {
  return request.post('/auth/register', data)
}

/**
 * 人脸登录
 * @param {string} imageData 人脸图片base64数据
 */
export function faceLogin(imageData) {
  return request.post('/auth/face-login', { image: imageData })
}

/**
 * 登出
 */
export function logout() {
  return request.post('/auth/logout')
}

/**
 * 获取验证码
 */
export function getCaptcha() {
  return request.get('/auth/captcha')
}
