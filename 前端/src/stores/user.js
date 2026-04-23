import { defineStore } from 'pinia'
import { setToken, removeToken, setUserRole, getUserRole, getToken } from '@/utils/auth'
import { mockUsers } from '@/mock/data'

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
    // Mock登录
    async loginMock(username, password, role) {
      this.isLoading = true
      try {
        // 模拟登录延迟
        await new Promise(resolve => setTimeout(resolve, 800))
        
        // Mock验证
        const mockUser = mockUsers[role]
        if (!mockUser) {
          throw new Error('角色不存在')
        }
        
        // 生成mock token
        const mockToken = `mock_token_${role}_${Date.now()}`
        
        this.token = mockToken
        this.userInfo = { ...mockUser }
        this.role = role
        
        setToken(mockToken)
        setUserRole(role)
        
        return { success: true, user: mockUser }
      } catch (error) {
        return { success: false, message: error.message }
      } finally {
        this.isLoading = false
      }
    },
    
    // 退出登录
    async logout() {
      this.token = ''
      this.userInfo = {}
      this.role = ''
      removeToken()
    },
    
    // 设置用户信息
    setUserInfo(info) {
      this.userInfo = info
      if (info.role) {
        this.role = info.role
        setUserRole(info.role)
      }
    },
    
    // 清空状态
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
