<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapsed ? '72px' : '240px'" class="sidebar">
      <div class="logo-area">
        <div class="logo-icon">
          <svg viewBox="0 0 32 32" fill="none">
            <circle cx="16" cy="16" r="14" fill="rgba(255,255,255,0.15)"/>
            <path d="M16 6L10 12V22L16 26L22 22V12L16 6Z" stroke="white" stroke-width="1.5" fill="none"/>
            <circle cx="16" cy="16" r="3" fill="white"/>
          </svg>
        </div>
        <transition name="fade">
          <span v-if="!isCollapsed" class="logo-text">管理后台</span>
        </transition>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapsed"
        :collapse-transition="false"
        router
        class="sidebar-menu"
      >
        <el-menu-item index="/admin/home">
          <span class="menu-dot"></span>
          <span>首页</span>
        </el-menu-item>
        
        <el-menu-item index="/admin/users">
          <span class="menu-dot"></span>
          <span>用户管理</span>
        </el-menu-item>
        
        <el-menu-item index="/admin/courses">
          <span class="menu-dot"></span>
          <span>课程管理</span>
        </el-menu-item>
        
        <el-menu-item index="/admin/review">
          <span class="menu-dot"></span>
          <span>资源审核</span>
        </el-menu-item>
        
        <el-menu-item index="/admin/monitor">
          <span class="menu-dot"></span>
          <span>系统监控</span>
        </el-menu-item>
        
        <el-menu-item index="/admin/profile">
          <span class="menu-dot"></span>
          <span>个人中心</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container>
      <!-- 顶部导航 -->
      <el-header class="header">
        <div class="header-left">
          <button class="collapse-btn" @click="toggleSidebar">
            <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2">
              <path v-if="isCollapsed" d="M4 6h16M4 12h16M4 18h16"/>
              <path v-else d="M4 6h16M4 12h10M4 18h16"/>
            </svg>
          </button>
          <Breadcrumb />
        </div>
        
        <div class="header-right">
          <div class="user-dropdown" @click="showUserMenu = !showUserMenu">
            <div class="user-avatar" :style="{ background: 'linear-gradient(135deg, #7C4DFF, #651FFF)' }">
              {{ userStore.userInfo.name?.charAt(0) }}
            </div>
            <span class="user-name">{{ userStore.userInfo.name }}</span>
            <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M6 9l6 6 6-6"/>
            </svg>
            
            <transition name="dropdown">
              <div class="dropdown-menu" v-if="showUserMenu" @click.stop>
                <div class="dropdown-item" @click="handleCommand('profile')">个人中心</div>
                <div class="dropdown-divider"></div>
                <div class="dropdown-item danger" @click="handleCommand('logout')">退出登录</div>
              </div>
            </transition>
          </div>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const appStore = useAppStore()

const showUserMenu = ref(false)

const isCollapsed = computed(() => appStore.sidebarCollapsed)
const activeMenu = computed(() => route.path)

const toggleSidebar = () => {
  appStore.toggleSidebar()
}

const handleCommand = async (command) => {
  showUserMenu.value = false
  
  switch (command) {
    case 'profile':
      router.push('/admin/profile')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await userStore.logout()
        ElMessage.success('已退出登录')
        router.push('/login')
      } catch {
        // 取消退出
      }
      break
  }
}

const handleClickOutside = (e) => {
  if (showUserMenu.value) {
    showUserMenu.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
  background: #F5F6F8;
}

.sidebar {
  background: linear-gradient(180deg, #7C4DFF 0%, #651FFF 100%);
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
}

.logo-area {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo-icon {
  width: 36px;
  height: 36px;
  flex-shrink: 0;
}

.logo-icon svg {
  width: 100%;
  height: 100%;
}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  color: #FFFFFF;
  letter-spacing: 1px;
  white-space: nowrap;
}

.sidebar-menu {
  border: none;
  background: transparent;
  margin-top: 12px;
  
  :deep(.el-menu-item) {
    height: 50px;
    line-height: 50px;
    margin: 4px 10px;
    padding: 0 14px !important;
    color: rgba(255, 255, 255, 0.75);
    border-radius: 10px;
    font-size: 14px;
    display: flex;
    align-items: center;
    gap: 12px;
    
    &:hover {
      background: rgba(255, 255, 255, 0.15);
      color: #FFFFFF;
    }
    
    &.is-active {
      background: rgba(255, 255, 255, 0.2);
      color: #FFFFFF;
      font-weight: 500;
      
      .menu-dot {
        background: #FFFFFF;
      }
    }
  }
}

.menu-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.4);
  transition: all 0.2s ease;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: #FFFFFF;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #F5F6F8;
  border: none;
  border-radius: 8px;
  color: #636E72;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover {
    background: #E8ECEF;
    color: #2D3436;
  }
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-dropdown {
  position: relative;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 12px 6px 6px;
  background: #F5F6F8;
  border-radius: 24px;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover {
    background: #E8ECEF;
  }
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 600;
  color: #FFFFFF;
}

.user-name {
  font-size: 14px;
  color: #2D3436;
  font-weight: 500;
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  min-width: 160px;
  background: #FFFFFF;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  padding: 8px;
  z-index: 1000;
}

.dropdown-item {
  padding: 10px 14px;
  font-size: 14px;
  color: #2D3436;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.15s ease;
  
  &:hover {
    background: #F5F6F8;
  }
  
  &.danger {
    color: #EF5350;
    
    &:hover {
      background: #FFEBEE;
    }
  }
}

.dropdown-divider {
  height: 1px;
  background: #F0F2F5;
  margin: 6px 0;
}

.main-content {
  padding: 0;
  background: #F5F6F8;
  overflow-y: auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.2s ease;
}

.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>
