import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    sidebarCollapsed: false,
    theme: 'light',
    device: 'desktop',
    language: 'zh-CN',
    cacheViews: []
  }),
  
  getters: {
    sidebarStatus: state => state.sidebarCollapsed ? 'closed' : 'opened'
  },
  
  actions: {
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed
    },
    
    setSidebarCollapsed(collapsed) {
      this.sidebarCollapsed = collapsed
    },
    
    setTheme(theme) {
      this.theme = theme
    },
    
    setDevice(device) {
      this.device = device
    },
    
    setLanguage(language) {
      this.language = language
    },
    
    addCacheView(view) {
      if (!this.cacheViews.includes(view)) {
        this.cacheViews.push(view)
      }
    },
    
    removeCacheView(view) {
      const index = this.cacheViews.indexOf(view)
      if (index > -1) {
        this.cacheViews.splice(index, 1)
      }
    }
  }
})
