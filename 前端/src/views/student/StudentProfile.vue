<template>
  <div class="student-profile">
    <el-row :gutter="20">
      <!-- 左侧个人信息卡片 -->
      <el-col :span="8">
        <el-card shadow="hover" class="profile-card">
          <div class="profile-header">
            <el-avatar :size="80" :style="{ background: '#409EFF' }">
              {{ userStore.userInfo.name?.charAt(0) }}
            </el-avatar>
            <h3>{{ userStore.userInfo.name }}</h3>
            <el-tag :type="getRoleType()">{{ getRoleLabel() }}</el-tag>
          </div>
          
          <div class="profile-stats">
            <div class="stat-item">
              <span class="value">{{ stats.questions }}</span>
              <span class="label">提问数</span>
            </div>
            <div class="stat-item">
              <span class="value">{{ stats.answers }}</span>
              <span class="label">回答数</span>
            </div>
            <div class="stat-item">
              <span class="value">{{ stats.courses }}</span>
              <span class="label">学习课程</span>
            </div>
          </div>
          
          <el-divider />
          
          <div class="profile-info">
            <div class="info-item">
              <el-icon><User /></el-icon>
              <span>{{ userStore.userInfo.username }}</span>
            </div>
            <div class="info-item">
              <el-icon><Phone /></el-icon>
              <span>{{ userStore.userInfo.phone || '未设置' }}</span>
            </div>
            <div class="info-item">
              <el-icon><Message /></el-icon>
              <span>{{ userStore.userInfo.email || '未设置' }}</span>
            </div>
            <div class="info-item" v-if="userStore.userInfo.class">
              <el-icon><School /></el-icon>
              <span>{{ userStore.userInfo.class }}</span>
            </div>
          </div>
        </el-card>
        
        <!-- 人脸绑定 -->
        <el-card shadow="hover" class="face-card">
          <template #header>
            <span><el-icon><Coin /></el-icon> 人脸绑定</span>
          </template>
          <div class="face-content">
            <div class="face-status" :class="{ bound: isFaceBound }">
              <el-icon :size="48">
                {{ isFaceBound ? '<CircleCheck />' : '<Picture />' }}
              </el-icon>
              <p>{{ isFaceBound ? '已绑定人脸' : '未绑定人脸' }}</p>
            </div>
            <el-button type="primary" @click="handleBindFace">
              {{ isFaceBound ? '更换人脸' : '立即绑定' }}
            </el-button>
          </div>
        </el-card>
      </el-col>
      
      <!-- 右侧设置表单 -->
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>个人信息设置</span>
              <el-button type="primary" size="small" @click="saveProfile" :loading="saving">
                保存修改
              </el-button>
            </div>
          </template>
          
          <el-form 
            ref="formRef"
            :model="profileForm" 
            :rules="rules" 
            label-position="left"
            label-width="100px"
          >
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="profileForm.realName" />
            </el-form-item>
            
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="profileForm.phone" />
            </el-form-item>
            
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="profileForm.email" />
            </el-form-item>
            
            <el-form-item label="个人简介">
              <el-input
                v-model="profileForm.bio"
                type="textarea"
                :rows="4"
                placeholder="介绍一下自己..."
              />
            </el-form-item>
          </el-form>
        </el-card>
        
        <el-card shadow="hover" class="password-card">
          <template #header>
            <span>修改密码</span>
          </template>
          
          <el-form 
            ref="pwdFormRef"
            :model="passwordForm" 
            :rules="pwdRules" 
            label-position="left"
            label-width="100px"
          >
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password />
            </el-form-item>
            
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" show-password />
            </el-form-item>
            
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="changePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const formRef = ref(null)
const pwdFormRef = ref(null)
const saving = ref(false)
const isFaceBound = ref(false)

const stats = reactive({
  questions: 28,
  answers: 45,
  courses: 5
})

const profileForm = reactive({
  realName: '',
  phone: '',
  email: '',
  bio: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ]
}

const pwdRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const getRoleType = () => {
  const types = { student: 'primary', teacher: 'success', admin: 'warning' }
  return types[userStore.role] || 'info'
}

const getRoleLabel = () => {
  const labels = { student: '学生', teacher: '教师', admin: '管理员' }
  return labels[userStore.role] || '用户'
}

const initProfile = () => {
  const info = userStore.userInfo
  profileForm.realName = info.name || ''
  profileForm.phone = info.phone || ''
  profileForm.email = info.email || ''
  profileForm.bio = ''
}

const saveProfile = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    saving.value = true
    try {
      // 模拟保存
      await new Promise(resolve => setTimeout(resolve, 1000))
      userStore.setUserInfo({ ...userStore.userInfo, ...profileForm })
      ElMessage.success('保存成功')
    } finally {
      saving.value = false
    }
  })
}

const changePassword = async () => {
  if (!pwdFormRef.value) return
  
  await pwdFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    try {
      await new Promise(resolve => setTimeout(resolve, 1000))
      ElMessage.success('密码修改成功')
      passwordForm.oldPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
    } catch {
      ElMessage.error('密码修改失败')
    }
  })
}

const handleBindFace = () => {
  ElMessage.info('人脸绑定功能开发中...')
}

onMounted(() => {
  initProfile()
})
</script>

<style lang="scss" scoped>
.student-profile {
  .profile-card {
    .profile-header {
      text-align: center;
      padding: 20px 0;
      
      h3 {
        margin: 15px 0 10px;
        font-size: 20px;
      }
    }
    
    .profile-stats {
      display: flex;
      justify-content: space-around;
      padding: 20px 0;
      
      .stat-item {
        text-align: center;
        
        .value {
          display: block;
          font-size: 24px;
          font-weight: 600;
          color: #409EFF;
        }
        
        .label {
          font-size: 12px;
          color: #909399;
        }
      }
    }
    
    .profile-info {
      .info-item {
        display: flex;
        align-items: center;
        padding: 10px 0;
        color: #606266;
        
        .el-icon {
          margin-right: 10px;
          color: #909399;
        }
      }
    }
  }
  
  .face-card {
    margin-top: 20px;
    
    .face-content {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20px;
      
      .face-status {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 20px;
        color: #909399;
        
        &.bound {
          color: #67C23A;
        }
        
        p {
          margin-top: 10px;
        }
      }
    }
  }
  
  .password-card {
    margin-top: 20px;
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
