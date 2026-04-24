<template>
  <div class="student-profile">
    <el-row :gutter="20">
      <!-- 左侧个人信息卡片 -->
      <el-col :span="8">
        <el-card shadow="hover" class="profile-card" v-loading="profileLoading">
          <div class="profile-header">
            <el-avatar :size="80" :style="{ background: '#409EFF' }">
              {{ (userStore.userInfo.realName || userStore.userInfo.username || '').charAt(0) }}
            </el-avatar>
            <h3>{{ userStore.userInfo.realName || userStore.userInfo.username }}</h3>
            <el-tag :type="getRoleType()">{{ getRoleLabel() }}</el-tag>
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
            <div class="info-item" v-if="userStore.userInfo.genderDesc">
              <el-icon><User /></el-icon>
              <span>{{ userStore.userInfo.genderDesc }}</span>
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
                <CircleCheck v-if="isFaceBound" />
                <Picture v-else />
              </el-icon>
              <p>{{ isFaceBound ? '已绑定人脸' : '未绑定人脸' }}</p>
            </div>
            <el-button :type="isFaceBound ? 'danger' : 'primary'" @click="handleBindFace">
              {{ isFaceBound ? '解除绑定' : '立即绑定' }}
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getUserInfo, updateUserInfo, changePassword as changePasswordApi, uploadAvatar, bindFace, unbindFace } from '@/api/user'

const userStore = useUserStore()
const formRef = ref(null)
const pwdFormRef = ref(null)
const saving = ref(false)
const changingPwd = ref(false)
const isFaceBound = ref(false)
const profileLoading = ref(false)

const profileForm = reactive({
  realName: '',
  phone: '',
  email: ''
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

const initProfile = (user) => {
  profileForm.realName = user.realName || ''
  profileForm.phone = user.phone || ''
  profileForm.email = user.email || ''
  isFaceBound.value = !!user.faceRegistered
}

const fetchUserInfo = async () => {
  profileLoading.value = true
  try {
    const user = await getUserInfo()
    // Update store with fresh data
    userStore.setUserInfo(user)
    initProfile(user)
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  } finally {
    profileLoading.value = false
  }
}

const saveProfile = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    saving.value = true
    try {
      await updateUserInfo({
        realName: profileForm.realName,
        phone: profileForm.phone,
        email: profileForm.email
      })
      // Refresh user info from server
      await fetchUserInfo()
      ElMessage.success('保存成功')
    } catch (error) {
      ElMessage.error(error.message || '保存失败')
    } finally {
      saving.value = false
    }
  })
}

const changePassword = async () => {
  if (!pwdFormRef.value) return

  await pwdFormRef.value.validate(async (valid) => {
    if (!valid) return

    changingPwd.value = true
    try {
      await changePasswordApi({
        oldPassword: passwordForm.oldPassword,
        newPassword: passwordForm.newPassword,
        confirmPassword: passwordForm.confirmPassword
      })
      ElMessage.success('密码修改成功')
      passwordForm.oldPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
    } catch (error) {
      ElMessage.error(error.message || '密码修改失败')
    } finally {
      changingPwd.value = false
    }
  })
}

const handleBindFace = async () => {
  if (isFaceBound.value) {
    // Already bound - confirm then unbind
    try {
      await ElMessageBox.confirm(
        '确定要解除人脸绑定吗？解除后将无法使用人脸识别功能。',
        '解除人脸绑定',
        { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
      )
      await unbindFace()
      isFaceBound.value = false
      ElMessage.success('人脸绑定已解除')
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error(error.message || '解绑失败')
      }
    }
  } else {
    // Not bound - confirm then bind with mock image data
    try {
      await ElMessageBox.confirm(
        '确定要绑定人脸吗？系统将使用模拟数据完成绑定。',
        '人脸绑定',
        { confirmButtonText: '确定', cancelButtonText: '取消', type: 'info' }
      )
      // Mock image data since we don't have a real camera
      const mockImageData = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg=='
      await bindFace(mockImageData)
      isFaceBound.value = true
      ElMessage.success('人脸绑定成功')
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error(error.message || '绑定失败')
      }
    }
  }
}

const handleAvatarUpload = async (uploadFile) => {
  const formData = new FormData()
  formData.append('file', uploadFile.raw)
  try {
    await uploadAvatar(formData)
    await fetchUserInfo()
    ElMessage.success('头像上传成功')
  } catch (error) {
    ElMessage.error(error.message || '头像上传失败')
  }
}

onMounted(() => {
  fetchUserInfo()
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
