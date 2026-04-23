// Token管理工具

const TOKEN_KEY = 'learning_assistant_token'
const USER_ROLE_KEY = 'learning_assistant_role'

/**
 * 获取Token
 */
export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

/**
 * 设置Token
 */
export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

/**
 * 移除Token
 */
export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_ROLE_KEY)
}

/**
 * 获取用户角色
 */
export function getUserRole() {
  return localStorage.getItem(USER_ROLE_KEY)
}

/**
 * 设置用户角色
 */
export function setUserRole(role) {
  localStorage.setItem(USER_ROLE_KEY, role)
}

/**
 * 检查是否已登录
 */
export function isLoggedIn() {
  return !!getToken()
}

/**
 * 检查是否有权限访问
 */
export function hasPermission(requiredRoles) {
  const role = getUserRole()
  if (!role) return false
  if (!requiredRoles || !requiredRoles.length) return true
  return requiredRoles.includes(role)
}
