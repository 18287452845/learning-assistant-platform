<template>
  <div class="login-wrapper">
    <!-- 装饰性背景 -->
    <div class="bg-decoration">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
      <div class="floating-dot dot-1"></div>
      <div class="floating-dot dot-2"></div>
      <div class="floating-dot dot-3"></div>
    </div>

    <!-- 左侧品牌区域 -->
    <div class="brand-section">
      <div class="brand-content">
        <div class="brand-logo">
          <div class="logo-icon">
            <svg viewBox="0 0 40 40" fill="none">
              <circle cx="20" cy="20" r="18" fill="rgba(255,255,255,0.2)"/>
              <path d="M20 8L12 16V28L20 32L28 28V16L20 8Z" stroke="white" stroke-width="2" fill="none"/>
              <circle cx="20" cy="20" r="4" fill="white"/>
            </svg>
          </div>
          <h1 class="brand-title">知行学堂</h1>
        </div>
        
        <p class="brand-slogan">让学习更高效，让知识更触手可及</p>
        
        <!-- 简洁的功能描述 - 去掉图标 -->
        <div class="feature-list">
          <div class="feature-item">
            <span class="feature-num">01</span>
            <div class="feature-text">
              <h3>智能问答</h3>
              <p>精准理解问题，即时给出答案</p>
            </div>
          </div>
          <div class="feature-item">
            <span class="feature-num">02</span>
            <div class="feature-text">
              <h3>个性化学习</h3>
              <p>贴合个人进度，动态调整内容</p>
            </div>
          </div>
          <div class="feature-item">
            <span class="feature-num">03</span>
            <div class="feature-text">
              <h3>师生互动</h3>
              <p>随时提问，即时获得专业解答</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧登录表单 -->
    <div class="form-section">
      <div class="form-container">
        <!-- 顶部角色切换 - 更自然的设计 -->
        <div class="role-switch">
          <button 
            v-for="item in roleOptions" 
            :key="item.value"
            :class="['role-btn', { active: loginForm.role === item.value }]"
            @click="loginForm.role = item.value"
          >
            {{ item.label }}
          </button>
        </div>

        <!-- 欢迎文字 -->
        <div class="welcome-text">
          <h2>欢迎回来</h2>
          <p>请登录您的账号继续学习</p>
        </div>

        <!-- 登录表单 -->
        <el-form 
          ref="formRef"
          :model="loginForm" 
          :rules="rules" 
          size="large"
          class="login-form"
        >
          <el-form-item prop="username">
            <div class="input-wrapper">
              <input 
                v-model="loginForm.username" 
                placeholder="请输入用户名/学号/工号"
                class="custom-input"
                autocomplete="username"
              />
            </div>
          </el-form-item>
          
          <el-form-item prop="password">
            <div class="input-wrapper">
              <input 
                v-model="loginForm.password" 
                type="password" 
                placeholder="请输入密码"
                class="custom-input"
                autocomplete="current-password"
              />
            </div>
          </el-form-item>
          
          <div class="form-options">
            <label class="remember-me">
              <input type="checkbox" v-model="loginForm.remember" />
              <span>记住登录状态</span>
            </label>
            <a href="#" class="forgot-link">忘记密码？</a>
          </div>
          
          <el-form-item>
            <button 
              type="button"
              class="login-btn"
              :class="{ loading: loading }"
              :disabled="loading"
              @click="handleLogin"
            >
              <span v-if="!loading">登 录</span>
              <span v-else class="loading-dots">登录中<span>.</span><span>.</span><span>.</span></span>
            </button>
          </el-form-item>
        </el-form>

        <!-- 底部注册提示 -->
        <div class="register-prompt">
          <span>还没有账号？</span>
          <router-link to="/register" class="register-link">立即加入</router-link>
        </div>

        <!-- 演示账号提示 - 折叠样式 -->
        <div class="demo-toggle" @click="showDemo = !showDemo">
          <span>试用账号</span>
          <span class="toggle-arrow" :class="{ active: showDemo }">▼</span>
        </div>
        
        <transition name="slide">
          <div class="demo-accounts" v-if="showDemo">
            <div class="demo-item" v-for="demo in demoAccounts" :key="demo.role">
              <span class="demo-role">{{ demo.label }}</span>
              <span class="demo-credential">{{ demo.username }} / 任意密码</span>
            </div>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const showDemo = ref(false)

