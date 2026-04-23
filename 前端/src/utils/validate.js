// 表单验证工具

/**
 * 验证用户名/学号/工号
 * @param {string} value
 * @returns {boolean}
 */
export function validateUsername(value) {
  if (!value) return false
  // 学号：10位数字，工号：6位数字，用户名：字母数字下划线
  const studentRegex = /^\d{10}$/
  const teacherRegex = /^\d{6}$/
  const usernameRegex = /^[a-zA-Z0-9_]{4,20}$/
  return studentRegex.test(value) || teacherRegex.test(value) || usernameRegex.test(value)
}

/**
 * 验证密码
 * @param {string} value
 * @returns {boolean}
 */
export function validatePassword(value) {
  if (!value) return false
  // 至少8位，包含字母和数字
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*#?&]{8,}$/
  return passwordRegex.test(value)
}

/**
 * 验证手机号
 * @param {string} value
 * @returns {boolean}
 */
export function validatePhone(value) {
  if (!value) return true // 手机号可选
  const phoneRegex = /^1[3-9]\d{9}$/
  return phoneRegex.test(value)
}

/**
 * 验证邮箱
 * @param {string} value
 * @returns {boolean}
 */
export function validateEmail(value) {
  if (!value) return true // 邮箱可选
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
  return emailRegex.test(value)
}

/**
 * 验证姓名
 * @param {string} value
 * @returns {boolean}
 */
export function validateName(value) {
  if (!value) return false
  const nameRegex = /^[\u4e00-\u9fa5]{2,10}$/
  return nameRegex.test(value)
}

// Element Plus 表单验证规则
export const rules = {
  username: [
    { required: true, message: '请输入用户名/学号/工号', trigger: 'blur' },
    { min: 4, max: 20, message: '长度在 4 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' },
    { pattern: /^(?=.*[A-Za-z])(?=.*\d)/, message: '密码需包含字母和数字', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { pattern: /^[\u4e00-\u9fa5]{2,10}$/, message: '姓名需为2-10个中文', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 4, message: '验证码为4位', trigger: 'blur' }
  ],
  question: [
    { required: true, message: '请输入问题', trigger: 'blur' },
    { min: 10, message: '问题至少10个字', trigger: 'blur' },
    { max: 500, message: '问题不能超过500字', trigger: 'blur' }
  ]
}
