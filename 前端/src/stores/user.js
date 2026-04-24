import { defineStore } from 'pinia'
import { setToken, removeToken, setUserRole, getUserRole, getToken } from '@/utils/auth'
import { login as loginApi, logout as logoutApi } from '@/api/auth'
import { getUserInfo } from '@/api/user'

const roleMap = {
  'STUDENT': 'student',
  'TEACHER': 'teacher',
  'ADMIN': 'admin'
}

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken() || '',
    userInfo: {},
    role: getUserRole() || '',
    isLoading: false
  }),

  getters: {
    isLoggedIn: state => !!state.token,
    isStudent: state => state.role === 'student',
    isTeacher: state => state.role === 'teacher',
    isAdmin: state => state.role === 'admin'
  },

  actions: {
    async login(username, password) {
      this.isLoading = true
      try {
        const res = await loginApi({ username, password })
        // res = LoginVO: { accessToken, tokenType, expiresIn, user }
        const { accessToken, user } = res
        const role = user.roles && user.roles.length > 0
          ? (roleMap[user.roles[0]] || 'student')
          : 'student'

        this.token = accessToken
        this.userInfo = user
        this.role = role

        setToken(accessToken)
        setUserRole(role)

        return { success: true, user, role }
      } catch (error) {
        return { success: false, message: error.message || '登录失败' }
      } finally {
        this.isLoading = false
      }
    },

    async fetchUserInfo() {
      try {
        const user = await getUserInfo()
        this.userInfo = user
        if (user.roles && user.roles.length > 0) {
          this.role = roleMap[user.roles[0]] || this.role
          setUserRole(this.role)
        }
      } catch (e) {
        // ignore
      }
    },

    async logout() {
      try {
        await logoutApi()
      } catch (e) {
        // ignore
      }
      this.token = ''
      this.userInfo = {}
      this.role = ''
      removeToken()
    },

    setUserInfo(info) {
      this.userInfo = info
      if (info.role) {
        this.role = info.role
        setUserRole(info.role)
      }
    },

    reset() {
      this.token = ''
      this.userInfo = {}
      this.role = ''
      removeToken()
    }
  },

  persist: {
    enabled: true,
    strategies: [
      {
        key: 'user',
        storage: localStorage,
        paths: ['token', 'userInfo', 'role']
      }
    ]
  }
})