const loginForm = reactive({
  role: 'student',
  username: '',
  password: '',
  remember: false
})

const roleOptions = [
  { value: 'student', label: '学生' },
  { value: 'teacher', label: '教师' },
  { value: 'admin', label: '管理员' }
]

const demoAccounts = [
  { role: 'student', label: '学生', username: 'student01' },
  { role: 'teacher', label: '教师', username: 'teacher01' },
  { role: 'admin', label: '管理员', username: 'admin01' }
]

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    // 模拟登录
    setTimeout(() => {
      ElMessage.success('登录成功')
      loading.value = false
      
      // 根据角色跳转到对应首页
      const roleRoutes = {
        student: '/student/home',
        teacher: '/teacher/home',
        admin: '/admin/home'
      }
      router.push(roleRoutes[loginForm.role])
    }, 1000)
  } catch (e) {
    console.log('验证失败')
  }
}
</script>

<style lang="scss" scoped>
// 登录页面专用样式
.login-wrapper {
  min-height: 100vh;
  display: flex;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #FFF8F0 0%, #FFE4D6 50%, #FFD4B8 100%);
}

// 装饰性背景
.bg-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
}

.shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.15;
}

.shape-1 {
  width: 600px;
  height: 600px;
  background: linear-gradient(135deg, #FF8C42, #FFB380);
  top: -200px;
  right: -100px;
  animation: float 20s ease-in-out infinite;
}

.shape-2 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #FFD700, #FFA500);
  bottom: -150px;
  left: -100px;
  animation: float 15s ease-in-out infinite reverse;
}

.shape-3 {
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, #FF6B6B, #FF8E53);
  top: 50%;
  left: 30%;
  animation: float 18s ease-in-out infinite;
}

.floating-dot {
  position: absolute;
  width: 8px;
  height: 8px;
  background: rgba(255, 140, 66, 0.4);
  border-radius: 50%;
}

.dot-1 { top: 20%; left: 15%; animation: pulse 3s infinite; }
.dot-2 { top: 60%; left: 25%; animation: pulse 4s infinite 1s; }
.dot-3 { top: 40%; right: 20%; animation: pulse 3.5s infinite 0.5s; }

@keyframes float {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(20px, -30px) rotate(5deg); }
  50% { transform: translate(-10px, 20px) rotate(-5deg); }
  75% { transform: translate(30px, 10px) rotate(3deg); }
}

@keyframes pulse {
  0%, 100% { opacity: 0.4; transform: scale(1); }
  50% { opacity: 0.8; transform: scale(1.5); }
}

// 左侧品牌区域
.brand-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;
  position: relative;
  z-index: 1;
}

.brand-content {
  max-width: 480px;
}

.brand-logo {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
}

.logo-icon {
  width: 56px;
  height: 56px;
}

.logo-icon svg {
  width: 100%;
  height: 100%;
}

.brand-title {
  font-size: 32px;
  font-weight: 700;
  color: #2D3436;
  letter-spacing: 2px;
}

.brand-slogan {
  font-size: 16px;
  color: #636E72;
  margin-bottom: 48px;
  line-height: 1.8;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  gap: 20px;
}

.feature-num {
  font-size: 28px;
  font-weight: 700;
  color: rgba(255, 140, 66, 0.25);
  font-family: 'Georgia', serif;
  line-height: 1;
}

.feature-text h3 {
  font-size: 16px;
  font-weight: 600;
  color: #2D3436;
  margin-bottom: 4px;
}

.feature-text p {
  font-size: 13px;
  color: #636E72;
  line-height: 1.6;
}

// 右侧表单区域
.form-section {
  width: 480px;
  background: #FFFFFF;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;
  position: relative;
  z-index: 1;
  box-shadow: -20px 0 60px rgba(0, 0, 0, 0.05);
}

.form-container {
  width: 100%;
  max-width: 360px;
}

