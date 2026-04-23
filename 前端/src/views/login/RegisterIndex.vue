<template>
  <div class="register-container">
    <div class="register-card">
      <div class="card-header">
        <el-button text @click="$router.push('/login')">
          <el-icon><ArrowLeft /></el-icon> 返回登录
        </el-button>
        <h2>用户注册</h2>
      </div>
      
      <el-steps :active="currentStep" finish-status="success" class="register-steps">
        <el-step title="选择角色" />
        <el-step title="填写信息" />
        <el-step title="注册成功" />
      </el-steps>
      
      <!-- 步骤1: 选择角色 -->
      <div v-if="currentStep === 0" class="step-content">
        <h3>请选择您的身份</h3>
        <div class="role-cards">
          <div 
            class="role-card"
            :class="{ active: registerForm.role === 'student' }"
            @click="registerForm.role = 'student'"
          >
            <div class="role-icon">
              <el-icon :size="48"><User /></el-icon>
            </div>
            <h4>学生</h4>
            <p>我可以上传课程资料、提出问题、获得AI智能答疑</p>
          </div>
          
          <div 
            class="role-card"
            :class="{ active: registerForm.role === 'teacher' }"
            @click="registerForm.role = 'teacher'"
          >
            <div class="role-icon">
              <el-icon :size="48"><Reading /></el-icon>
            </div>
            <h4>教师</h4>
            <p>我可以管理课程、回答学生问题、查看学习统计</p>
          </div>
        </div>
        
        <div class="step-actions">
          <el-button type="primary" size="large" @click="nextStep" :disabled="!registerForm.role">
            下一步 <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
      </div>
      
      <!-- 步骤2: 填写信息 -->
      <div v-if="currentStep === 1" class="step-content">
        <el-form 
          ref="formRef"
          :model="registerForm" 
          :rules="step2Rules" 
          label-position="top"
          size="large"
        >
          <!-- 学生表单 -->
          <template v-if="registerForm.role === 'student'">
            <el-form-item label="学号" prop="username">
              <el-input v-model="registerForm.username" placeholder="请输入10位学号">
                <template #prefix><el-icon><Postcard /></el-icon></template>
              </el-input>
            </el-form-item>
          </template>
          
          <!-- 教师表单 -->
          <template v-else>
            <el-form-item label="工号" prop="username">
              <el-input v-model="registerForm.username" placeholder="请输入6位工号">
                <template #prefix><el-icon><Postcard /></el-icon></template>
              </el-input>
            </el-form-item>
          </template>
          
          <el-form-item label="真实姓名" prop="realName">
            <el-input v-model="registerForm.realName" placeholder="请输入真实姓名">
              <template #prefix><el-icon><User /></el-icon></template>
            </el-input>
          </el-form-item>
          
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="registerForm.phone" placeholder="请输入手机号">
              <template #prefix><el-icon><Phone /></el-icon></template>
            </el-input>
          </el-form-item>
          
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="registerForm.email" placeholder="请输入邮箱">
              <template #prefix><el-icon><Message /></el-icon></template>
            </el-input>
          </el-form-item>
          
          <el-form-item label="密码" prop="password">
            <el-input v-model="registerForm.password" type="password" placeholder="请设置密码（至少6位，包含字母和数字）" show-password>
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>
          
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" show-password>
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>
        </el-form>
        
        <div class="step-actions">
          <el-button size="large" @click="prevStep">
            <el-icon><ArrowLeft /></el-icon> 上一步
          </el-button>
          <el-button type="primary" size="large" @click="handleRegister" :loading="loading">
            注册
          </el-button>
        </div>
      </div>
      
      <!-- 步骤3: 注册成功 -->
      <div v-if="currentStep === 2" class="step-content success-step">
        <div class="success-icon">
          <el-icon :size="80" color="#67C23A"><CircleCheck /></el-icon>
        </div>
        <h3>注册成功！</h3>
        <p>欢迎加入智能学习助手，您的账号：{{ registerForm.username }}</p>
        
        <div class="step-actions">
          <el-button type="primary" size="large" @click="$router.push('/login')">
            立即登录
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { validatePassword, validatePhone, validateEmail } from '@/utils/validate'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const currentStep = ref(0)

const registerForm = reactive({
  role: '',
  username: '',
  realName: '',
  phone: '',
  email: '',
  password: '',
  confirmPassword: ''
})

// 自定义验证规则
const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const validateUsernameRule = (rule, value, callback) => {
  if (registerForm.role === 'student') {
    if (!/^\d{10}$/.test(value)) {
      callback(new Error('学号必须为10位数字'))
    } else {
      callback()
    }
  } else if (registerForm.role === 'teacher') {
    if (!/^\d{6}$/.test(value)) {
      callback(new Error('工号必须为6位数字'))
    } else {
      callback()
    }
  } else {
    callback()
  }
}

const step2Rules = computed(() => ({
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { validator: validateUsernameRule, trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { pattern: /^[\u4e00-\u9fa5]{2,10}$/, message: '姓名需为2-10个中文', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}))

const nextStep = () => {
  if (!registerForm.role) {
    ElMessage.warning('请选择注册角色')
    return
  }
  currentStep.value++
}

const prevStep = () => {
  currentStep.value--
}

const handleRegister = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    
    try {
      // 模拟注册延迟
      await new Promise(resolve => setTimeout(resolve, 1500))
      
      // Mock注册成功
      ElMessage.success('注册成功！')
      currentStep.value = 2
    } catch (error) {
      ElMessage.error('注册失败，请稍后重试')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style lang="scss" scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
}

.register-card {
  width: 100%;
  max-width: 600px;
  background: white;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  
  .card-header {
    text-align: center;
    margin-bottom: 30px;
    
    h2 {
      font-size: 28px;
      color: #303133;
      margin-top: 15px;
    }
  }
  
  .register-steps {
    margin-bottom: 40px;
  }
}

.step-content {
  min-height: 300px;
  
  h3 {
    text-align: center;
    color: #606266;
    margin-bottom: 30px;
  }
  
  .role-cards {
    display: flex;
    gap: 20px;
    margin-bottom: 30px;
    
    .role-card {
      flex: 1;
      padding: 30px 20px;
      border: 2px solid #e4e7ed;
      border-radius: 12px;
      text-align: center;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        border-color: #409EFF;
        transform: translateY(-5px);
      }
      
      &.active {
        border-color: #409EFF;
        background: #ecf5ff;
        
        .role-icon {
          color: #409EFF;
        }
      }
      
      .role-icon {
        margin-bottom: 15px;
        color: #909399;
        transition: color 0.3s;
      }
      
      h4 {
        font-size: 18px;
        color: #303133;
        margin-bottom: 10px;
      }
      
      p {
        font-size: 13px;
        color: #909399;
        line-height: 1.6;
      }
    }
  }
  
  .step-actions {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-top: 30px;
  }
}

.success-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  
  .success-icon {
    margin-bottom: 20px;
    animation: scaleIn 0.5s ease;
  }
  
  h3 {
    font-size: 24px;
    color: #303133;
    margin-bottom: 10px;
  }
  
  p {
    color: #606266;
    margin-bottom: 30px;
  }
  
  @keyframes scaleIn {
    0% {
      transform: scale(0);
    }
    50% {
      transform: scale(1.2);
    }
    100% {
      transform: scale(1);
    }
  }
}

@media (max-width: 600px) {
  .register-card {
    padding: 20px;
  }
  
  .role-cards {
    flex-direction: column;
  }
}
</style>