// 角色切换
.role-switch {
  display: flex;
  gap: 4px;
  background: #F5F6F8;
  padding: 4px;
  border-radius: 10px;
  margin-bottom: 32px;
}

.role-btn {
  flex: 1;
  padding: 10px 16px;
  font-size: 14px;
  font-weight: 500;
  color: #636E72;
  background: transparent;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.25s ease;
}

.role-btn.active {
  background: #FFFFFF;
  color: #FF8C42;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

// 欢迎文字
.welcome-text {
  margin-bottom: 28px;
}

.welcome-text h2 {
  font-size: 26px;
  font-weight: 700;
  color: #2D3436;
  margin-bottom: 8px;
}

.welcome-text p {
  font-size: 14px;
  color: #636E72;
}

// 登录表单
.login-form {
  :deep(.el-form-item) {
    margin-bottom: 20px;
  }
  
  :deep(.el-form-item__error) {
    padding-top: 4px;
    font-size: 12px;
  }
}

.input-wrapper {
  position: relative;
}

.custom-input {
  width: 100%;
  height: 48px;
  padding: 0 16px;
  font-size: 15px;
  color: #2D3436;
  background: #FAFBFC;
  border: 1px solid #E8ECEF;
  border-radius: 10px;
  outline: none;
  transition: all 0.25s ease;
}

.custom-input::placeholder {
  color: #B2BEC3;
}

.custom-input:focus {
  border-color: #FF8C42;
  background: #FFFFFF;
  box-shadow: 0 0 0 3px rgba(255, 140, 66, 0.1);
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 13px;
  color: #636E72;
}

.remember-me input {
  width: 16px;
  height: 16px;
  accent-color: #FF8C42;
}

.forgot-link {
  font-size: 13px;
  color: #FF8C42;
  text-decoration: none;
  transition: color 0.2s;
}

.forgot-link:hover {
  color: #E67332;
}

.login-btn {
  width: 100%;
  height: 50px;
  font-size: 16px;
  font-weight: 600;
  color: #FFFFFF;
  background: linear-gradient(135deg, #FF8C42 0%, #FF6B35 100%);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.25s ease;
  letter-spacing: 4px;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(255, 140, 66, 0.35);
}

.login-btn:active {
  transform: translateY(0);
}

.login-btn.loading {
  background: linear-gradient(135deg, #FFB380 0%, #FF8C42 100%);
  cursor: not-allowed;
}

.loading-dots span {
  animation: blink 1.4s infinite both;
}

.loading-dots span:nth-child(2) {
  animation-delay: 0.2s;
}

.loading-dots span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes blink {
  0%, 80%, 100% { opacity: 0; }
  40% { opacity: 1; }
}

// 注册提示
.register-prompt {
  text-align: center;
  margin-top: 28px;
  font-size: 14px;
  color: #636E72;
}

.register-link {
  color: #FF8C42;
  text-decoration: none;
  font-weight: 500;
  margin-left: 4px;
}

.register-link:hover {
  text-decoration: underline;
}

// 演示账号
.demo-toggle {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  margin-top: 24px;
  padding: 12px;
  font-size: 13px;
  color: #B2BEC3;
  cursor: pointer;
  transition: color 0.2s;
}

.demo-toggle:hover {
  color: #636E72;
}

.toggle-arrow {
  font-size: 10px;
  transition: transform 0.3s ease;
}

.toggle-arrow.active {
  transform: rotate(180deg);
}

.demo-accounts {
  background: #FAFBFC;
  border-radius: 10px;
  padding: 16px;
  margin-top: 8px;
}

.demo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  font-size: 13px;
  
  &:not(:last-child) {
    border-bottom: 1px dashed #E8ECEF;
  }
}

.demo-role {
  color: #FF8C42;
  font-weight: 500;
}

.demo-credential {
  color: #636E72;
  font-family: 'Monaco', 'Consolas', monospace;
  font-size: 12px;
}

// 动画
.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

// 响应式
@media (max-width: 1024px) {
  .brand-section {
    display: none;
  }
  
  .form-section {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .form-section {
    padding: 40px 24px;
  }
  
  .welcome-text h2 {
    font-size: 22px;
  }
}
</style>
